package com.example.chitchat.models

data class Room(
    val userId:String,
    val friendId:String,
    val message: Message
)