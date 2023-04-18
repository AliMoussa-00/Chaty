package com.example.chitchat.ui.screens.signup.email

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.domain.Response
import com.example.chitchat.domain.auth.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EmailPassAuthViewModel @Inject constructor(
    private val repo: EmailPasswordAuthRepository,
) : ViewModel() {

    var signUpResponse by mutableStateOf<SignUpEmailPassResponse>(Response.Success(false))
        private set

    var sendVerificationEmailResponse by mutableStateOf<SendVerificationEmailResponse>(
        Response.Success(false)
    )
        private set

    var loginResponse by mutableStateOf<SignInEmailPassResponse>(Response.Success(false))
        private set

    var resetPasswordResponse by mutableStateOf<SignInEmailPassResponse>(Response.Success(false))
        private set

    fun signUpWithEmailAndPassword(email:String,password:String){
        viewModelScope.launch {
            signUpResponse = Response.Loading
            signUpResponse = repo.firebaseSignUpWithEmailAndPassword(email, password)
        }
    }

    fun sendVerificationEmail(){
        viewModelScope.launch {
            sendVerificationEmailResponse = Response.Loading
            sendVerificationEmailResponse = repo.sendEmailVerification()
        }
    }

    fun loginWithEmailPass(email: String, password: String){
        viewModelScope.launch {
            loginResponse = Response.Loading
            loginResponse = repo.firebaseSignInWithEmailPassword(email, password)
        }
    }

    fun resetPassword(email: String){
        viewModelScope.launch {
            resetPasswordResponse = Response.Loading
            resetPasswordResponse = repo.sendPasswordResetEmail(email)
        }
    }

    fun isEmailVerified(): isEmailVerifiedResponse {
        return repo.isEmailVerified()
    }

    fun resetSignUpResponse(){
        signUpResponse = Response.Success(false)
    }
    fun resetSendVerificationEmailResponse(){
        sendVerificationEmailResponse = Response.Success(false)
    }

}