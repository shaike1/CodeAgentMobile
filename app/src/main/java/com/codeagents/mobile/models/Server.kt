package com.codeagents.mobile.models

import java.util.Date
import java.util.UUID

data class Server(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val host: String,
    val port: Int = 22,
    val username: String,
    val authMethodType: String = "password", // "password" or "key"
    val createdAt: Date = Date(),
    val lastConnected: Date? = null
)