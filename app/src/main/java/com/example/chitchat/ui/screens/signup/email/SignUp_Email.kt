package com.example.chitchat.ui.screens.signup.email

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chitchat.R
import com.example.chitchat.domain.Response
import com.example.chitchat.model.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel
import com.example.chitchat.ui.screens.commons.ChatTopAppBar
import com.example.chitchat.ui.screens.commons.ProgressBar

@Composable
fun SignUpEmail(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    emailPassAuthViewModel: EmailPassAuthViewModel = hiltViewModel(),
) {
    BackHandler {
        chatViewModel.setScreenType(ScreenType.SingChoose)
    }

    var email by rememberSaveable {
        mutableStateOf("")
    }

    var passwordValue by rememberSaveable {
        mutableStateOf("")
    }
    var passwordValueConfirm by rememberSaveable {
        mutableStateOf("")
    }


    Column(modifier.fillMaxSize()) {
        ChatTopAppBar(
            topAppBarTitle = stringResource(id = R.string.signup_email),
            onClickBack = {chatViewModel.setScreenType(ScreenType.SingChoose)}
        )

        SingContents(
            chatViewModel = chatViewModel,
            emailPassAuthViewModel = emailPassAuthViewModel,
            emailValue = email,
            emailValueChanged = { email = it },
            passWordValue = passwordValue,
            passwordValueChanged = { passwordValue = it },
            confirmPassWordValue = passwordValueConfirm,
            confirmPasswordValueChanged = { passwordValueConfirm = it },

            onClickSignIn = {
                emailPassAuthViewModel.signUpWithEmailAndPassword(
                    email = email,
                    password = passwordValue
                )
            }
        )
    }

}

@Composable
fun SingContents(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    emailPassAuthViewModel: EmailPassAuthViewModel,
    emailValue: String,
    emailValueChanged: (String) -> Unit,
    passWordValue: String,
    passwordValueChanged: (String) -> Unit,
    confirmPassWordValue: String,
    confirmPasswordValueChanged: (String) -> Unit,
    onClickSignIn: () -> Unit,
) {
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Email(emailValue = emailValue, emailValueChanged = emailValueChanged)

        Password(
            passWordValue = passWordValue,
            passwordValueChanged = passwordValueChanged
        )

        ConfirmPassword(
            confirmPassWordValue = confirmPassWordValue,
            confirmPasswordValueChanged = confirmPasswordValueChanged,
            passWordValue = passWordValue
        )

        SignInButton(
            onClickSignIn = { onClickSignIn() },
            isEnabled = passWordValue == confirmPassWordValue && passWordValue.length >= 6
        )

        SignUpResponse(
            emailPassAuthViewModel = emailPassAuthViewModel,
            chatViewModel = chatViewModel,
            sendEmailVerification = {
                emailPassAuthViewModel.sendVerificationEmail()
            },
            showVerifyEmail = {
                Toast.makeText(
                    context,
                    "Please check your emails for verification",
                    Toast.LENGTH_LONG
                ).show()
            }
        )

        SendEmailVerification(emailPassAuthViewModel = emailPassAuthViewModel)

    }
}

@Composable
fun Email(
    modifier: Modifier = Modifier,
    emailValue: String,
    emailValueChanged: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = emailValue,
        onValueChange = { emailValueChanged(it) },
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.email)) },

        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        ),
        trailingIcon = {
            IconButton(onClick = { emailValueChanged("") }) {
                if (emailValue.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(id = R.string.clear_field)
                    )
                }
            }
        }
    )
}

@Composable
fun Password(
    modifier: Modifier = Modifier,
    passWordValue: String,
    passwordValueChanged: (String) -> Unit,
) {
    var showPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = passWordValue,
        onValueChange = { passwordValueChanged(it) },
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.passeword)) },
        supportingText = {
            if (passWordValue.length < 6) {
                Text(
                    text = stringResource(id = R.string.weak_pass),
                    color = MaterialTheme.colorScheme.error
                )
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onDone = null),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                if (!showPassword) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = stringResource(id = R.string.show_pass)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = stringResource(id = R.string.hide_pass)
                    )
                }
            }
        }
    )
}

@Composable
private fun ConfirmPassword(
    modifier: Modifier = Modifier,
    confirmPassWordValue: String,
    confirmPasswordValueChanged: (String) -> Unit,
    passWordValue: String,
) {
    var showPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = confirmPassWordValue,
        onValueChange = { confirmPasswordValueChanged(it) },
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.confirm_password)) },
        isError = passWordValue != confirmPassWordValue,
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = null),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { showPassword = !showPassword }) {
                if (!showPassword) {
                    Icon(
                        imageVector = Icons.Filled.Visibility,
                        contentDescription = stringResource(id = R.string.show_pass)
                    )
                } else {
                    Icon(
                        imageVector = Icons.Filled.VisibilityOff,
                        contentDescription = stringResource(id = R.string.hide_pass)
                    )
                }
            }
        }
    )
}

@Composable
fun SignInButton(
    onClickSignIn: () -> Unit,
    isEnabled: Boolean,
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        enabled = isEnabled,
        onClick = { onClickSignIn() }
    ) {
        Text(text = stringResource(id = R.string.signup))
    }
}

@Composable
private fun SignUpResponse(
    emailPassAuthViewModel: EmailPassAuthViewModel,
    chatViewModel: ChatViewModel,
    sendEmailVerification: () -> Unit,
    showVerifyEmail: () -> Unit,
) {
    when (val signUpEmailPassResponse = emailPassAuthViewModel.signUpResponse) {
        is Response.Loading -> ProgressBar()

        is Response.Success -> {
            val isUserSignedUp = signUpEmailPassResponse.data!!
            LaunchedEffect(key1 = signUpEmailPassResponse, block = {
                if (isUserSignedUp) {
                    sendEmailVerification()
                    showVerifyEmail()
                    chatViewModel.setScreenType(ScreenType.SignProfile)
                }
            })
        }

        is Response.Failure -> {
            Log.e("TAG", "SIGN UP EMAIL Response Failed : ${signUpEmailPassResponse.e}")
        }
    }
}

@Composable
private fun SendEmailVerification(emailPassAuthViewModel: EmailPassAuthViewModel) {

    when (val sendEmailResponse = emailPassAuthViewModel.sendVerificationEmailResponse) {
        is Response.Loading -> ProgressBar()

        is Response.Success -> Unit

        is Response.Failure -> {
            Log.e("TAG", "SEND VERIFICATION EMAIL Response Failed : ${sendEmailResponse.e}")
        }
    }
}


