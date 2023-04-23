package com.example.chitchat.models

data class Message(
    val messageId: String = "",
    val senderId: String = "",
    val receiversIds: List<String> = emptyList(),
    val timeSent: String = "",
    val text: String = "",
    val image: String = "",
    val isSent: Boolean = false,
    val isRead: Boolean = false,
)