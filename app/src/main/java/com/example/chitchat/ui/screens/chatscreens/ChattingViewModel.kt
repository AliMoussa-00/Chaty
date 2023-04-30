package com.example.chitchat.ui.screens.chatscreens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.domain.Response
import com.example.chitchat.domain.firestore.FireStoreRepository
import com.example.chitchat.domain.firestore.SendMessageToFriendResponse
import com.example.chitchat.models.Message
import com.example.chitchat.models.Room
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChattingViewModel @Inject constructor(private val repo: FireStoreRepository) : ViewModel() {

    var sendMessageToFriendResponse by mutableStateOf<SendMessageToFriendResponse>(
        Response.Success(
            false
        )
    )
        private set

    fun sendMessage(room: Room) {
        viewModelScope.launch {
            repo.sendMessage(room)
        }
    }

    fun getAllMessages(userID: String, friendId: String): StateFlow<List<Message>> {
        return repo.getAllMessages(userID, friendId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000L),
                initialValue = emptyList()
            )
    }
}