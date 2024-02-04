package com.example.database.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db: AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "user-database"
    ).build()

    private val userDao: UserDao = db.userDao()

    fun insertUserIfNotExists(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            // Check if the user with ID 1 already exists
            val existingUser = userDao.findById(user.id)
            if (existingUser == null) {
                // User doesn't exist, insert the user
                userDao.insertUser(user)
            }
        }
    }

    suspend fun getUserById(userId: Int): User? {
        return withContext(Dispatchers.IO) {
            userDao.findById(userId)
        }
    }

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