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
import com.example.chitchat.core.DEFAULT_USER_IMAGE
import com.example.chitchat.domain.Response
import com.example.chitchat.model.ScreenType
import com.example.chitchat.model.User
import com.example.chitchat.ui.screens.ChatViewModel
import com.example.chitchat.ui.screens.commons.ChatTopAppBar
import com.example.chitchat.ui.screens.commons.ProgressBar
import com.example.chitchat.ui.screens.signup.email.SignInButton
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


@Composable
fun SignScreen_Profile(
    modifier: Modifier = Modifier,
    chatViewModel: ChatViewModel,
    FireStoreViewModel: FireStoreViewModel = hiltViewModel(),
) {
    BackHandler {}

    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var description by remember { mutableStateOf<String?>(null) }
    var profileImage by remember { mutableStateOf<Uri?>(null) }

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

                val user = User(
                    id = Firebase.auth.currentUser?.uid!!,
                    fullName = "$firstName $lastName",
                    userImage = if(profileImage!=null)profileImage.toString() else null,
                    description = description,
                )

                FireStoreViewModel.addUserToDB(user)
            }
        )

        AddDataToDBResponse(fireStoreViewModel = FireStoreViewModel,chatViewModel=chatViewModel, image = profileImage)

        UploadImageToStorageResponse(fireStoreViewModel = FireStoreViewModel, chatViewModel = chatViewModel)

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
    setPickedImage: (Uri?) -> Unit,
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
    setPickedImage: (Uri?) -> Unit,
) {


    var selectedImage by remember { mutableStateOf<Uri?>(null) }

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = {
            selectedImage = it

            setPickedImage(selectedImage)
        }
    )


    Box(modifier.size(128.dp)) {

        AsyncImage(
            modifier = Modifier
                .size(128.dp)
                .clip(CircleShape),
            model = selectedImage ?: Uri.parse(DEFAULT_USER_IMAGE),
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
    fireStoreViewModel: FireStoreViewModel,
    chatViewModel: ChatViewModel,
    image: Uri?
){
    when(val addUserToDBResponse = fireStoreViewModel.addUserToDBResponse){
        is Response.Loading->{ProgressBar()}

        is Response.Success ->{
            LaunchedEffect(key1 = addUserToDBResponse, block = {
                if(addUserToDBResponse.data!!){
                    if(image != null){
                        fireStoreViewModel.uploadImageToStorage(image.toString())
                    }
                    else{
                        chatViewModel.setScreenType(ScreenType.HomeList)
                    }
                    fireStoreViewModel.resetAddUserToDBResponse()
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
    fireStoreViewModel: FireStoreViewModel,
    chatViewModel: ChatViewModel
){
    val context = LocalContext.current
    when(val uploadImageToStorageResponse = fireStoreViewModel.uploadImageToStorageResponse){
        is Response.Loading->{ ProgressBar()}

        is Response.Success ->{
            LaunchedEffect(key1 = uploadImageToStorageResponse, block = {
                if(uploadImageToStorageResponse.data!!){
                    chatViewModel.setScreenType(ScreenType.HomeList)
                    fireStoreViewModel.resetUploadImageToStorageResponse()
                    fireStoreViewModel.resetSetUserToDBResponse()
                }
            })
        }

        is Response.Failure->{
            Log.e("TAG","AddDataToDBResponse FAILED : ${uploadImageToStorageResponse.e}")
            Toast.makeText(context,"Something is wrong",Toast.LENGTH_LONG).show()
        }
    }
}