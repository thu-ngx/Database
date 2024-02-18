package com.example.database

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.example.database.data.MainViewModel
import com.example.database.data.User
import com.example.database.ui.theme.DatabaseTheme

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = MainViewModel(application)
        // Insert the initial user if it doesn't exist
        viewModel.insertUserIfNotExists(User(id = 1, userName = "Nguyen"))

        setContent {
            DatabaseTheme {
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

                LaunchedEffect(hasNotificationPermission) {
                    if (hasNotificationPermission) {
                        notificationService.showBasicNotification()
                    }
                }

                val permissionLauncher = rememberLauncherForActivityResult(
                    contract = ActivityResultContracts.RequestPermission(),
                    onResult = { isGranted ->
                        if (isGranted) {
                            notificationService.showPermissionEnabledNotification()
                        }
                        hasNotificationPermission = isGranted
                    }
                )

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
                        }
                    }) {
                        Text("Request permission")
                    }
                    Button(onClick = {
                        if (hasNotificationPermission) {
                            notificationService.showBasicNotification()
                        }
                    }) {
                        Text("Show notification")
                    }
                }

//                MyAppNavHost(viewModel = viewModel, context = this)
            }

        }
    }
}