package com.example.chitchat.ui.screens.signup

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.chitchat.R
import com.togitech.ccp.component.TogiCountryCodePicker
import com.togitech.ccp.component.isPhoneNumber

@Composable
fun SignUp_Phone() {

    SignUpPhoneContents()
}

@Composable
fun SignUpPhoneContents(
    modifier: Modifier = Modifier,
) {
    val phoneNumber = rememberSaveable { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = Modifier.weight(1f))

        Text(text = stringResource(id = R.string.continue_phone))

        TogiCountryCodePicker(
            text = phoneNumber.value,
            onValueChange = { phoneNumber.value = it },
            modifier = Modifier.fillMaxWidth(),
            shape = MaterialTheme.shapes.small,
            unfocusedBorderColor = MaterialTheme.colorScheme.outline
        )

        Spacer(modifier = Modifier.weight(1f))

        SignInButton {
            if(isPhoneNumber()){

            }
        }

    }
}
