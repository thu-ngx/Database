package com.example.database

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun ImageDisplayer() {
    val imageUrl =
        "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Church_of_light.jpg/2880px-Church_of_light.jpg"
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .aspectRatio(2880f / 1920f)
    )
}

@Composable
fun PhotoPicker() {
    var selectedImageUri by remember {
        mutableStateOf<Uri?>(null)
    }

    // Registers a photo picker activity launcher in single-select mode.
    val singlePhotoPickLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { uri -> selectedImageUri = uri }
    )

    // Launch the photo picker and let the user choose only images.
    fun pickPhoto() {
        singlePhotoPickLauncher.launch(
            PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
        )
    }

    Column {
        Button(
            onClick = { pickPhoto() }, modifier = Modifier
                .fillMaxWidth(0.5f)
        ) {
            Text(text = "Pick one photo")
        }

        Spacer(modifier = Modifier.height(20.dp))

        AsyncImage(
            model = selectedImageUri,
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.Crop
        )
    }
}