package com.example.chitchat.ui.screens.commons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatTopAppBar(
    topAppBarTitle: String,
    canGoBack: Boolean = true,
    onClickBack: ()->Unit
){
    TopAppBar(
        title = { Text(text = topAppBarTitle)},
        navigationIcon = {
            if(canGoBack){
                IconButton(onClick = onClickBack) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = null)
                }
            }
        }
    )
}