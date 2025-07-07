package com.codeagents.mobile.services

import com.codeagents.mobile.models.Server
import com.codeagents.mobile.models.FileNode
import com.jcraft.jsch.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.*

class SSHService {
    private val connectionPool = mutableMapOf<String, Session>()
    
    suspend fun connect(server: Server, password: String): Result<Session> = withContext(Dispatchers.IO) {
        try {
            val jsch = JSch()
            val session = jsch.getSession(server.username, server.host, server.port)
            session.setPassword(password)
            session.setConfig("StrictHostKeyChecking", "no")
            session.connect()
            
            connectionPool[server.id] = session
            Result.success(session)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun executeCommand(serverId: String, command: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val session = connectionPool[serverId] ?: return@withContext Result.failure(Exception("Not connected"))
            val channel = session.openChannel("exec") as ChannelExec
            channel.setCommand(command)
            channel.connect()
            
            val inputStream = channel.inputStream
            val reader = BufferedReader(InputStreamReader(inputStream))
            val output = StringBuilder()
            
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                output.appendLine(line)
            }
            
            channel.disconnect()
            Result.success(output.toString())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun listFiles(serverId: String, path: String): Result<List<FileNode>> = withContext(Dispatchers.IO) {
        try {
            val session = connectionPool[serverId] ?: return@withContext Result.failure(Exception("Not connected"))
            val channel = session.openChannel("sftp") as ChannelSftp
            channel.connect()
            
            val files = mutableListOf<FileNode>()
            val entries = channel.ls(path) as Vector<ChannelSftp.LsEntry>
            
            for (entry in entries) {
                if (entry.filename != "." && entry.filename != "..") {
                    files.add(
                        FileNode(
                            name = entry.filename,
                            path = "$path/${entry.filename}",
                            isDirectory = entry.attrs.isDir,
                            size = if (!entry.attrs.isDir) entry.attrs.size else null,
                            lastModified = entry.attrs.mTime * 1000L
                        )
                    )
                }
            }
            
            channel.disconnect()
            Result.success(files)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun readFile(serverId: String, filePath: String): Result<String> = withContext(Dispatchers.IO) {
        try {
            val session = connectionPool[serverId] ?: return@withContext Result.failure(Exception("Not connected"))
            val channel = session.openChannel("sftp") as ChannelSftp
            channel.connect()
            
            val inputStream = channel.get(filePath)
            val content = inputStream.bufferedReader().use { it.readText() }
            
            channel.disconnect()
            Result.success(content)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    suspend fun writeFile(serverId: String, filePath: String, content: String): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val session = connectionPool[serverId] ?: return@withContext Result.failure(Exception("Not connected"))
            val channel = session.openChannel("sftp") as ChannelSftp
            channel.connect()
            
            channel.put(content.byteInputStream(), filePath)
            
            channel.disconnect()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    fun disconnect(serverId: String) {
        connectionPool[serverId]?.disconnect()
        connectionPool.remove(serverId)
    }
    
    fun disconnectAll() {
        connectionPool.values.forEach { it.disconnect() }
        connectionPool.clear()
    }
}