package com.example.database

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
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

class MainActivity : ComponentActivity(){
    private lateinit var viewModel: MainViewModel
    private val notificationViewModel by viewModels<NotificationViewModel>()

//    private lateinit var sensorManager: SensorManager
//    private lateinit var gyroscopeSensor: Sensor

    private var notificationService: NotificationService? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, SensorService::class.java))

        viewModel = MainViewModel(application)
        // Insert the initial user if it doesn't exist
        viewModel.insertUserIfNotExists(User(id = 1, userName = "Nguyen"))

//        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
//        gyroscopeSensor = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE)!!

        setContent {
            DatabaseTheme {
                notificationService = NotificationService(this)

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
                        notificationService!!.showBasicNotification()
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
//
//    override fun onResume() {
//        super.onResume()
//        // Register the gyroscope sensor listener
//        sensorManager.registerListener(this, gyroscopeSensor, SensorManager.SENSOR_DELAY_NORMAL)
//    }
//
//    override fun onPause() {
//        super.onPause()
//        // Unregister the sensor listener to prevent battery drain when the app is in the background
//        sensorManager.unregisterListener(this)
//    }
//
//    override fun onSensorChanged(event: SensorEvent?) {
//        if (event?.sensor?.type == Sensor.TYPE_GYROSCOPE) {
//            // Check if the change in the y-direction is significant
//            val deltaY = event.values[1]
//            if (deltaY > THRESHOLD_Y_DIRECTION_CHANGE) {
//                // Trigger the notification
//                notificationService?.showRotatingNotification()
//            }
//        }
//    }
//
//    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
//        // No implementation needed
//    }
//
//    companion object {
//        // Define a threshold value for significant change in the y-direction
//        private const val THRESHOLD_Y_DIRECTION_CHANGE = 0.5f
//    }
}