package com.example.chitchat.ui.screens.chatscreens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chitchat.R
import com.example.chitchat.models.Message
import com.example.chitchat.models.Room
import com.example.chitchat.models.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel
import com.example.chitchat.ui.screens.chatscreens.commons.ChatMessages
import com.example.chitchat.ui.screens.commons.OneToOneChatTopAppBar
import com.example.chitchat.ui.theme.ChitChatTheme
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewChat() {
    ChitChatTheme {

        Column(Modifier.fillMaxSize()) {
            TopAppBar(
                title = { Text(text = "Title") },
                modifier = Modifier
                    .fillMaxWidth(),
            )

            Column(Modifier.weight(1f)) {
                LazyColumn(modifier = Modifier.weight(1f), content = {
                    items(40) {
                        Text(text = "Text : $it", modifier = Modifier.fillMaxWidth())
                    }
                })

                TextField(value = "", onValueChange = {})
            }
        }
    }

}

@Composable
fun OneToOneChatScreen(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    chattingViewModel: ChattingViewModel = hiltViewModel(),
) {

    BackHandler {
        chatViewModel.setScreenType(ScreenType.HomeList)
    }

    val friendData = chatViewModel.friendUser
    val userId = Firebase.auth.currentUser?.uid!!

    Column(modifier.fillMaxSize()) {

        OneToOneChatTopAppBar(
            friendData = friendData,
            onClickBack = { chatViewModel.setScreenType(ScreenType.HomeList) }
        )

        OneToOneChatScreenContents(
            modifier = Modifier.weight(1f),
            chattingViewModel = chattingViewModel,
            friendId = friendData.id,
            onMessageSent = {
                chattingViewModel.sendMessage(
                    Room(
                        userId = userId,
                        friendId = friendData.id,
                        message = Message(userId, text = it)
                    )
                )
            }
        )
    }

}

@Composable
private fun OneToOneChatScreenContents(
    modifier: Modifier,
    chattingViewModel: ChattingViewModel,
    friendId: String,
    onMessageSent: (String) -> Unit,
    onMessageDeleted: () -> Unit = {},
) {

    Column(
        modifier = modifier
    ) {
        ChatMessages(
            modifier = Modifier.weight(1f),
            friendId = friendId,
            chattingViewModel = chattingViewModel
        )
        ChatBottom(
            onMessageSent = { onMessageSent(it) }
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

