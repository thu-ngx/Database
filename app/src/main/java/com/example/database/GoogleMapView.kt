package com.example.database

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState


@Composable
fun GoogleMapView(onNavigateToUserDetails: () -> Unit) {
    val oulu = LatLng(65.010873, 25.467515)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(oulu, 10f)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        GoogleMap(
            modifier = Modifier.fillMaxSize(),
            cameraPositionState = cameraPositionState
        ) {
            Marker (
                state = MarkerState(position = oulu),
                title = "Oulu",
                snippet = "Marker in Oulu"
            )
        }

        // Back to UserDetails screen
        IconButton(onClick = onNavigateToUserDetails, modifier = Modifier.offset(16.dp)) {
            Icon(
                imageVector = Icons.Default.ArrowBackIosNew,
                contentDescription = "Switch camera"
            )
        }
    }

}