package com.example.chitchat.ui.screens.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.domain.Response
import com.example.chitchat.domain.profile_repo.ProfileRepository
import com.example.chitchat.domain.profile_repo.SignOutResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val repository: ProfileRepository
):ViewModel() {

    var signOutResponse by mutableStateOf<SignOutResponse>(Response.Success(false))
        private set


    fun signOut(){
        viewModelScope.launch {
            signOutResponse = Response.Loading
            signOutResponse = repository.signOut()
        }
    }

    fun setSignOutResponseFalse(){
        signOutResponse = Response.Success(false)
    }

}