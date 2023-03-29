package com.example.chitchat.model

import com.example.chitchat.R

data class User(
    val id: Int,
    val fullName: String,
    val userImage: String,
    val isConnected: Boolean,
    val lastConnected: Long,
)

data class CurrentUser(
    val firstName: String="",
    val lastName: String="",
    val description: String? = null,
    val image: String = "android.resource://com.example.chitchat/${R.drawable.baseline_account_circle_24}"
)