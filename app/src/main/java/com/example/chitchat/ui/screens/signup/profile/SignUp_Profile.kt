package com.example.chitchat.ui.screens.signup.profile

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.example.chitchat.R
import com.example.chitchat.domain.Response
import com.example.chitchat.model.CurrentUser
import com.example.chitchat.model.ScreenType
import com.example.chitchat.ui.screens.ChatViewModel
import com.example.chitchat.ui.screens.commons.ChatTopAppBar
import com.example.chitchat.ui.screens.commons.ProgressBar
import com.example.chitchat.ui.screens.signup.email.SignInButton


@Composable
fun SignScreen_Profile(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    profileViewModel: profileViewModel = hiltViewModel(),
) {
    BackHandler {}

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf<String?>(null) }
    var profileImage by remember {
        mutableStateOf<Uri>(
            Uri.parse(
                "android.resource://com.example.chitchat/${R.drawable.baseline_account_circle_24}"
            )
        )
    }

    Column(modifier.fillMaxSize()) {
        ChatTopAppBar(
            topAppBarTitle = stringResource(id = R.string.signup_profile),
            canGoBack = false,
            onClickBack = {}
        )

        SingContents2(
            firstNameValue = firstName,
            firstNameValueChanged = { firstName = it },
            lastNameValue = lastName,
            lastNameValueChanged = { lastName = it },
            description = description ?: "",
            descriptionValueChanged = { description = it },
            setPickedImage = { profileImage = it },
            onClickSignIn = {

                val currentUser = CurrentUser(
                    name = "$firstName $lastName",
                    description = description,
                    image = profileImage.toString()
                )

                profileViewModel.addUserToDB(currentUser)

            }
        )

        AddDataToDBResponse(profileViewModel = profileViewModel, image = profileImage.toString())

        UploadImageToStorageResponse(profileViewModel = profileViewModel, chatViewModel = chatViewModel)

    }
}

@Composable
fun SingContents2(
    modifier: Modifier = Modifier,
    firstNameValue: String,
    firstNameValueChanged: (String) -> Unit,
    lastNameValue: String,
    lastNameValueChanged: (String) -> Unit,
    description: String,
    descriptionValueChanged: (String) -> Unit,
    onClickSignIn: () -> Unit,
    setPickedImage: (Uri) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        PickUserImage { setPickedImage(it) }

        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FirstName(
                modifier = Modifier.weight(0.9f),
                firstNameValue = firstNameValue,
                firstNameValueChanged = firstNameValueChanged
            )
            LastName(
                modifier = Modifier.weight(1.1f),
                lastNameValue = lastNameValue,
                lastNameValueChanged = lastNameValueChanged
            )
        }

        DescriptionField(
            description = description,
            descriptionValueChanged = descriptionValueChanged
        )

        SignInButton(
            isEnabled = firstNameValue.isNotEmpty() && lastNameValue.isNotEmpty(),
            onClickSignIn = onClickSignIn
        )
    }
}

@Composable
fun PickUserImage(
    modifier: Modifier = Modifier,
    setPickedImage: (Uri) -> Unit,
) {

    val defaultImage =
        "android.resource://com.example.chitchat/${R.drawable.baseline_account_circle_24}"

    var selectedImage by remember { mutableStateOf<Uri>(Uri.parse(defaultImage)) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImage = it ?: Uri.parse(defaultImage)

            setPickedImage(selectedImage)
        }
    )


    Box(modifier.size(128.dp)) {

        AsyncImage(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape),
            model = selectedImage,
            contentDescription = null,
            contentScale = ContentScale.Crop
        )

        IconButton(
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .clip(CircleShape)
                .background(Color.White),
            onClick = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(
                        ActivityResultContracts.PickVisualMedia.ImageOnly
                    )
                )
            }) {
            Icon(
                painter = painterResource(id = R.drawable.baseline_photo_camera_28),
                contentDescription = stringResource(id = R.string.select_image),
            )
        }
    }
}

@Composable
fun FirstName(
    modifier: Modifier = Modifier,
    firstNameValue: String,
    firstNameValueChanged: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = firstNameValue,
        onValueChange = { firstNameValueChanged(it) },
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.first_name)) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        trailingIcon = {
            IconButton(onClick = { firstNameValueChanged("") }) {
                if (firstNameValue.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(id = R.string.clear_field)
                    )
                }
            }
        }
    )
}

@Composable
fun LastName(
    modifier: Modifier = Modifier,
    lastNameValue: String,
    lastNameValueChanged: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier,
        value = lastNameValue,
        onValueChange = { lastNameValueChanged(it) },
        singleLine = true,
        label = { Text(text = stringResource(id = R.string.last_name)) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        trailingIcon = {
            IconButton(onClick = { lastNameValueChanged("") }) {
                if (lastNameValue.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(id = R.string.clear_field)
                    )
                }
            }
        }
    )
}

@Composable
fun DescriptionField(
    modifier: Modifier = Modifier,
    description: String,
    descriptionValueChanged: (String) -> Unit,
) {
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth(),
        value = description,
        onValueChange = { descriptionValueChanged(it) },
        maxLines = 5,
        label = { Text(text = stringResource(id = R.string.description)) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
        trailingIcon = {
            IconButton(onClick = { descriptionValueChanged("") }) {
                if (description.isNotEmpty()) {
                    Icon(
                        imageVector = Icons.Default.Clear,
                        contentDescription = stringResource(id = R.string.clear_field)
                    )
                }
            }
        }
    )
}

@Composable
private fun AddDataToDBResponse(
    profileViewModel: profileViewModel,
    image: String
){
    when(val addUserToDBResponse = profileViewModel.addUserToDBResponse){
        is Response.Loading->{ProgressBar()}

        is Response.Success ->{
            LaunchedEffect(key1 = addUserToDBResponse, block = {
                if(addUserToDBResponse.data!!){
                    profileViewModel.uploadImageToStorage(image)
                }
            })
        }

        is Response.Failure->{
            Log.e("TAG","AddDataToDBResponse FAILED : ${addUserToDBResponse.e}")
        }
    }
}

@Composable
private fun UploadImageToStorageResponse(
    profileViewModel: profileViewModel,
    chatViewModel: ChatViewModel
){
    val context = LocalContext.current
    when(val uploadImageToStorageResponse = profileViewModel.uploadImageToStorageResponse){
        is Response.Loading->{ ProgressBar()}

        is Response.Success ->{
            LaunchedEffect(key1 = uploadImageToStorageResponse, block = {
                if(uploadImageToStorageResponse.data!!){
                    chatViewModel.setScreenType(ScreenType.HomeList)
                }
            })
        }

        is Response.Failure->{
            Log.e("TAG","AddDataToDBResponse FAILED : ${uploadImageToStorageResponse.e}")
            Toast.makeText(context,"Something is wrong",Toast.LENGTH_LONG).show()
        }
    }
}