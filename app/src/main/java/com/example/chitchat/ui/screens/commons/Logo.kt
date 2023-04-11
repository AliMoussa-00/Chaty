package com.example.chitchat.ui.screens.commons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.chitchat.R

@Composable
fun ChattyLogo(modifier: Modifier = Modifier){
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(40.dp)
        ,
        contentAlignment = Alignment.Center
    ){
        Text(
            text = stringResource(id = R.string.app_name),
            fontSize = 60.sp,
        )
    }
}