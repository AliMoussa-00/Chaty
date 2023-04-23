package com.example.chitchat.ui.screens.commons

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.chitchat.core.DEFAULT_USER_IMAGE
import com.example.chitchat.models.User

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar(
    topAppBarTitle: String,
    canGoBack: Boolean = true,
    onClickBack: () -> Unit = {},
    canSignOut: Boolean = false,
    onClickSignOut: () -> Unit = {},
) {
    TopAppBar(
        title = { Text(text = topAppBarTitle) },
        navigationIcon = {
            if (canGoBack) {
                IconButton(onClick = onClickBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        },
        actions = {
            if (canSignOut) {
                IconButton(onClick = onClickSignOut) {
                    Icon(imageVector = Icons.Default.Logout, contentDescription = null)
                }
            }
        }

    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OneToOneChatTopAppBar(
    modifier: Modifier = Modifier,
    friendData: User,
    onClickFriendImage: () -> Unit = {},
    onClickBack: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = friendData.fullName,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        },
        navigationIcon = {
            IconButton(onClick = onClickBack) {
                Icon(
                    modifier = Modifier.size(18.dp),
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null
                )
            }
        },
        actions = {
            IconButton(modifier = Modifier.padding(8.dp), onClick = { onClickFriendImage() }) {

                Box(modifier = Modifier.size(38.dp)) {
                    AsyncImage(
                        model = friendData.userImage ?: DEFAULT_USER_IMAGE,
                        contentDescription = null,
                        modifier = modifier
                            .size(38.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    if (friendData.isConnected) {
                        Spacer(
                            modifier = Modifier
                                .align(Alignment.TopEnd)
                                .background(color = Color.Green, shape = CircleShape)
                                .border(width = 1.dp, color = Color.White, shape = CircleShape)
                                .size(12.dp)
                        )
                    }
                }
            }
        }
    )
}