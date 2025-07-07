package com.codeagents.mobile.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TerminalScreen() {
    var command by remember { mutableStateOf("") }
    var terminalHistory by remember { mutableStateOf(listOf<String>()) }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // Terminal output
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .padding(8.dp)
        ) {
            items(terminalHistory) { line ->
                Text(
                    text = line,
                    color = Color.Green,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        
        // Command input
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "$ ",
                color = Color.Green,
                fontFamily = FontFamily.Monospace
            )
            
            OutlinedTextField(
                value = command,
                onValueChange = { command = it },
                placeholder = { 
                    Text(
                        "Enter command...",
                        color = Color.Gray,
                        fontFamily = FontFamily.Monospace
                    )
                },
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(
                    onDone = {
                        if (command.isNotBlank()) {
                            terminalHistory = terminalHistory + "$ $command"
                            // TODO: Execute command via SSH
                            terminalHistory = terminalHistory + "Command executed: $command"
                            command = ""
                        }
                    }
                ),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = Color.Green,
                    unfocusedTextColor = Color.Green,
                    focusedBorderColor = Color.Green,
                    unfocusedBorderColor = Color.Gray
                )
            )
        }
    }
}