package com.example.chitchat.ui.screens.signup.phone

import android.app.Activity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.domain.Response
import com.example.chitchat.domain.auth.PhoneAuthRepository
import com.example.chitchat.domain.auth.ResendVerificationCodeResponse
import com.example.chitchat.domain.auth.SendVerificationCodeResponse
import com.example.chitchat.domain.auth.SignInWithPhoneCredentialResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PhoneAuthViewModel @Inject constructor(private val repo: PhoneAuthRepository) : ViewModel() {

    var sendVerificationCodeResponse by mutableStateOf<SendVerificationCodeResponse>(
        Response.Success(false)
    )
        private set

    var resendVerificationCodeResponse by mutableStateOf<ResendVerificationCodeResponse>(
        Response.Success(false)
    )
        private set


    var signInWithPhoneCredentialResponse by mutableStateOf<SignInWithPhoneCredentialResponse>(
        Response.Success(false)
    )
        private set

    fun sendVerificationCode(phone: String,activity: Activity) {
        viewModelScope.launch {
            sendVerificationCodeResponse = Response.Loading
            sendVerificationCodeResponse = repo.sendVerificationCode(phone,activity)
        }
    }

    fun resendVerificationCode() {
        viewModelScope.launch {
            resendVerificationCodeResponse = Response.Loading
            resendVerificationCodeResponse = repo.resendCode()
        }
    }

    fun signInWithPhoneCredential(otp: String) {
        viewModelScope.launch {
            signInWithPhoneCredentialResponse = Response.Loading
            signInWithPhoneCredentialResponse = repo.signInWithPhoneCredential(otp)
        }
    }

    fun resetSendVerificationCodeResponse(){
        sendVerificationCodeResponse = Response.Success(false)
    }

    fun resetResendVerificationCodeResponse(){
        resendVerificationCodeResponse = Response.Success(false)
    }

    fun resetSignInWithPhoneCredentialResponse(){
        signInWithPhoneCredentialResponse = Response.Success(false)
    }


}