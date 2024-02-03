package com.example.database
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.remember
import androidx.room.Room
import com.example.database.data.AppDatabase
import com.example.database.data.MainViewModel
import com.example.database.data.User

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModel: MainViewModel = MainViewModel(application)
        setContent {
            MyAppNavHost(viewModel = viewModel)
        }
    }
}