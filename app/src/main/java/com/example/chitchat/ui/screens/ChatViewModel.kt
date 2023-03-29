package com.example.chitchat.ui.screens

import androidx.lifecycle.ViewModel
import com.example.chitchat.model.CurrentUser
import com.example.chitchat.model.ScreenType
import com.example.chitchat.model.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ChatViewModel : ViewModel() {

    private var _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

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
    //----- Setting Current User --
    //----------------------------
    fun setCurrentUser(currentUser: CurrentUser) {
        _uiState.update {
            it.copy(
                currentUser = currentUser
            )
        }
    }
}