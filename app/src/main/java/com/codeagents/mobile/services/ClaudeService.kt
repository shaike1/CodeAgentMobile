package com.codeagents.mobile.services

import com.codeagents.mobile.models.Message
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ClaudeService(private val apiKey: String) {
    private val client = OkHttpClient()
    private val baseUrl = "https://api.anthropic.com/v1"
    
    suspend fun sendMessage(
        messages: List<Message>,
        systemPrompt: String? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val jsonMessages = JSONArray()
            messages.forEach { message ->
                jsonMessages.put(
                    JSONObject().apply {
                        put("role", message.role)
                        put("content", message.content)
                    }
                )
            }
            
            val requestBody = JSONObject().apply {
                put("model", "claude-3-sonnet-20240229")
                put("max_tokens", 4000)
                put("messages", jsonMessages)
                if (systemPrompt != null) {
                    put("system", systemPrompt)
                }
            }
            
            val request = Request.Builder()
                .url("$baseUrl/messages")
                .addHeader("Content-Type", "application/json")
                .addHeader("x-api-key", apiKey)
                .addHeader("anthropic-version", "2023-06-01")
                .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
                .build()
            
            val response = client.newCall(request).execute()
            
            if (!response.isSuccessful) {
                return@withContext Result.failure(IOException("Unexpected code $response"))
            }
            
            val responseBody = response.body?.string()
            val jsonResponse = JSONObject(responseBody ?: "")
            val content = jsonResponse.getJSONArray("content")
                .getJSONObject(0)
                .getString("text")
            
            Result.success(content)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}