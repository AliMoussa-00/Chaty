package com.example.chitchat.model

data class User(
    val id:Int,
    val fullName:String,
    val userImage:String,
    val isConnected:Boolean,
    val lastConnected:Long
)