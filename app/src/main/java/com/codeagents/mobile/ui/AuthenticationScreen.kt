package com.codeagents.mobile.ui

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun AuthenticationScreen(
    onAuthSuccess: () -> Unit,
    onLaunchWebAuth: () -> Unit
) {
    var apiKey by remember { mutableStateOf("") }
    var authMethod by remember { mutableStateOf(AuthMethod.WEB_ACCOUNT) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Welcome to CodeAgents Mobile",
            style = MaterialTheme.typography.headlineMedium,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        Text(
            text = "Choose authentication method:",
            style = MaterialTheme.typography.titleMedium
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Authentication method selection
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            FilterChip(
                onClick = { authMethod = AuthMethod.WEB_ACCOUNT },
                label = { Text("Claude Account") },
                selected = authMethod == AuthMethod.WEB_ACCOUNT
            )
            
            FilterChip(
                onClick = { authMethod = AuthMethod.API_KEY },
                label = { Text("API Key") },
                selected = authMethod == AuthMethod.API_KEY
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        when (authMethod) {
            AuthMethod.WEB_ACCOUNT -> {
                WebAccountAuth(onLaunchWebAuth = onLaunchWebAuth)
            }
            
            AuthMethod.API_KEY -> {
                ApiKeyAuth(
                    apiKey = apiKey,
                    onApiKeyChange = { apiKey = it },
                    onAuthSuccess = onAuthSuccess
                )
            }
        }
    }
}

@Composable
fun WebAccountAuth(onLaunchWebAuth: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Sign in with your existing Claude subscription",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "• Access your existing conversations\n• Use your Claude Pro/subscription\n• No additional API costs",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Button(
            onClick = onLaunchWebAuth,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Sign in with Claude Account")
        }
    }
}

@Composable
fun ApiKeyAuth(
    apiKey: String,
    onApiKeyChange: (String) -> Unit,
    onAuthSuccess: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Use Claude API Key",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center
        )
        
        Spacer(modifier = Modifier.height(8.dp))
        
        Text(
            text = "Get your API key from console.anthropic.com",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        OutlinedTextField(
            value = apiKey,
            onValueChange = onApiKeyChange,
            label = { Text("Claude API Key") },
            placeholder = { Text("sk-ant-...") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        Button(
            onClick = {
                if (apiKey.isNotBlank()) {
                    // Save API key and authenticate
                    onAuthSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth(),
            enabled = apiKey.isNotBlank()
        ) {
            Text("Continue with API Key")
        }
    }
}

enum class AuthMethod {
    WEB_ACCOUNT,
    API_KEY
}