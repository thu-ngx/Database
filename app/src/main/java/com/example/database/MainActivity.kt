package com.example.database

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.database.data.MainViewModel
import com.example.database.data.User
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
                val notificationService = NotificationService(this)

                // Launch the basic notification when the app is launched if permission is granted
                LaunchedEffect(notificationViewModel.hasNotificationPermission.value) {
                    if (notificationViewModel.hasNotificationPermission.value) {
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