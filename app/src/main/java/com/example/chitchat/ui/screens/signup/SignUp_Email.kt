package com.example.chitchat.ui.screens.signup

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.chitchat.R
import com.example.chitchat.model.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun SignScreen_1(
    firebaseAuth: FirebaseAuth,
    chatViewModel: ChatViewModel
) {

    var email by remember { mutableStateOf("") }

    var passwordValue by remember { mutableStateOf("") }
    var passwordValueConfirm by remember { mutableStateOf("") }

    val context = LocalContext.current

    SingContents(
        emailValue = email,
        emailValueChanged = { email = it },
        passWordValue = passwordValue,
        passwordValueChanged = { passwordValue = it },
        confirmPassWordValue = passwordValueConfirm,
        confirmPasswordValueChanged = { passwordValueConfirm = it },

        onClickSignIn = {
            createAccount(
                firebaseAuth = firebaseAuth,
                email = email,
                password = passwordValue,
                context = context,
                chatViewModel = chatViewModel,
                activity = context as Activity
            )
        }
    )

}

@Composable
fun SingContents(
    modifier: Modifier = Modifier,
    emailValue: String,
    emailValueChanged: (String) -> Unit,
    passWordValue: String,
    passwordValueChanged: (String) -> Unit,
    confirmPassWordValue: String,
    confirmPasswordValueChanged: (String) -> Unit,
    onClickSignIn: () -> Unit,
) {
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
fun ConfirmPassword(
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

private fun createAccount(
    firebaseAuth: FirebaseAuth,
    email: String,
    password: String,
    context: Context,
    chatViewModel: ChatViewModel,
    activity: Activity
) {

    firebaseAuth
        .createUserWithEmailAndPassword(email, password)
        .addOnCompleteListener(activity) {
            if (it.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.e("TAG", "createUserWithEmail:success")

               sendEmailVerification(firebaseAuth, chatViewModel,context)

            } else {
                // If sign in fails, display a message to the user.
                Log.e("TAG", "createUserWithEmail:failure", it.exception)

                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
}

private fun sendEmailVerification(firebaseAuth: FirebaseAuth,chatViewModel: ChatViewModel,context:Context){
    val user = firebaseAuth.currentUser

    user?.sendEmailVerification()?.addOnCompleteListener{
        if(it.isSuccessful){
            chatViewModel.setScreenType(ScreenType.SignProfile)

            Toast.makeText(context, "please check your email box to verify your email", Toast.LENGTH_LONG).show()
        }
        else{
            Log.e("TAG","SEND EMAIL failed: ${it.exception}")
        }
    }
}


