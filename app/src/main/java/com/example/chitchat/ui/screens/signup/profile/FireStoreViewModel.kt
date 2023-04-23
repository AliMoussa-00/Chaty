package com.example.chitchat.ui.screens.signup.profile

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chitchat.domain.Response
import com.example.chitchat.domain.firestore.AddUserToDBResponse
import com.example.chitchat.domain.firestore.FireStoreRepository
import com.example.chitchat.domain.firestore.SetUserToDBResponse
import com.example.chitchat.domain.firestore.UploadImageToStorageResponse
import com.example.chitchat.models.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FireStoreViewModel @Inject constructor(private val repo: FireStoreRepository): ViewModel() {

    var addUserToDBResponse by mutableStateOf<AddUserToDBResponse>(Response.Success(false))
        private set

    var setUserToDBResponse by mutableStateOf<SetUserToDBResponse>(Response.Success(false))
        private set

    var uploadImageToStorageResponse by mutableStateOf<UploadImageToStorageResponse>(Response.Success(false))
        private set

    fun addUserToDB(user: User){
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

    fun resetAddUserToDBResponse(){
        addUserToDBResponse = Response.Success(false)
    }
    fun resetSetUserToDBResponse(){
        setUserToDBResponse = Response.Success(false)
    }
    fun resetUploadImageToStorageResponse(){
        uploadImageToStorageResponse = Response.Success(false)
    }
}