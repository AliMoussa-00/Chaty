package com.example.chitchat.model

data class UiState(
    val screenType: ScreenType = ScreenType.Login,
    val currentUser: CurrentUser= CurrentUser()
)

enum class ScreenType{
    Login,
    SingChoose,
    SignEmail,
    SingPhone,
    SignCode,
    SignProfile,
    HomeList
}