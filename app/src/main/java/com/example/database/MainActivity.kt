package com.example.database

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.database.camera.CameraViewModel
import com.example.database.data.UserViewModel
import com.example.database.data.User
import com.example.database.notification.NotificationService
import com.example.database.notification.NotificationViewModel
import com.example.database.sensor.SensorService
import com.example.database.ui.theme.DatabaseTheme
import com.example.database.views.CameraView

class MainActivity : ComponentActivity(){
    private lateinit var userViewModel: UserViewModel
    private val notificationViewModel by viewModels<NotificationViewModel>()

    private var notificationService: NotificationService? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, SensorService::class.java))

        if(!hasCameraPermission()) {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.CAMERA), 0
            )
        }
        userViewModel = UserViewModel(application)
        // Insert the initial user if it doesn't exist
        userViewModel.insertUserIfNotExists(User(id = 1, userName = "Nguyen"))

        setContent {
            DatabaseTheme {
//                notificationService = NotificationService(this)
//
//                var hasNotificationPermission by remember {
//                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//                        mutableStateOf(
//                            ContextCompat.checkSelfPermission(
//                                this, Manifest.permission.POST_NOTIFICATIONS
//                            ) == PackageManager.PERMISSION_GRANTED
//                        )
//                    } else mutableStateOf(true)
//                }
//
//                LaunchedEffect(key1 = true ){
//                    if(hasNotificationPermission){
//                        notificationService!!.showBasicNotification()
//                    }
//                }
//                MyAppNavHost(
//                    viewModel = viewModel,
//                    context = this,
//                    notificationViewModel = notificationViewModel
//                )

                CameraView()
            }

        }
    }
    private fun hasCameraPermission() : Boolean {
        return ContextCompat.checkSelfPermission(
            this, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED
    }
}