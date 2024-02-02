package com.example.database

import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import coil.compose.AsyncImage

@Composable
fun ImageDisplayer() {
    val imageUrl = "https://upload.wikimedia.org/wikipedia/commons/thumb/f/fa/Church_of_light.jpg/2880px-Church_of_light.jpg"
    AsyncImage(
        model = imageUrl,
        contentDescription = null,
        modifier = Modifier
            .aspectRatio(2880f/1920f)
    )
}