package com.example.chitchat.ui.screens.list_users

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.chitchat.R
import com.example.chitchat.model.User

@Composable
fun ListUsersScreen() {
    ListUsersScreenContents(list = localList)
}

@Composable
fun ListUsersScreenContents(
    list: List<User>,
) {
    Column(Modifier.padding(8.dp)) {
        LazyColumn {
            items(list) {
                ListItem(user = it)
            }
        }
    }
}

@Composable
fun ListItem(modifier: Modifier = Modifier, user: User) {

    Row(
        modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        ListItemImage(
            userImage = user.userImage,
            userName = user.fullName,
            isConnected = user.isConnected
        )
        ListItemContents(modifier = Modifier.weight(1f), user = user)
    }
}

@Composable
fun ListItemImage(
    modifier: Modifier = Modifier,
    userImage: String,
    userName: String,
    isConnected: Boolean,
) {

    Box(Modifier.size(44.dp)) {
        AsyncImage(
            model = R.drawable.facebook,
            contentDescription = userName,
            modifier = modifier
                .size(44.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        if (isConnected) {
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

@Composable
fun ListItemContents(modifier: Modifier, user: User) {

    Column(
        modifier =  modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        Row(
             modifier=Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = user.fullName, fontWeight = FontWeight.Bold)

            Text(text = if (!user.isConnected) "Yesterday" else "")
        }
        Text(text = "Last Message",  fontSize = 12.sp)
    }
}

val localList = listOf(
    User(
        id = 0,
        fullName = "Ali Moussa",
        userImage = "https://picsum.photos/id/237/48",
        isConnected = false,
        lastConnected = 12123L
    ),
    User(
        id = 0,
        fullName = "Ali Moussa",
        userImage = "https://picsum.photos/id/237/48",
        isConnected = true,
        lastConnected = 12123L
    ),
    User(
        id = 0,
        fullName = "Ali Moussa",
        userImage = "https://picsum.photos/id/237/48",
        isConnected = false,
        lastConnected = 12123L
    ),
    User(
        id = 0,
        fullName = "Ali Moussa",
        userImage = "https://picsum.photos/id/237/48",
        isConnected = true,
        lastConnected = 12123L
    ),
    User(
        id = 0,
        fullName = "Ali Moussa",
        userImage = "https://picsum.photos/id/237/48",
        isConnected = false,
        lastConnected = 12123L
    ),
    User(
        id = 0,
        fullName = "Ali Moussa",
        userImage = "https://picsum.photos/id/237/48",
        isConnected = false,
        lastConnected = 12123L
    ),
    User(
        id = 0,
        fullName = "Ali Moussa",
        userImage = "https://picsum.photos/id/237/48",
        isConnected = false,
        lastConnected = 12123L
    ),
    User(
        id = 0,
        fullName = "Ali Moussa",
        userImage = "https://picsum.photos/id/237/48",
        isConnected = false,
        lastConnected = 12123L
    ),
    User(
        id = 0,
        fullName = "Ali Moussa",
        userImage = "https://picsum.photos/id/237/48",
        isConnected = false,
        lastConnected = 12123L
    ),
    User(
        id = 0,
        fullName = "Ali Moussa",
        userImage = "https://picsum.photos/id/237/48",
        isConnected = false,
        lastConnected = 12123L
    ),
    User(
        id = 0,
        fullName = "Ali Moussa",
        userImage = "https://picsum.photos/id/237/48",
        isConnected = false,
        lastConnected = 12123L
    ),
    User(
        id = 0,
        fullName = "Ali Moussa",
        userImage = "https://picsum.photos/id/237/48",
        isConnected = false,
        lastConnected = 12123L
    ),
    User(
        id = 0,
        fullName = "Ali Moussa",
        userImage = "https://picsum.photos/id/237/48",
        isConnected = false,
        lastConnected = 12123L
    ),
    User(
        id = 0,
        fullName = "Ali Moussa",
        userImage = "https://picsum.photos/id/237/48",
        isConnected = false,
        lastConnected = 12123L
    ),


    )