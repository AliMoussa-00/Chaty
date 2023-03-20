package com.example.chitchat.ui.screens.signup

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.example.chitchat.R

@Composable
fun SignScreen_1() {

    var email by remember { mutableStateOf("") }

    var passwordValue by remember { mutableStateOf("") }
    var passwordValueConfirm by remember { mutableStateOf("") }

    SingContents(
        emailValue = email,
        emailValueChanged = { email = it },
        passWordValue = passwordValue,
        passwordValueChanged = { passwordValue = it },
        confirmPassWordValue = passwordValueConfirm,
        confirmPasswordValueChanged = { passwordValueConfirm = it }
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
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {

        Spacer(modifier = Modifier.weight(0.4f))

        Email(emailValue = emailValue, emailValueChanged = emailValueChanged)

        Password(passWordValue = passWordValue, passwordValueChanged = passwordValueChanged)

        ConfirmPassword(
            confirmPassWordValue = confirmPassWordValue,
            confirmPasswordValueChanged = confirmPasswordValueChanged
        )

        SignInButton(onClickSignIn = { /*TODO*/ })
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
    val showPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = passWordValue,
        onValueChange = { passwordValueChanged(it) },
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.passeword)) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        keyboardActions = KeyboardActions(onDone = null),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { passwordValueChanged("") }) {
                if (passWordValue.isNotEmpty()) {
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
fun ConfirmPassword(
    modifier: Modifier = Modifier,
    confirmPassWordValue: String,
    confirmPasswordValueChanged: (String) -> Unit,
) {
    val showPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = confirmPassWordValue,
        onValueChange = { confirmPasswordValueChanged(it) },
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.confirm_password)) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(onDone = null),
        visualTransformation = if (showPassword) VisualTransformation.None else PasswordVisualTransformation(),
        trailingIcon = {
            IconButton(onClick = { confirmPasswordValueChanged("") }) {
                if (confirmPassWordValue.isNotEmpty()) {
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
fun SignInButton(
    onClickSignIn: () -> Unit,
) {
    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = { onClickSignIn() }
    ) {
        Text(text = stringResource(id = R.string.signup))
    }
}


