package com.example.chitchat.ui.screens.signup

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun SignUpCode() {
    SignUpCodeContents()
}


@Composable
fun SignUpCodeContents(
    modifier: Modifier = Modifier
){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceAround
    ) {

        OtpFields()

        SignInButton { /*TODO*/}
    }
}

@SuppressLint("MutableCollectionMutableState")
@Composable
fun OtpFields(
    modifier:Modifier = Modifier
) {

    val focusManager = LocalFocusManager.current
    val codeDigits = remember { MutableList(4) { mutableStateOf("") } }
Log.e("CODE DIGITS","codeDigits= ${codeDigits[0].value}  ${codeDigits[1].value}  ${codeDigits[2].value}  ${codeDigits[3].value} ")
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
            if(it.isNotEmpty() && it.length == 1){
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
            fontSize = 24.sp
        ),
        maxLines = 1,
        singleLine = true,
        modifier = modifier
            .size(58.dp)
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