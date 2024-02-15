package com.example.database
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.database.data.MainViewModel
import com.example.database.data.User

class MainActivity : ComponentActivity() {
    private lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = MainViewModel(application)
        // Insert the initial user if it doesn't exist
        viewModel.insertUserIfNotExists(User(id = 1, userName = "Nguyen"))

//        val notificationService = NotificationService(applicationContext)

        setContent {
            MyAppNavHost(viewModel = viewModel)
        }
    }
}