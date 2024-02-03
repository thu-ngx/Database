package com.example.database.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db: AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "user-database"
    ).build()

    private val userDao: UserDao = db.userDao()

    // Use Dispatchers.IO to perform database operations in the background
    fun insertUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            userDao.insertUser(user)
        }
    }

    // Use a suspending function with withContext(Dispatchers.IO)
    suspend fun getCurrentUserById(userId: Int): User {
        return withContext(Dispatchers.IO) {
            userDao.findById(userId)
        }
    }

    // Use Dispatchers.IO to perform database operations in the background
    fun updateUserName(newName: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val currentUser = userDao.findById(1)
            currentUser?.let {
                // Update the user's name
                it.userName = newName
                userDao.updateUser(it)
            }
        }
    }
}

