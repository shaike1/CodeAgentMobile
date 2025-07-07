package com.codeagents.mobile.ui

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.codeagents.mobile.ui.theme.CodeAgentsMobileTheme

class MainActivity : ComponentActivity() {
    
    private var isAuthenticated by mutableStateOf(false)
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // Check if user is already authenticated
        checkAuthenticationStatus()
        
        setContent {
            CodeAgentsMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    if (isAuthenticated) {
                        MainScreen()
                    } else {
                        AuthenticationScreen(
                            onAuthSuccess = { 
                                isAuthenticated = true 
                            },
                            onLaunchWebAuth = {
                                startAuthActivity()
                            }
                        )
                    }
                }
            }
        }
    }
    
    private fun checkAuthenticationStatus() {
        // Check shared preferences or stored tokens
        val prefs = getSharedPreferences("claude_auth", MODE_PRIVATE)
        isAuthenticated = prefs.getBoolean("is_authenticated", false)
    }
    
    private fun startAuthActivity() {
        val intent = Intent(this, AuthActivity::class.java)
        startActivityForResult(intent, AUTH_REQUEST_CODE)
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        
        if (requestCode == AUTH_REQUEST_CODE && resultCode == RESULT_OK) {
            isAuthenticated = true
            
            // Save authentication status
            val prefs = getSharedPreferences("claude_auth", MODE_PRIVATE)
            prefs.edit().putBoolean("is_authenticated", true).apply()
        }
    }
    
    companion object {
        private const val AUTH_REQUEST_CODE = 1001
    }
}