package com.example.chitchat.core

import com.example.chitchat.R

//Names
const val SIGN_IN_REQUEST = "signInRequest"
const val SIGN_UP_REQUEST = "signUpRequest"

// Name of collection in cloud fireStore
const val COLLECTION_USERS= "users"
const val COLLECTION_ROOMS= "rooms"
const val COLLECTION_MESSAGES= "messages"

val DEFAULT_USER_IMAGE= "android.resource://com.example.chitchat/${R.drawable.baseline_account_circle_24}"

// fields names of room collection :
const val ROOM_ID = "roomId"
const val USERS_ID= "usersId"
const val SENDER_ID= "senderId"
const val FRIEND_ID= "friendId"
const val MESSAGE_TEXT= "message_text"

// field names of message collection
const val MESSAGE_ID = "messageId"
const val TIME_SENT= "timeSent"
const val TEXT_MESSAGE = "text"
const val IMAGE_MESSAGE = "message_image"

fun getRoomDocPath(userId:String,friendId: String):String{
    return  listOf(userId,friendId).sorted().joinToString("_")
}
fun getMessageDocPath(userId:String,friendId: String):String{
    return "message_from_${userId}_to_${friendId}"
}

fun getMessagesCollectionPath(roomId:String):String{
    return "$COLLECTION_ROOMS/${roomId}/${COLLECTION_MESSAGES}"
}