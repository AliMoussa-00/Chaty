package com.example.chitchat.ui.screens.commons

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginCard(
    modifier: Modifier = Modifier,
    image: Int? = null,
    isImageVector: Boolean = false,
    text: Int,
    onClick: () -> Unit,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {

            if (isImageVector) {
                Icon(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    imageVector = Icons.Default.Email,
                    contentDescription = null,
                )
            } else {
                Image(
                    modifier = Modifier
                        .size(30.dp)
                        .clip(CircleShape),
                    painter = painterResource(image!!),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Text(
                text = stringResource(id = text),
                fontSize = 18.sp
            )
        }
    }
}