package com.codeagents.mobile.services

import android.webkit.CookieManager
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class ClaudeWebService {
    private val client = OkHttpClient()
    private val baseUrl = "https://claude.ai"
    private var sessionToken: String? = null
    private var organizationId: String? = null
    
    /**
     * Authenticate using Claude web account
     * This requires the user to log in through WebView
     */
    suspend fun authenticateWithWebAccount(webView: WebView): Result<Boolean> = withContext(Dispatchers.Main) {
        try {
            // Set up WebView for Claude authentication
            webView.settings.javaScriptEnabled = true
            webView.settings.domStorageEnabled = true
            
            webView.webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    
                    // Check if we're on the main Claude page (authenticated)
                    if (url?.contains("claude.ai/chats") == true) {
                        // Extract session information from cookies
                        extractSessionFromCookies()
                    }
                }
            }
            
            // Load Claude login page
            webView.loadUrl("$baseUrl/login")
            
            Result.success(true)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Extract session token and organization ID from browser cookies
     */
    private fun extractSessionFromCookies() {
        val cookieManager = CookieManager.getInstance()
        val cookies = cookieManager.getCookie(baseUrl)
        
        cookies?.split(";")?.forEach { cookie ->
            val parts = cookie.trim().split("=")
            if (parts.size == 2) {
                when (parts[0]) {
                    "sessionKey" -> sessionToken = parts[1]
                    "sessionToken" -> sessionToken = parts[1]
                    // Extract other relevant authentication tokens
                }
            }
        }
    }
    
    /**
     * Get existing conversations from Claude account
     */
    suspend fun getConversations(): Result<List<ClaudeConversation>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/api/organizations/$organizationId/chat_conversations")
                .addHeader("Cookie", getCookieHeader())
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36")
                .build()
            
            val response = client.newCall(request).execute()
            
            if (!response.isSuccessful) {
                return@withContext Result.failure(IOException("Failed to fetch conversations"))
            }
            
            val responseBody = response.body?.string()
            val conversations = parseConversations(responseBody ?: "")
            
            Result.success(conversations)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Send message to existing conversation or create new one
     */
    suspend fun sendMessage(
        conversationId: String?,
        message: String,
        files: List<String>? = null
    ): Result<String> = withContext(Dispatchers.IO) {
        try {
            val endpoint = if (conversationId != null) {
                "$baseUrl/api/organizations/$organizationId/chat_conversations/$conversationId/completion"
            } else {
                "$baseUrl/api/organizations/$organizationId/chat_conversations"
            }
            
            val requestBody = JSONObject().apply {
                put("prompt", message)
                put("timezone", "UTC")
                put("model", "claude-3-sonnet-20240229")
                
                files?.let { fileList ->
                    put("attachments", JSONArray(fileList))
                }
            }
            
            val request = Request.Builder()
                .url(endpoint)
                .addHeader("Cookie", getCookieHeader())
                .addHeader("Content-Type", "application/json")
                .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10) AppleWebKit/537.36")
                .addHeader("Referer", "$baseUrl/chats")
                .post(requestBody.toString().toRequestBody("application/json".toMediaType()))
                .build()
            
            val response = client.newCall(request).execute()
            
            if (!response.isSuccessful) {
                return@withContext Result.failure(IOException("Failed to send message"))
            }
            
            // Parse streaming response from Claude
            val responseText = parseClaudeResponse(response.body?.string() ?: "")
            Result.success(responseText)
            
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    /**
     * Resume existing conversation by ID
     */
    suspend fun resumeConversation(conversationId: String): Result<List<ClaudeMessage>> = withContext(Dispatchers.IO) {
        try {
            val request = Request.Builder()
                .url("$baseUrl/api/organizations/$organizationId/chat_conversations/$conversationId")
                .addHeader("Cookie", getCookieHeader())
                .build()
            
            val response = client.newCall(request).execute()
            val messages = parseConversationMessages(response.body?.string() ?: "")
            
            Result.success(messages)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    private fun getCookieHeader(): String {
        val cookieManager = CookieManager.getInstance()
        return cookieManager.getCookie(baseUrl) ?: ""
    }
    
    private fun parseConversations(json: String): List<ClaudeConversation> {
        // Parse JSON response to extract conversation list
        val conversations = mutableListOf<ClaudeConversation>()
        try {
            val jsonObj = JSONObject(json)
            val conversationArray = jsonObj.optJSONArray("conversations")
            
            conversationArray?.let { array ->
                for (i in 0 until array.length()) {
                    val conv = array.getJSONObject(i)
                    conversations.add(
                        ClaudeConversation(
                            id = conv.getString("uuid"),
                            name = conv.getString("name"),
                            updatedAt = conv.getString("updated_at"),
                            model = conv.optString("model", "claude-3-sonnet")
                        )
                    )
                }
            }
        } catch (e: Exception) {
            // Handle parsing errors
        }
        
        return conversations
    }
    
    private fun parseConversationMessages(json: String): List<ClaudeMessage> {
        // Parse conversation messages
        return emptyList() // Implement based on Claude's response format
    }
    
    private fun parseClaudeResponse(response: String): String {
        // Parse Claude's streaming response format
        return response // Implement proper parsing
    }
}

data class ClaudeConversation(
    val id: String,
    val name: String,
    val updatedAt: String,
    val model: String
)

data class ClaudeMessage(
    val id: String,
    val text: String,
    val sender: String, // "human" or "assistant"
    val createdAt: String
)