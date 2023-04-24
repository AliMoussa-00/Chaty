package com.example.chitchat.ui.screens.chatscreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chitchat.R
import com.example.chitchat.models.Message
import com.example.chitchat.models.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel
import com.example.chitchat.ui.screens.chatscreens.commons.ChatMessages
import com.example.chitchat.ui.screens.commons.OneToOneChatTopAppBar
import com.example.chitchat.ui.theme.ChitChatTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewChat(){
    ChitChatTheme {
        OneToOneChatScreen()
    }
}

@Composable
fun OneToOneChatScreen(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel = hiltViewModel(),
    currentUserId: String = "",
    friendId: String = "",
) {

    BackHandler {
        chatViewModel.setScreenType(ScreenType.HomeList)
    }

    val friendData = chatViewModel.friendUser


    Column(modifier.fillMaxSize()) {

        OneToOneChatTopAppBar(
            friendData = friendData,
            onClickBack = { chatViewModel.setScreenType(ScreenType.HomeList) }
        )

        OneToOneChatScreenContents(modifier=Modifier.weight(1f))

    }

}

@Composable
private fun OneToOneChatScreenContents(
    modifier: Modifier,
    onMessageDeleted: () -> Unit = {},
) {

    Column(
        modifier = modifier
    ) {

        ChatMessages(modifier = Modifier.weight(1f), messages = messages)
        ChatBottom(
            onMessageSent = {
                messages.add(Message(senderId = Firebase.auth.currentUser?.uid!!, text = it))
            }
        )
    }
}


@Composable
private fun ChatBottom(
    modifier: Modifier = Modifier,
    onMessageSent: (String) -> Unit = {},
) {
    Row(
        modifier.fillMaxWidth(),
    ) {

        var value by remember { mutableStateOf("") }

        MessageField(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChanged = { value = it },
            onClickSend = {
                onMessageSent(value)
                value = ""
            }
        )

        AddOptionsButtons(
            modifier = Modifier.align(Alignment.Bottom)
        )
    }
}

@Composable
private fun MessageField(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    onClickSend: (String) -> Unit,
) {

    Box(
        modifier = modifier
    ) {
        OutlinedTextField(
            value = value,
            onValueChange = { onValueChanged(it) },
            placeholder = { Text(text = stringResource(id = R.string.message)) },
            trailingIcon = {
                Row(
                    modifier = Modifier.background(color = Color.Red)

                ) {
                    IconButton(
                        modifier = Modifier.background(color = Color.Red),
                        onClick = {
                            onClickSend(value)
                        }) {
                        Icon(
                            imageVector = Icons.Outlined.Send,
                            contentDescription = stringResource(id = R.string.send_message)
                        )
                    }
                }
            },
            maxLines = 3,
            shape = MaterialTheme.shapes.small,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun AddOptionsButtons(
    modifier: Modifier = Modifier,
) {
    IconButton(
        modifier = modifier
            .background(color = Color.Cyan, shape = CircleShape),
        onClick = { /*TODO*/ }
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = stringResource(id = R.string.add_options)
        )
    }
}

var messages = mutableStateListOf(
    Message(senderId = "ze", text = "hello Ali"),
    Message(senderId = "ze", text = "how are you doing ?"),
    Message(senderId = "ze", text = "is everything fine?"),
    Message(senderId = Firebase.auth.currentUser?.uid!!, text = "hello john"),
    Message(senderId = Firebase.auth.currentUser?.uid!!, text = "i am fine thanks"),
    Message(senderId = "ze", text = "that is good news"),
    Message(senderId = "ze", text = "hello Ali"),
    Message(senderId = "ze", text = "how are you doing ?"),
    Message(senderId = "ze", text = "is everything fine?"),
    Message(senderId = Firebase.auth.currentUser?.uid!!, text = "hello john"),
    Message(senderId = Firebase.auth.currentUser?.uid!!, text = "i am fine thanks"),
    Message(senderId = "ze", text = "that is good news"),
    Message(senderId = "ze", text = "that is good newsnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"),
    Message(
        senderId = Firebase.auth.currentUser?.uid!!,
        text = "that is good newsnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
    ),
    Message(
        senderId = Firebase.auth.currentUser?.uid!!,
        text = "that is good newsnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
    ),
    Message(
        senderId = "ze",
        text = "that is good newsnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
    ),
    Message(
        senderId = Firebase.auth.currentUser?.uid!!,
        text = "that is good newsnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"
    ),
)
