package com.example.chitchat.ui.screens.signup.phone

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.chitchat.R
import com.example.chitchat.domain.Response
import com.example.chitchat.model.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel
import com.example.chitchat.ui.screens.commons.ChatTopAppBar
import com.example.chitchat.ui.screens.commons.ProgressBar
import com.example.chitchat.ui.screens.signup.email.SignInButton
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.component.getFullPhoneNumber

@Composable
fun SignUpPhone(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    phoneAuthViewModel: PhoneAuthViewModel = hiltViewModel(),
) {
    BackHandler {
        chatViewModel.setScreenType(ScreenType.SingChoose)
    }

    val context = LocalContext.current

   Column(modifier = modifier.fillMaxSize()) {
       ChatTopAppBar(
           topAppBarTitle = stringResource(id = R.string.signup_code),
           onClickBack = {chatViewModel.setScreenType(ScreenType.SingChoose)}
       )

       SignUpPhoneContents(
           onClickSendCode = {
               phoneAuthViewModel.sendVerificationCode(it,context as Activity)
           }
       )

       SendingCodeResponse(chatViewModel, phoneAuthViewModel)
   }
}

@Composable
fun SignUpPhoneContents(
    modifier: Modifier = Modifier,
    onClickSendCode: (String)->Unit
) {
    var phoneNumber by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Text(text = stringResource(id = R.string.continue_phone))

        TogiCountryCodePicker(
            text = phoneNumber,
            onValueChange = { phoneNumber = it },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.weight(1f))

        SignInButton(
            isEnabled = true,
            onClickSignIn = {
               onClickSendCode(getFullPhoneNumber())
            }
        )

    }
}

@Composable
private fun SendingCodeResponse(
    chatViewModel: ChatViewModel,
    phoneAuthViewModel: PhoneAuthViewModel
){
    val context = LocalContext.current

  when(val sendVerificationCodeResponse = phoneAuthViewModel.sendVerificationCodeResponse){
      is Response.Loading -> ProgressBar()

      is Response.Success ->{
          LaunchedEffect(key1 = sendVerificationCodeResponse, block = {
              if(sendVerificationCodeResponse.data!!){
                  chatViewModel.setScreenType(ScreenType.SignCode)
              }
          })
      }

      is Response.Failure -> {
          Toast.makeText(context,"Something is wrong", Toast.LENGTH_LONG).show()
      }
  }
}





