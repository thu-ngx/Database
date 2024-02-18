package com.example.database

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NotificationApp : Application() {
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NotificationService.CHANNEL_ID,
                "Test channel",
                NotificationManager.IMPORTANCE_HIGH
            ).apply { description = "Used for implementing notification feature" }

            // Register the channel with the system.
            val notificationManager: NotificationManager =
                getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}