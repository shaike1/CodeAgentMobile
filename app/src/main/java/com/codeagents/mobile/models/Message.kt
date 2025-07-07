package com.codeagents.mobile.models

import java.util.Date
import java.util.UUID

data class Message(
    val id: String = UUID.randomUUID().toString(),
    val content: String,
    val role: String, // "user" or "assistant"
    val timestamp: Date = Date(),
    val projectId: String
)

data class MessageContent(
    val type: String, // "text", "tool_use", "tool_result"
    val text: String? = null,
    val toolName: String? = null,
    val toolInput: String? = null,
    val toolResult: String? = null
)