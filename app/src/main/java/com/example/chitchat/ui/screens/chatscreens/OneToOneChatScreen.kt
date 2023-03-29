package com.example.chitchat.ui.screens.chatscreens

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
import androidx.compose.ui.unit.dp
import com.example.chitchat.R
import com.example.chitchat.ui.theme.ChitChatTheme

@Composable
fun OneToOneChatScreen() {
}

@Composable
fun ChatOneScreenContents(
    modifier: Modifier = Modifier,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        Spacer(modifier = Modifier.weight(1f))
        ChatBottom()
    }
}

@Composable
fun ChatBottom(
    modifier: Modifier = Modifier,
) {
    Row(
        modifier.fillMaxWidth(),
    ) {

        var value by remember { mutableStateOf("") }

        MessageField(
            modifier = Modifier.weight(1f),
            value = value,
            onValueChanged = { value = it },
            onClickSend = {}
        )

        AddOptionsButtons(
            modifier = Modifier.align(Alignment.Bottom)
        )
    }
}

@Composable
fun MessageField(
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
            trailingIcon ={
                Row(
                    modifier = Modifier.background(color=Color.Red)

                ){
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
fun AddOptionsButtons(
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


@Preview(showSystemUi = true, showBackground = true)
@Composable
fun PreviewChatOnToOne() {
    ChitChatTheme {
        ChatOneScreenContents()
    }
}