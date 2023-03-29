package com.example.chitchat.ui.screens.signup

import android.app.Activity
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.chitchat.R
import com.example.chitchat.model.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.component.getFullPhoneNumber
import java.util.concurrent.TimeUnit

@Composable
fun SignUpPhone(
    firebaseAuth: FirebaseAuth,
    chatViewModel: ChatViewModel,
) {

    SignUpPhoneContents(
        firebaseAuth = firebaseAuth,
        chatViewModel = chatViewModel
    )
}

@Composable
fun SignUpPhoneContents(
    modifier: Modifier = Modifier,
    firebaseAuth: FirebaseAuth,
    chatViewModel: ChatViewModel,
) {
    var phoneNumber by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Text(text = stringResource(id = R.string.continue_phone))

        TogiCountryCodePicker(
            text = phoneNumber,
            onValueChange = { phoneNumber = it },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.weight(1f))

        SignInButton(
            isEnabled = true,
            onClickSignIn = {

                sendVerificationCode(
                    firebaseAuth = firebaseAuth,
                    phoneNumber = getFullPhoneNumber(),
                    activity = context as Activity
                )

                //
                chatViewModel.setScreenType(ScreenType.SignCode)
            }
        )

    }
}


private fun sendVerificationCode(
    firebaseAuth: FirebaseAuth,
    phoneNumber: String,
    activity: Activity,
) {
    val options = PhoneAuthOptions.newBuilder(firebaseAuth)
        .setPhoneNumber(phoneNumber)
        .setTimeout(60L, TimeUnit.SECONDS)
        .setActivity(activity)
        .setCallbacks(callbacks)
        .build()

    PhoneAuthProvider.verifyPhoneNumber(options)
}




