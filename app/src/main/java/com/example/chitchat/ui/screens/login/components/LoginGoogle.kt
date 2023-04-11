package com.example.chitchat.ui.screens.login.components

import android.app.Activity
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chitchat.R
import com.example.chitchat.domain.Response
import com.example.chitchat.model.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel
import com.example.chitchat.ui.screens.commons.LoginCard
import com.example.chitchat.ui.screens.commons.ProgressBar
import com.example.chitchat.ui.screens.login.GoogleAuthViewModel
import com.google.android.gms.auth.api.identity.BeginSignInResult
import com.google.firebase.auth.GoogleAuthProvider


@Composable
fun LoginGoogle(
    googleAuthViewModel: GoogleAuthViewModel = hiltViewModel(),
) {

    val launcher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.StartIntentSenderForResult(),
            onResult = {
                if (it.resultCode == Activity.RESULT_OK) {

                    try {
                        val credential =
                            googleAuthViewModel.oneTapClient.getSignInCredentialFromIntent(it.data)
                        val googleIdToken = credential.googleIdToken
                        val googleCredential = GoogleAuthProvider.getCredential(googleIdToken, null)

                        googleAuthViewModel.signInWithGoogle(googleCredential)


                    } catch (e: Exception) {
                        Log.e("TAG", "rememberLauncherForActivityResult : ${e.localizedMessage}")
                    }
                } else {
                    Log.e("TAG", "rememberLauncherForActivityResult ACTIVITY.CANCEL")
                }
            })

    fun launch(signInResult: BeginSignInResult) {
        val intent = IntentSenderRequest.Builder(signInResult.pendingIntent.intentSender).build()
        launcher.launch(intent)
    }

    LoginGoogleBtn(continueGoogle = { googleAuthViewModel.oneTapSignIn() })

    OneTapSignInResponse(googleAuthViewModel = googleAuthViewModel, launch = { launch(it) })

    SignInWithGoogleResponse(googleAuthViewModel = googleAuthViewModel)
}

@Composable
fun LoginGoogleBtn(
    continueGoogle: () -> Unit,
) {
    LoginCard(
        image = R.drawable.google,
        text = R.string.signin_google) {
        continueGoogle()
    }
}

@Composable
fun OneTapSignInResponse(
    googleAuthViewModel: GoogleAuthViewModel,
    launch: (result: BeginSignInResult) -> Unit,
) {
    when (val oneTapSignInResponse = googleAuthViewModel.oneTapSignInResponse) {

        is Response.Loading -> {
            ProgressBar()
        }

        is Response.Success -> {
            oneTapSignInResponse.data?.let {
                LaunchedEffect(key1 = it) {
                    launch(it)
                }
            }
        }

        is Response.Failure -> {
            Log.e("TAG", "OneTapSignIn launch FAILED ")
        }
    }
}

@Composable
fun SignInWithGoogleResponse(
    chatViewModel: ChatViewModel = viewModel(),
    googleAuthViewModel: GoogleAuthViewModel,
) {
    when (val signInWithGoogleResponse = googleAuthViewModel.signInWithGoogleResponse) {
        is Response.Loading -> {
            ProgressBar()
        }

        is Response.Success -> {
            signInWithGoogleResponse.data?.let {
                LaunchedEffect(key1 = it, block = {
                    chatViewModel.setScreenType(ScreenType.HomeList)
                })
            }
        }

        is Response.Failure -> {
            Log.e("TAG", "signInWithGoogleResponse FAILED ")
        }
    }
}
