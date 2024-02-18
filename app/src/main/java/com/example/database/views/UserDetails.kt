package com.example.database.views

import android.Manifest
import android.content.Context
import android.net.Uri
import android.os.Build
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
import coil.compose.AsyncImage
import com.example.database.NotificationService
import com.example.database.NotificationViewModel
import com.example.database.data.MainViewModel


@Composable
fun UserDetails(
    onNavigateToMessagesList: () -> Unit,
    viewModel: MainViewModel,
    context: Context,
    notificationViewModel: NotificationViewModel
) {
    val notificationService = NotificationService(context)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(20.dp)
    ) {
        NavigationBar(onNavigateToMessagesList)
        Spacer(modifier = Modifier.height(80.dp))
        UserInfo(viewModel, context)
        // Only show the button if the permission is not granted
        if (!notificationViewModel.hasNotificationPermission.value) {
            EnableNotificationButton(notificationService, notificationViewModel)
        }
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
                saveImageToInternalStorage(context, it)
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
fun EnableNotificationButton(
    notificationService: NotificationService,
    notificationViewModel: NotificationViewModel
) {
    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            notificationViewModel.setNotificationPermission(true)
            notificationService.showPermissionEnabledNotification()
        }
    }

    Button(
        onClick = {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        },
        modifier = Modifier.padding(top = 16.dp)
    ) {
        Text("Enable notifications")
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

fun saveImageToInternalStorage(context: Context, uri: Uri): Uri? {
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = context.openFileOutput("image.jpg", Context.MODE_PRIVATE)

        inputStream?.use { input ->
            outputStream.use { output ->
                input.copyTo(output)
            }
        }

        // Get the Uri of the saved image file
        return Uri.fromFile(context.getFileStreamPath("image.jpg"))
    } catch (e: Exception) {
        e.printStackTrace()
        return null
    }
}


fun loadSavedImageUri(context: Context): Uri? {
    return Uri.fromFile(context.getFileStreamPath("image.jpg"))
}