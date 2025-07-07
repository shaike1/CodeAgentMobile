package com.codeagents.mobile.models

data class FileNode(
    val name: String,
    val path: String,
    val isDirectory: Boolean,
    val size: Long? = null,
    val lastModified: Long? = null,
    val children: List<FileNode>? = null
)