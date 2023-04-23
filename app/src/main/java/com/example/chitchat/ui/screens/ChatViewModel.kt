package com.example.chitchat.ui.screens

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.chitchat.models.ScreenType
import com.example.chitchat.models.UiState
import com.example.chitchat.models.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatViewModel : ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    var friendUser by mutableStateOf(User())
        private set

    //----------------------------
    //----- Setting Screen Type
    //----------------------------
    fun setScreenType(screenType: ScreenType) {
        _uiState.update {
            it.copy(
                screenType = screenType
            )
        }
    }

    //----------------------------
    //----- setting friend
    //----------------------------
    fun settingFriendUserData(user: User){
        friendUser = user
    }

}