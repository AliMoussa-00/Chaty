package com.example.chitchat.ui.screens.chatscreens.commons

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.chitchat.models.Message
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ChatMessages(
    modifier: Modifier,
    currentUserId: String = Firebase.auth.currentUser?.uid!!,
    messages: List<Message>,
) {

    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        items(messages) {
            Column(
                modifier= Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ){
                MessageItem(
                    modifier=Modifier
                        .align(if(it.senderId == currentUserId)Alignment.End else Alignment.Start),
                    isSender = it.senderId == currentUserId,
                    messageText = it.text
                )
            }
        }

    }
}

@Composable
private fun MessageItem(
    modifier: Modifier,
    isSender: Boolean,
    messageText: String,
) {

    //get the screen size

    val width= getScreenSize(LocalContext.current).toFloat() * 0.45f


    var numLines by remember { mutableStateOf(1) }

    val cornerPercent = cornerShapePercent(numLines)

    val shape = if(isSender)RoundedCornerShape(cornerPercent).copy(
        topEnd = CornerSize(0), bottomEnd = CornerSize(0)
    )
    else RoundedCornerShape(cornerPercent).copy(
        topStart = CornerSize(0), bottomStart = CornerSize(0)
    )


    Surface(
        modifier=modifier.widthIn(max=width.dp),
        color = Color.LightGray,
        shape = shape
    ) {

        Text(
            text = messageText,
            modifier = Modifier.padding(horizontal = 24.dp, vertical = 16.dp),
            onTextLayout = {
                numLines = it.lineCount
            }
        )
    }
}

private fun getScreenSize(context: Context):Int{
    val displayMetrics = DisplayMetrics()
    val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    windowManager.defaultDisplay.getMetrics(displayMetrics)
    return displayMetrics.widthPixels
}

private fun cornerShapePercent(numOfLines:Int):Int{
    return when(numOfLines){
        1-> 50
        2,3-> 40
        else -> 25
    }
}

