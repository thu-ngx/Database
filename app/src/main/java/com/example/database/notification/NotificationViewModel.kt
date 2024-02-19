package com.example.database.notification

import android.os.Build
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel

class NotificationViewModel : ViewModel() {
    private val _hasNotificationPermission = mutableStateOf(false)
    val hasNotificationPermission: State<Boolean> = _hasNotificationPermission

    fun setNotificationPermission(permission: Boolean) {
        _hasNotificationPermission.value = permission
    }
}
