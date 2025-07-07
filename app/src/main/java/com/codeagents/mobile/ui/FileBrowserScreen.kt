package com.codeagents.mobile.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.InsertDriveFile
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.codeagents.mobile.models.FileNode

@Composable
fun FileBrowserScreen() {
    var files by remember { mutableStateOf(listOf<FileNode>()) }
    var currentPath by remember { mutableStateOf("/") }
    
    // Mock data for now
    LaunchedEffect(Unit) {
        files = listOf(
            FileNode("src", "/src", true),
            FileNode("build.gradle", "/build.gradle", false, 1234, System.currentTimeMillis()),
            FileNode("README.md", "/README.md", false, 567, System.currentTimeMillis())
        )
    }
    
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Current path
        Text(
            text = "Current: $currentPath",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(16.dp)
        )
        
        // File list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(files) { file ->
                FileItem(
                    file = file,
                    onClick = {
                        if (file.isDirectory) {
                            currentPath = file.path
                            // TODO: Load directory contents
                        } else {
                            // TODO: Open file for editing
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun FileItem(
    file: FileNode,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = if (file.isDirectory) Icons.Default.Folder else Icons.Default.InsertDriveFile,
            contentDescription = if (file.isDirectory) "Folder" else "File",
            tint = if (file.isDirectory) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
        )
        
        Spacer(modifier = Modifier.width(12.dp))
        
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = file.name,
                style = MaterialTheme.typography.bodyLarge
            )
            if (!file.isDirectory && file.size != null) {
                Text(
                    text = "${file.size} bytes",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}