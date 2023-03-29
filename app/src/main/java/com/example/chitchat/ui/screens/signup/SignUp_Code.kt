package com.example.chitchat.ui.screens.signup

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
import com.example.chitchat.R
import com.example.chitchat.model.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider

private var verificationID by mutableStateOf("")

@Composable
fun SignUpCode(
    chatViewModel: ChatViewModel,
    firebaseAuth: FirebaseAuth,
) {
    SignUpCodeContents(
        chatViewModel = chatViewModel,
        firebaseAuth = firebaseAuth
    )
}


@Composable
fun SignUpCodeContents(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    firebaseAuth: FirebaseAuth,
) {
    val codeDigits = remember { MutableList(6) { mutableStateOf("") } }

    val context = LocalContext.current

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
            isEnabled = codeDigits.size == 6 ,
            onClickSignIn = {
                Log.e("TAG", "CODE = ${codeDigits.joinToString(separator = ""){it.value}}")

                val credential = PhoneAuthProvider.getCredential(
                    verificationID,
                    codeDigits.joinToString(separator = ""){it.value}
                )

                signInWithPhoneAuthCredential(
                    phoneAuthCredential = credential,
                    firebaseAuth = firebaseAuth,
                    activity = context as Activity,
                    context = context,
                    chatViewModel = chatViewModel
                )
            }
        )
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

// on below line creating method to
// sign in with phone credentials
private fun signInWithPhoneAuthCredential(
    phoneAuthCredential: PhoneAuthCredential,
    firebaseAuth: FirebaseAuth,
    activity: Activity,
    context: Context,
    chatViewModel: ChatViewModel,
) {
    firebaseAuth.signInWithCredential(phoneAuthCredential)
        .addOnCompleteListener(activity) {
            if (it.isSuccessful) {
                // Sign in success, update UI with the signed-in user's information
                Log.e("TAG", "createUserWithEmail:success")
                chatViewModel.setScreenType(ScreenType.SignProfile)

            } else {
                // If sign in fails, display a message to the user.
                Log.e("TAG", "createUserWithEmail:failure", it.exception)

                Toast.makeText(context, "Authentication failed.", Toast.LENGTH_SHORT).show()
            }
        }
}


val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
    override fun onVerificationCompleted(credential: PhoneAuthCredential) {
        // This callback will be invoked in two situations:
        // 1 - Instant verification. In some cases the phone number can be instantly
        //     verified without needing to send or enter a verification code.
        // 2 - Auto-retrieval. On some devices Google Play services can automatically
        //     detect the incoming verification SMS and perform verification without
        //     user action.
        Log.e("TAG", "onVerificationCompleted:$credential")
    }

    override fun onVerificationFailed(e: FirebaseException) {
        // This callback is invoked in an invalid request for verification is made,
        // for instance if the the phone number format is not valid.
        Log.e("TAG", "onVerificationFailed", e)

        if (e is FirebaseAuthInvalidCredentialsException) {
            // Invalid request
        } else if (e is FirebaseTooManyRequestsException) {
            // The SMS quota for the project has been exceeded
        }

        // Show a message and update the UI
    }

    override fun onCodeSent(
        verificationId: String,
        token: PhoneAuthProvider.ForceResendingToken,
    ) {
        // The SMS verification code has been sent to the provided phone number, we
        // now need to ask the user to enter the code and then construct a credential
        // by combining the code with a verification ID.
        Log.e("TAG", "onCodeSent:$verificationId")

        // Save verification ID and resending token so we can use them later

        verificationID = verificationId

        // storedToken = token
    }
}


