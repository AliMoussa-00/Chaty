package com.example.chitchat.ui.screens.login.components

import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chitchat.R
import com.example.chitchat.domain.Response
import com.example.chitchat.models.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel
import com.example.chitchat.ui.screens.commons.ChatTopAppBar
import com.example.chitchat.ui.screens.commons.ChattyLogo
import com.example.chitchat.ui.screens.commons.ProgressBar
import com.example.chitchat.ui.screens.signup.email.EmailPassAuthViewModel

@Composable
fun LoginEmailScreen(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
){
    BackHandler {
        chatViewModel.setScreenType(ScreenType.ChooseLogin)
    }

    Column(
        modifier = modifier.fillMaxSize().padding(16.dp)
    ) {
        ChatTopAppBar(
            topAppBarTitle = "Back",
            onClickBack = {chatViewModel.setScreenType(ScreenType.ChooseLogin)}
        )

        ChattyLogo()

        LoginEmailScreenContent(chatViewModel = chatViewModel)
    }
}

@Composable
private fun LoginEmailScreenContent(
    emailPassAuthViewModel: EmailPassAuthViewModel = hiltViewModel(),
    chatViewModel: ChatViewModel,
){

    var userNameValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }

    var loginClicked by remember{ mutableStateOf(false) }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        UserNAmeField(userNameValue = userNameValue, userNameValueChanged = {userNameValue = it})

        PassWordField(passWordValue = passwordValue, passwordValueChanged = {passwordValue = it})

        ForgetPassWord (onClickForget ={
            emailPassAuthViewModel.resetPassword(email = userNameValue.trim())
        } )

        LoginButton(onClickLogin = {
            loginClicked = true
            emailPassAuthViewModel.loginWithEmailPass(userNameValue.trim(),passwordValue.trim())
        })

        EmailPassLoginResponse(emailPassAuthViewModel = emailPassAuthViewModel) {
            if(loginClicked)
                chatViewModel.setScreenType(ScreenType.HomeList)
        }

        ResetPasswordResponse(emailPassAuthViewModel = emailPassAuthViewModel)
    }
}

@Composable
private fun UserNAmeField(
    modifier: Modifier = Modifier,
    userNameValue: String,
    userNameValueChanged: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = userNameValue,
        onValueChange = { userNameValueChanged(it) },
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.user_name)) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        trailingIcon = {
            IconButton(onClick = { userNameValueChanged("") }) {
                if (userNameValue.isNotEmpty()) {
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
private fun PassWordField(
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
private fun ForgetPassWord(onClickForget: () -> Unit) {
    TextButton(
        onClick = { onClickForget() }
    ) {
        Text(text = stringResource(id = R.string.forget_password))
    }
}

@Composable
private fun LoginButton(modifier: Modifier = Modifier, onClickLogin: () -> Unit) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(50)
    ) {
        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = { onClickLogin() }
        ) {
            Text(text = stringResource(id = R.string.login))
        }
    }
}

@Composable
private fun EmailPassLoginResponse(
    emailPassAuthViewModel: EmailPassAuthViewModel,
    goToHomeScreen:()->Unit
){
    val context = LocalContext.current

    when(val emailPassResponse = emailPassAuthViewModel.loginResponse){
        is Response.Loading-> ProgressBar()

        is Response.Success->{
            LaunchedEffect(key1 = emailPassResponse, block = {
                if(emailPassAuthViewModel.isEmailVerified()){
                    goToHomeScreen()
                }
                else{
                    Toast.makeText(context,"Please verify your email",Toast.LENGTH_LONG).show()
                }
            })
        }

        is Response.Failure->{
            Log.e("TAG","LOGIN EMAIL Response Failed : ${emailPassResponse.e.localizedMessage}")
            Toast.makeText(context,"something is wrong",Toast.LENGTH_LONG).show()
        }
    }
}

@Composable
fun ResetPasswordResponse(
    emailPassAuthViewModel: EmailPassAuthViewModel
){
    val context = LocalContext.current

    when(val resetPassResponse = emailPassAuthViewModel.resetPasswordResponse){
        is Response.Loading-> ProgressBar()

        is Response.Success->{
            LaunchedEffect(key1 = resetPassResponse, block = {
                if(resetPassResponse.data!!){
                    Toast.makeText(context,"Please check your email",Toast.LENGTH_LONG).show()
                }
            })
        }

        is Response.Failure->{

            Toast.makeText(context,"something is wrong",Toast.LENGTH_LONG).show()
        }
    }
}
