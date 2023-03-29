package com.example.chitchat.ui.screens.login

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.chitchat.R
import com.example.chitchat.model.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    firebaseAuth: FirebaseAuth,
) {

    var userNameValue by remember { mutableStateOf("") }
    var passwordValue by remember { mutableStateOf("") }
    val context = LocalContext.current

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center

    ) {

        LoginFields(
            userNameValue = userNameValue,
            userNameValueChanged = { userNameValue = it },
            passWordValue = passwordValue,
            passwordValueChanged = { passwordValue = it },
            onClickSignIn = { chatViewModel.setScreenType(ScreenType.SingChoose) },
            onClickForget = {
                if(userNameValue.isNotEmpty()){
                    resetPassword(firebaseAuth = firebaseAuth, email = userNameValue, context = context)
                }
                else{
                    Toast.makeText(context,"please enter your email!",Toast.LENGTH_LONG).show()
                }
            },
            onClickLogin = {
                loginUser(firebaseAuth, userNameValue, passwordValue, chatViewModel, context)
            },
        )

    }
}

@Composable
fun LoginFields(
    modifier: Modifier = Modifier,
    userNameValue: String,
    userNameValueChanged: (String) -> Unit,
    passWordValue: String,
    passwordValueChanged: (String) -> Unit,
    onClickForget: () -> Unit,
    onClickLogin: () -> Unit,
    onClickSignIn: () -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Spacer(modifier = Modifier.weight(1f))

        UserNAmeField(userNameValue = userNameValue, userNameValueChanged = userNameValueChanged)
        PassWordField(passWordValue = passWordValue, passwordValueChanged = passwordValueChanged)
        ForgetPassWord(onClickForget = onClickForget)
        LoginButton(onClickLogin = onClickLogin)
        SignUp(onClickSignUp = onClickSignIn)
        Spacer(modifier = Modifier.weight(1f))
        LoginDivider()
        ContinueLogin()

    }
}

@Composable
fun UserNAmeField(
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
fun PassWordField(
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
fun ForgetPassWord(modifier: Modifier = Modifier, onClickForget: () -> Unit) {
    TextButton(
        onClick = { onClickForget() }
    ) {
        Text(text = stringResource(id = R.string.forget_password))
    }
}

@Composable
fun LoginButton(modifier: Modifier = Modifier, onClickLogin: () -> Unit) {
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
fun LoginDivider(modifier: Modifier = Modifier) {

    Row(
        modifier = modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Divider(Modifier.weight(1f))
        Text(text = stringResource(id = R.string.or))
        Divider(Modifier.weight(1f))
    }
}


@Composable
fun ContinueLogin(modifier: Modifier = Modifier) {

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /*TODO*/ }) {
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.facebook),
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        IconButton(onClick = { /*TODO*/ }) {
            Image(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                painter = painterResource(id = R.drawable.google),
                contentDescription = null,
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
        Text(text = stringResource(id = R.string.dont_have_account))

        TextButton(onClick = { onClickSignUp() }) {
            Text(text = stringResource(id = R.string.signup))
        }
    }
}

private fun loginUser(
    firebaseAuth: FirebaseAuth,
    email: String,
    password: String,
    chatViewModel: ChatViewModel,
    context: Context,
) {

    firebaseAuth.signInWithEmailAndPassword(email, password)
        .addOnCompleteListener {
            if (it.isSuccessful) {
                Log.e("TAG", "LOGIN successful")
                verifyEmailAddress(
                    firebaseAuth = firebaseAuth,
                    chatViewModel = chatViewModel,
                    context = context
                )
            } else {
                Log.e("TAG", "LOGIN failed :${it.exception}")
                Toast.makeText(context, "Login failed ! ", Toast.LENGTH_LONG).show()
            }
        }
}

private fun verifyEmailAddress(
    firebaseAuth: FirebaseAuth,
    chatViewModel: ChatViewModel,
    context: Context,
) {
    val user = firebaseAuth.currentUser
    if (user!!.isEmailVerified) {
        chatViewModel.setScreenType(ScreenType.HomeList)
    } else {
        Toast.makeText(context, "Please verify your email", Toast.LENGTH_LONG).show()
    }
}

private fun resetPassword(firebaseAuth: FirebaseAuth,email:String,context: Context){
    firebaseAuth.sendPasswordResetEmail(email)
        .addOnCompleteListener{
            if(it.isSuccessful){
                Toast.makeText(context,"Please check your email box",Toast.LENGTH_LONG).show()
            }
            else{
                Toast.makeText(context,"Something is wrong !",Toast.LENGTH_LONG).show()
            }
        }
}