package com.example.chitchat.models

data class UiState(
    val screenType: ScreenType = ScreenType.ChooseLogin
)

enum class ScreenType{
    ChooseLogin,
    LoginEmail,
    SingChoose,
    SignEmail,
    SingPhone,
    SignCode,
    SignProfile,
    HomeList,
    OneToOneChat,
}