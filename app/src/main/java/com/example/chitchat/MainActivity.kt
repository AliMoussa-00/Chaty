package com.example.chitchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chitchat.model.ScreenType
import com.example.chitchat.model.UiState
import com.example.chitchat.ui.screens.ChatViewModel
import com.example.chitchat.ui.screens.list_users.ListUsersScreen
import com.example.chitchat.ui.screens.login.components.LoginEmailScreen
import com.example.chitchat.ui.screens.login.components.LoginScreen
import com.example.chitchat.ui.screens.signup.ChooseSignUpMethode
import com.example.chitchat.ui.screens.signup.email.SignUpEmail
import com.example.chitchat.ui.screens.signup.phone.SignUpCode
import com.example.chitchat.ui.screens.signup.phone.SignUpPhone
import com.example.chitchat.ui.screens.signup.profile.SignScreen_Profile
import com.example.chitchat.ui.theme.ChitChatTheme
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChitChatTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {

                    val chatViewModel : ChatViewModel = viewModel()

                    val uiState by chatViewModel.uiState.collectAsState()


                    SetScreen(
                        chatViewModel = chatViewModel,
                        uiState = uiState,
                    )
                }

            }
        }
    }

    @Composable
    private fun SetScreen(
        chatViewModel: ChatViewModel,
        uiState: UiState,
    ) {
        when (uiState.screenType) {
            ScreenType.ChooseLogin -> {
                LoginScreen(chatViewModel = chatViewModel)
            }
            ScreenType.LoginEmail -> {
                LoginEmailScreen(chatViewModel = chatViewModel)
            }
            ScreenType.SingChoose -> {
                ChooseSignUpMethode(chatViewModel = chatViewModel)
            }
            ScreenType.SignEmail -> {
                SignUpEmail(chatViewModel = chatViewModel)
            }
            ScreenType.SingPhone -> {
                SignUpPhone(chatViewModel = chatViewModel)
            }
            ScreenType.SignCode -> {
                SignUpCode(chatViewModel = chatViewModel)
            }
            ScreenType.SignProfile -> {
                SignScreen_Profile(chatViewModel = chatViewModel)
            }
            ScreenType.HomeList -> {
                ListUsersScreen()
            }
        }
    }
}
