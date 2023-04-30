package com.example.chitchat.models

data class Message(
    val senderId: String = "",
    val text: String? = null,
    val timeSent:String=""
){
    public constructor(): this("","","")
}