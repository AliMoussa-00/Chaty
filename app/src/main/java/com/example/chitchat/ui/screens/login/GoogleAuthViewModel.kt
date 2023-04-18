package com.example.chitchat.ui.screens.login

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.domain.Response
import com.example.chitchat.domain.auth.GoogleAuthRepository
import com.example.chitchat.domain.auth.OneTapSignInResponse
import com.example.chitchat.domain.auth.SignInWithGoogleResponse
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.auth.AuthCredential
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GoogleAuthViewModel @Inject constructor(
    private val repo : GoogleAuthRepository,
    val oneTapClient : SignInClient
) : ViewModel() {

    val isUserAuthenticated = repo.isUserAuthenticatedWithFirebase

    var oneTapSignInResponse by mutableStateOf<OneTapSignInResponse>(Response.Success(null))
        private set

    var signInWithGoogleResponse by mutableStateOf<SignInWithGoogleResponse>(Response.Success(null))

    fun oneTapSignIn(){
        viewModelScope.launch {
            oneTapSignInResponse = Response.Loading
            oneTapSignInResponse = repo.oneTapSignInWithGoogle()
        }
    }

    fun signInWithGoogle(googleCredential : AuthCredential){
        viewModelScope.launch {
            oneTapSignInResponse = Response.Loading
            signInWithGoogleResponse = repo.firebaseSignInWithGoogle(googleCredential)
        }
    }

    fun resetSignInGoogleResponse(){
        oneTapSignInResponse = Response.Success(null)
        signInWithGoogleResponse = Response.Success(null)
    }

}