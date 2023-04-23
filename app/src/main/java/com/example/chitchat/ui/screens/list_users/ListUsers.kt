package com.example.chitchat.ui.screens.list_users

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.chitchat.core.DEFAULT_USER_IMAGE
import com.example.chitchat.domain.Response
import com.example.chitchat.models.ScreenType
import com.example.chitchat.models.User
import com.example.chitchat.ui.screens.ChatViewModel
import com.example.chitchat.ui.screens.commons.ChatTopAppBar
import com.example.chitchat.ui.screens.commons.ProgressBar
import com.example.chitchat.ui.screens.profile.ProfileViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun ListUsersScreen(
    chatViewModel: ChatViewModel,
    profileViewModel: ProfileViewModel = hiltViewModel(),
    listUsersViewModel: ListUsersViewModel = hiltViewModel(),
) {
    val usersList by listUsersViewModel.listUsers.collectAsState()

    Column {
        ChatTopAppBar(
            topAppBarTitle = "Chatty",
            canGoBack = false,
            canSignOut = true,
            onClickSignOut = {profileViewModel.signOut()}
        )

        ListUsersScreenContents(
            list = usersList.filter { it.id != Firebase.auth.currentUser?.uid!! },
            onClickUser = {
                chatViewModel.settingFriendUserData(it)
                chatViewModel.setScreenType(ScreenType.OneToOneChat)
            }
        )

        SignOutResponse(profileViewModel = profileViewModel, chatViewModel = chatViewModel)

    }
}

@Composable
fun ListUsersScreenContents(
    list: List<User>,
    onClickUser: (User) -> Unit
) {
    Column(Modifier.padding(8.dp)) {
        LazyColumn {
            items(list) {
                ListItem(user = it, onClickUser = { onClickUser(it) })
            }
        }
    }
}

@Composable
fun ListItem(modifier: Modifier = Modifier, user: User,onClickUser: () -> Unit) {

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
        ListItemContents(modifier = Modifier.weight(1f), user = user, onClickUser = onClickUser)
    }
}

@Composable
fun ListItemImage(
    modifier: Modifier = Modifier,
    userImage: String?,
    userName: String,
    isConnected: Boolean,
) {

    Box(Modifier.size(48.dp)) {
        AsyncImage(
            model = userImage ?: DEFAULT_USER_IMAGE,
            contentDescription = userName,
            modifier = modifier
                .size(48.dp)
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
fun ListItemContents(modifier: Modifier, user: User,onClickUser:()->Unit) {

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickUser() },
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

@Composable
private fun SignOutResponse(profileViewModel: ProfileViewModel,chatViewModel: ChatViewModel){
    when(val signOutResponse = profileViewModel.signOutResponse){
        is Response.Loading ->{
            ProgressBar()
        }

        is Response.Success->{
            LaunchedEffect(key1 = signOutResponse, block = {
                if(signOutResponse.data!!){
                    profileViewModel.setSignOutResponseFalse()
                    chatViewModel.setScreenType(ScreenType.ChooseLogin)
                }
            })
        }

        is Response.Failure->{
            Log.e("TAG","SignOutResponse FAILED : ${signOutResponse.e}")
        }
    }
}