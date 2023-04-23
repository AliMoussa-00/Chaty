package com.example.chitchat.models


data class User(
    val id: String,
    val fullName: String,
    val description: String? = null,
    val userImage: String? =null,
    val isConnected: Boolean = true,
) {
   public constructor(): this("","",null,"",true)
}
