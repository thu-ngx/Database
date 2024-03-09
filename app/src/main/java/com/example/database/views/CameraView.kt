@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.database.views

import android.graphics.Bitmap
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture.OnImageCapturedCallback
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.view.CameraController
import androidx.camera.view.LifecycleCameraController
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Cameraswitch
import androidx.compose.material.icons.filled.Photo
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.database.camera.CameraPreview
import com.example.database.camera.CameraViewModel
import com.example.database.camera.PhotoBottomSheetContent
import kotlinx.coroutines.launch

@Composable
fun CameraView(
) {
    val scope = rememberCoroutineScope()
    val scaffoldState = rememberBottomSheetScaffoldState()
    val context = LocalContext.current
    val controller = remember {
        LifecycleCameraController(context).apply {
            setEnabledUseCases(
                CameraController.IMAGE_CAPTURE
            )
        }
    }

    val cameraVM = viewModel<CameraViewModel>()
    val bitmaps by cameraVM.bitmaps.collectAsState()

    fun takePhoto(controller: LifecycleCameraController, onPhotoTaken: (Bitmap) -> Unit) {
        controller.takePicture(
            ContextCompat.getMainExecutor(context),
            object : OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)
                    onPhotoTaken(image.toBitmap())
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }
            })
    }

    BottomSheetScaffold(scaffoldState = scaffoldState, sheetPeekHeight = 0.dp, sheetContent = {
        PhotoBottomSheetContent(
            bitmaps = bitmaps, modifier = Modifier.fillMaxWidth()
        )
    }) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            CameraPreview(controller = controller, modifier = Modifier.fillMaxSize())

            // Back to UserDetails screen
            IconButton(onClick = {

            }, modifier = Modifier.offset(16.dp)) {
                Icon(
                    imageVector = Icons.Default.ArrowBackIosNew,
                    contentDescription = "Switch camera"
                )
            }

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                // Open gallery button
                IconButton(onClick = {
                    scope.launch { scaffoldState.bottomSheetState.expand() }
                }) {
                    Icon(
                        imageVector = Icons.Default.Photo, contentDescription = "Open gallery"
                    )
                }

                // Take photo button
                IconButton(onClick = {
                    takePhoto(controller = controller, onPhotoTaken = cameraVM::onTakePhoto)
                }) {
                    Icon(
                        imageVector = Icons.Default.PhotoCamera, contentDescription = "Take photo"
                    )
                }

                // Switch camera button
                IconButton(onClick = {
                    controller.cameraSelector =
                        if (controller.cameraSelector == CameraSelector.DEFAULT_BACK_CAMERA) {
                            CameraSelector.DEFAULT_FRONT_CAMERA
                        } else CameraSelector.DEFAULT_BACK_CAMERA
                }) {
                    Icon(
                        imageVector = Icons.Default.Cameraswitch,
                        contentDescription = "Switch camera"
                    )
                }
            }
        }
    }
}