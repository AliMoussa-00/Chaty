package com.example.chitchat.ui.screens.login.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chitchat.R
import com.example.chitchat.model.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel
import com.example.chitchat.ui.screens.commons.ChattyLogo
import com.example.chitchat.ui.screens.commons.LoginCard

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        LoginFields(
            chatViewModel = chatViewModel,
            onClickSignIn = { chatViewModel.setScreenType(ScreenType.SingChoose) },
        )

    }
}

@Composable
fun LoginFields(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    onClickSignIn: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        ChattyLogo()

        LoginGoogle()

        LoginFacebook()

        LoginTwitter()

        LoginDivider()

        LoginEmail(onClickEmailLogin = {chatViewModel.setScreenType(ScreenType.LoginEmail)})

        Spacer(modifier = Modifier.height(24.dp))
        SignUp(onClickSignUp = onClickSignIn)


    }
}

@Composable
private fun LoginDivider(modifier: Modifier = Modifier) {

    Column {
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Divider(Modifier.weight(1f))
            Text(text = stringResource(id = R.string.or))
            Divider(Modifier.weight(1f))
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Composable
private fun LoginFacebook(
    modifier: Modifier = Modifier,
) {
    LoginCard(
        image = R.drawable.facebook,
        text = R.string.signin_face,
        onClick = {}
    )
}

@Composable
private fun LoginTwitter(
    modifier: Modifier = Modifier,
) {
    LoginCard(
        image = R.drawable.twitter,
        text = R.string.signin_twitter,
        onClick = {}
    )
}

@Composable
private fun LoginEmail(
    modifier: Modifier = Modifier,
    onClickEmailLogin:()->Unit
) {

    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClickEmailLogin() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {

            Icon(
                modifier = Modifier
                    .size(30.dp)
                    .clip(CircleShape),
                imageVector = Icons.Default.Email,
                contentDescription = null,
            )

            Text(
                text = stringResource(id = R.string.signin_email),
                fontSize = 18.sp
            )
        }
    }
}

@Composable
fun SignUp(
    modifier: Modifier = Modifier,
    onClickSignUp: () -> Unit,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Text(text = stringResource(id = R.string.dont_have_account), textAlign = TextAlign.Center)

        TextButton(onClick = { onClickSignUp() }) {
            Text(text = stringResource(id = R.string.signup))
        }
    }
}
