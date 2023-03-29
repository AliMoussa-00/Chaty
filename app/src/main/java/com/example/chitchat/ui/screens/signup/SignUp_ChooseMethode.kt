package com.example.chitchat.ui.screens.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.chitchat.model.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel

@Composable
fun ChooseSignUpMethode(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
) {

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Please choose the methode you want to sign up with",
            textAlign = TextAlign.Center
        )

        ChoosePhone(onClickPhone = {
            chatViewModel.setScreenType(ScreenType.SingPhone)
        })

        ChooseEmailAndPass(onClickEmail = {
            chatViewModel.setScreenType(ScreenType.SignEmail)
        })
    }
}

@Composable
fun ChoosePhone(
    modifier: Modifier = Modifier,
    onClickPhone: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth(),
        onClick = { onClickPhone() }
    ) {
        Icon(imageVector = Icons.Outlined.Phone, contentDescription = null)
    }
}

@Composable
fun ChooseEmailAndPass(
    modifier: Modifier = Modifier,
    onClickEmail: () -> Unit,
) {
    OutlinedButton(
        modifier = modifier
            .fillMaxWidth(),
        onClick = { onClickEmail() }
    ) {
        Icon(imageVector = Icons.Outlined.Email, contentDescription = null)
    }
}