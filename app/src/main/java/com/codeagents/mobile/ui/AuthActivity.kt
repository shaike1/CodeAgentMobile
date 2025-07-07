package com.codeagents.mobile.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.webkit.WebView
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.codeagents.mobile.R
import com.codeagents.mobile.services.ClaudeWebService
import kotlinx.coroutines.launch

class AuthActivity : AppCompatActivity() {
    
    private lateinit var webView: WebView
    private lateinit var loginButton: Button
    private val claudeWebService = ClaudeWebService()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        
        webView = findViewById(R.id.auth_webview)
        loginButton = findViewById(R.id.btn_login)
        
        loginButton.setOnClickListener {
            authenticateWithClaude()
        }
    }
    
    private fun authenticateWithClaude() {
        lifecycleScope.launch {
            claudeWebService.authenticateWithWebAccount(webView)
                .onSuccess {
                    Toast.makeText(this@AuthActivity, "Authentication successful!", Toast.LENGTH_SHORT).show()
                    
                    // Return to main activity
                    setResult(Activity.RESULT_OK)
                    finish()
                }
                .onFailure { exception ->
                    Toast.makeText(this@AuthActivity, "Authentication failed: ${exception.message}", Toast.LENGTH_LONG).show()
                }
        }
    }
}