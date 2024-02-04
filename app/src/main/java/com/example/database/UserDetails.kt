package com.example.database

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.AsyncImage
import com.example.database.data.MainViewModel


@Composable
fun UserDetails(onNavigateToMessagesList: () -> Unit, viewModel: MainViewModel, context: Context) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        NavigationBar(onNavigateToMessagesList)
        Spacer(modifier = Modifier.height(80.dp))
        UserInfo(viewModel, context)
    }
}


@Composable
fun UserInfo(viewModel: MainViewModel, context: Context) {
    var userName by remember { mutableStateOf("") }

    val currentUserId = 1

    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    LaunchedEffect(currentUserId) {
        // Fetch the current user initially
        val currentUser = viewModel.getUserById(currentUserId)
        if (currentUser != null) {
            userName = currentUser.userName ?: ""
        }

        // Load the saved image Uri from app-specific storage
        selectedImageUri = loadSavedImageUri(context)
    }

    // Registers a photo picker activity launcher in single-select mode.
    val singlePhotoPickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri: Uri? ->
            uri?.let {
                selectedImageUri = it
                saveImageToInternalStorage(context, it, viewModel)
            }
        }
    )

    // Launch the photo picker and let the user choose only images.
    fun pickPhoto() {
        singlePhotoPickLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        AsyncImage(
            model = selectedImageUri,
            contentDescription = null,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.secondary, CircleShape)
                .clickable(onClick = {
                    pickPhoto()
                }),
            contentScale = ContentScale.FillBounds
        )

        Spacer(modifier = Modifier.width(20.dp))

        TextField(
            value = userName,
            onValueChange = {
                userName = it
            },
            modifier = Modifier.padding(vertical = 30.dp)
        )

        Button(
            onClick = {
                // Update the user's name in the database
                viewModel.updateUserName(userName)
            },
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text("Save")
        }
    }
}


@Composable
fun NavigationBar(onNavigateToMessagesList: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp), contentAlignment = Alignment.CenterStart
    ) {
        BackButton(onNavigateToMessagesList)
    }
}

@Composable
fun BackButton(onNavigateToMessagesList: () -> Unit) {
    Button(
        onClick = onNavigateToMessagesList,
        modifier = Modifier.width(100.dp),
    ) {
        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
    }
}

fun saveImageToInternalStorage(context: Context, uri: Uri, viewModel: MainViewModel): Uri? {
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = context.openFileOutput("image.jpg", Context.MODE_PRIVATE)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        // Get the Uri of the saved image file
        val savedImageUri = Uri.fromFile(context.getFileStreamPath("image.jpg"))

        return savedImageUri
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}


fun loadSavedImageUri(context: Context): Uri? {
    return Uri.fromFile(context.getFileStreamPath("image.jpg"))
}


//@Preview
//@Composable
//fun Preview() {
//    DatabaseTheme {
//        UserDetails({})
//    }
//}