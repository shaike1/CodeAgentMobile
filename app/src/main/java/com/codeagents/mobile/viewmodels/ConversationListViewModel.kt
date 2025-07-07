package com.codeagents.mobile.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codeagents.mobile.services.ClaudeConversation
import com.codeagents.mobile.services.ClaudeWebService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConversationListViewModel : ViewModel() {
    
    private val claudeWebService = ClaudeWebService()
    
    private val _conversations = MutableStateFlow<List<ClaudeConversation>>(emptyList())
    val conversations: StateFlow<List<ClaudeConversation>> = _conversations.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()
    
    fun loadConversations() {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null
            
            claudeWebService.getConversations()
                .onSuccess { conversationList ->
                    _conversations.value = conversationList
                }
                .onFailure { exception ->
                    _error.value = exception.message
                }
            
            _isLoading.value = false
        }
    }
    
    fun refreshConversations() {
        loadConversations()
    }
}