package com.example.chitchat.ui.screens.chatscreens.commons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.chitchat.ui.screens.chatscreens.ChattingViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ChatMessages(
    modifier: Modifier,
    currentUserId: String = Firebase.auth.currentUser?.uid!!,
    friendId: String,
    chattingViewModel: ChattingViewModel,
) {

    val listMessage by chattingViewModel.getAllMessages(currentUserId, friendId)
        .collectAsStateWithLifecycle()


    LazyColumn(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        reverseLayout = true
    ) {
        items(listMessage) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                MessageItem(
                    modifier = Modifier
                        .align(if (it.senderId == currentUserId) Alignment.End else Alignment.Start),
                    isSender = it.senderId == currentUserId,
                    messageText = it.text!!
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
    var numLines by remember { mutableStateOf(1) }

    val cornerPercent = cornerShapePercent(numLines)

    val shape = if (isSender) RoundedCornerShape(cornerPercent).copy(
        topEnd = CornerSize(0), bottomEnd = CornerSize(0)
    )
    else RoundedCornerShape(cornerPercent).copy(
        topStart = CornerSize(0), bottomStart = CornerSize(0)
    )

    val paddingModifier =
        if (isSender) modifier.padding(start = 40.dp) else modifier.padding(end = 40.dp)

    Surface(
        modifier = paddingModifier,
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

private fun cornerShapePercent(numOfLines: Int): Int {
    return when (numOfLines) {
        1 -> 50
        2, 3 -> 40
        else -> 25
    }
}

