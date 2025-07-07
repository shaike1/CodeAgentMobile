package com.codeagents.mobile.models

import java.util.Date
import java.util.UUID

data class Project(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val serverId: String,
    val remoteWorkingDirectory: String,
    val isActive: Boolean = false,
    val createdAt: Date = Date(),
    val lastAccessed: Date? = null
)