package com.example.chitchat.ui.screens.commons

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog

@Composable
fun ProgressBar(modifier: Modifier = Modifier) {

    Dialog(onDismissRequest = { })
    {
        Surface(

            tonalElevation = 2.dp,
            shape = MaterialTheme.shapes.small
        ) {
            CircularProgressIndicator(modifier = modifier.padding(8.dp),)
        }
    }

}