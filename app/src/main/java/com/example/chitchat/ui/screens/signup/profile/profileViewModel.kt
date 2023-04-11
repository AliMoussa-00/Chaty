package com.example.chitchat.ui.screens.signup.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.domain.Response
import com.example.chitchat.domain.auth.AddUserToDBResponse
import com.example.chitchat.domain.auth.RealTimeDBRepository
import com.example.chitchat.domain.auth.SetUserToDBResponse
import com.example.chitchat.domain.auth.UploadImageToStorageResponse
import com.example.chitchat.model.CurrentUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class profileViewModel @Inject constructor(private val repo: RealTimeDBRepository): ViewModel() {

    var addUserToDBResponse by mutableStateOf<AddUserToDBResponse>(Response.Success(false))
        private set

    var setUserToDBResponse by mutableStateOf<SetUserToDBResponse>(Response.Success(false))
        private set

    var uploadImageToStorageResponse by mutableStateOf<UploadImageToStorageResponse>(Response.Success(false))
        private set

    fun addUserToDB(user: CurrentUser){
        viewModelScope.launch {
            addUserToDBResponse = Response.Loading
            addUserToDBResponse = repo.addUserData(user)
        }
    }

    fun setUserData(childPath:String,data: String){
        viewModelScope.launch {
            setUserToDBResponse = Response.Loading
            setUserToDBResponse = repo.setUserSpecificData(childPath,data)
        }
    }

    fun uploadImageToStorage(image:String){
        viewModelScope.launch {
            uploadImageToStorageResponse = Response.Loading
            uploadImageToStorageResponse = repo.uploadImageToStorage(image)
        }
    }
}