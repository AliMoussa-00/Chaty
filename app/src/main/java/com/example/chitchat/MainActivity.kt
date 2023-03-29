package com.example.chitchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.chitchat.model.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel
import com.example.chitchat.ui.screens.list_users.ListUsersScreen
import com.example.chitchat.ui.screens.login.LoginScreen
import com.example.chitchat.ui.screens.signup.*
import com.example.chitchat.ui.theme.ChitChatTheme
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : ComponentActivity() {

    // Firebase instance variables
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ChitChatTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val chatViewModel: ChatViewModel by viewModels()
                    val uiState by chatViewModel.uiState.collectAsState()

                    // Initialize Firebase Auth
                    firebaseAuth = Firebase.auth

                    val currentUser = firebaseAuth.currentUser

                    // check if the user is not registered
                    if (currentUser == null) {
                        // send to sign screen
                        chatViewModel.setScreenType(ScreenType.SignEmail)

                        SignScreen_1(
                            firebaseAuth = firebaseAuth,
                            chatViewModel = chatViewModel
                        )
                    } else {

                        when (uiState.screenType) {
                            ScreenType.Login -> {
                                LoginScreen(
                                    chatViewModel = chatViewModel,
                                    firebaseAuth = firebaseAuth
                                )
                            }
                            ScreenType.SingChoose->{
                                ChooseSignUpMethode(chatViewModel = chatViewModel)
                            }
                            ScreenType.SignEmail -> {
                                SignScreen_1(firebaseAuth = firebaseAuth,chatViewModel)
                            }
                            ScreenType.SingPhone -> {
                                SignUpPhone(firebaseAuth,chatViewModel)
                            }
                            ScreenType.SignCode -> {
                                SignUpCode(chatViewModel,firebaseAuth)
                            }
                            ScreenType.SignProfile -> {
                                SignScreen_2(firebaseAuth, chatViewModel, uiState)
                            }
                            ScreenType.HomeList->{
                                ListUsersScreen()
                            }
                        }
                    }

                }
            }
        }
    }

    /*  override fun onStart() {
          super.onStart()
          setContent {
              ChitChatTheme {
                  // A surface container using the 'background' color from the theme
                  Surface(
                      modifier = Modifier.fillMaxSize(),
                      color = MaterialTheme.colorScheme.background
                  ) {
                      val chatViewModel : ChatViewModel by viewModels()

                      // Initialize Firebase Auth
                      firebaseAuth = Firebase.auth

                      val currentUser= firebaseAuth.currentUser

                      // check if the user is not registered
                      if(currentUser == null){
                          // send to sign screen
                          chatViewModel.setScreenType(ScreenType.SignEmail)

                          SignScreen_1(
                              firebaseAuth = firebaseAuth
                          )
                      }
                      else{

                          LoginScreen(
                              chatViewModel = chatViewModel
                          )
                      }

                  }
              }
          }

      }*/


}
