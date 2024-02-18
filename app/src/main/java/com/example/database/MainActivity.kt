package com.example.database

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.core.content.ContextCompat
import com.example.database.data.MainViewModel
import com.example.database.data.User
import com.example.database.notification.NotificationService
import com.example.database.notification.NotificationViewModel
import com.example.database.ui.theme.DatabaseTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel
    private val notificationViewModel by viewModels<NotificationViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = MainViewModel(application)
        // Insert the initial user if it doesn't exist
        viewModel.insertUserIfNotExists(User(id = 1, userName = "Nguyen"))


        setContent {
            DatabaseTheme {
//                val hasNotificationPermission by remember { mutableStateOf(notificationViewModel.hasNotificationPermission.value) }
                val notificationService = NotificationService(this)

                var hasNotificationPermission by remember {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        mutableStateOf(
                            ContextCompat.checkSelfPermission(
                                this, Manifest.permission.POST_NOTIFICATIONS
                            ) == PackageManager.PERMISSION_GRANTED
                        )
                    } else mutableStateOf(true)
                }

                LaunchedEffect(key1 = true ){
                    if(hasNotificationPermission){
                        notificationService.showBasicNotification()
                    }
                }
                MyAppNavHost(
                    viewModel = viewModel,
                    context = this,
                    notificationViewModel = notificationViewModel
                )
            }

        }
    }
}