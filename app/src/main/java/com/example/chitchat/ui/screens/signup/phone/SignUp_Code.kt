package com.example.chitchat.ui.screens.signup.phone

import android.annotation.SuppressLint
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chitchat.R
import com.example.chitchat.domain.Response
import com.example.chitchat.models.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel
import com.example.chitchat.ui.screens.commons.ChatTopAppBar
import com.example.chitchat.ui.screens.commons.ProgressBar
import com.example.chitchat.ui.screens.signup.email.SignInButton


@Composable
fun SignUpCode(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    phoneAuthViewModel: PhoneAuthViewModel = hiltViewModel(),
) {
    BackHandler {}

    Column(modifier.fillMaxSize()) {
        ChatTopAppBar(
            topAppBarTitle = stringResource(id = R.string.signup_code),
            canGoBack = false,
            onClickBack = {}
        )

        SignUpCodeContents(
            onClickSignIn = {
                phoneAuthViewModel.signInWithPhoneCredential(it)
            },
            onClickResend = {
                phoneAuthViewModel.resendVerificationCode()
            }
        )

        SignInPHoneResponse(chatViewModel = chatViewModel, phoneAuthViewModel = phoneAuthViewModel)

        ResendTokenResponse(phoneAuthViewModel = phoneAuthViewModel)
    }

}


@Composable
fun SignUpCodeContents(
    modifier: Modifier = Modifier,
    onClickSignIn: (String) -> Unit,
    onClickResend: () -> Unit,
) {
    val codeDigits = rememberSaveable { MutableList(6) { mutableStateOf("") } }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Text(text = stringResource(id = R.string.enter_code))

        OtpFields(codeDigits = codeDigits)

        SignInButton(
            isEnabled = codeDigits.size == 6,
            onClickSignIn = {
                Log.e("TAG", "CODE = ${codeDigits.joinToString(separator = "") { it.value }}")
                onClickSignIn(codeDigits.joinToString(separator = "") { it.value })
            }
        )
        ResendCode(onClickResend = { onClickResend() })
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun OtpFields(
    modifier: Modifier = Modifier,
    codeDigits: MutableList<MutableState<String>>,
) {

    val focusManager = LocalFocusManager.current


    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        OtpChar(
            value = codeDigits[0].value,
            onValueChanged = { codeDigits[0].value = it },
            focusManager = focusManager,
            isFirst = true
        )

        OtpChar(
            value = codeDigits[1].value,
            onValueChanged = { codeDigits[1].value = it },
            focusManager = focusManager
        )

        OtpChar(
            value = codeDigits[2].value,
            onValueChanged = { codeDigits[2].value = it },
            focusManager = focusManager
        )

        OtpChar(
            value = codeDigits[3].value,
            onValueChanged = { codeDigits[3].value = it },
            focusManager = focusManager
        )

        OtpChar(
            value = codeDigits[4].value,
            onValueChanged = { codeDigits[4].value = it },
            focusManager = focusManager
        )
        OtpChar(
            value = codeDigits[5].value,
            onValueChanged = { codeDigits[5].value = it },
            focusManager = focusManager, isLast = true
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun OtpChar(
    modifier: Modifier = Modifier,
    value: String,
    onValueChanged: (String) -> Unit,
    focusManager: FocusManager,
    isLast: Boolean = false,
    isFirst: Boolean = false,
) {

    OutlinedTextField(
        value = value,
        onValueChange = {
            if (it.isNotEmpty() && it.length == 1) {
                onValueChanged(it)
                if (!isLast) {
                    focusManager.moveFocus(FocusDirection.Next)
                }
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Number,
            imeAction = if (!isLast) ImeAction.Next else ImeAction.Done
        ),
        keyboardActions = KeyboardActions(onDone = {}),
        textStyle = LocalTextStyle.current.copy(
            textAlign = TextAlign.Center,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp
        ),
        maxLines = 1,
        singleLine = true,
        modifier = modifier
            .size(46.dp)
            .onKeyEvent {
                if (it.key == Key.Backspace) {
                    onValueChanged("")
                    if (!isFirst) {
                        focusManager.moveFocus(FocusDirection.Previous)
                    }
                    true
                } else {
                    false
                }
            },
    )
}

@Composable
private fun ResendCode(modifier: Modifier = Modifier, onClickResend: () -> Unit) {
    TextButton(onClick = { onClickResend() }) {
        Text(text = "resend the code !")
    }
}

@Composable
private fun ResendTokenResponse(
    phoneAuthViewModel: PhoneAuthViewModel,
) {
    val context = LocalContext.current

    when (val resendVerificationCodeResponse = phoneAuthViewModel.resendVerificationCodeResponse) {
        is Response.Loading -> ProgressBar()

        is Response.Success -> {
            LaunchedEffect(key1 = resendVerificationCodeResponse, block = {
                if (resendVerificationCodeResponse.data!!) {
                    Toast.makeText(context, "we have sent you the new code", Toast.LENGTH_LONG)
                        .show()

                    phoneAuthViewModel.resetResendVerificationCodeResponse()
                }
            })
        }

        is Response.Failure -> {
            Toast.makeText(context, "Something is wrong", Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
private fun SignInPHoneResponse(
    chatViewModel: ChatViewModel,
    phoneAuthViewModel: PhoneAuthViewModel,
) {
    val context = LocalContext.current

    when (val signInWithPhoneCredentialResponse =
        phoneAuthViewModel.signInWithPhoneCredentialResponse) {
        is Response.Loading -> ProgressBar()

        is Response.Success -> {
            LaunchedEffect(key1 = signInWithPhoneCredentialResponse, block = {
                if (signInWithPhoneCredentialResponse.data!!) {
                    chatViewModel.setScreenType(ScreenType.SignProfile)
                    phoneAuthViewModel.resetSignInWithPhoneCredentialResponse()
                }
            })
        }

        is Response.Failure -> {
            Toast.makeText(context, "Something is wrong", Toast.LENGTH_LONG).show()
        }
    }
}



