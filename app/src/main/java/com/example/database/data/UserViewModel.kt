package com.example.database.data

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val db: AppDatabase = Room.databaseBuilder(
        application,
        AppDatabase::class.java, "user-database"
    ).build()

    private val userDao: UserDao = db.userDao()

    private val initialUser = User(1, "Nguyen")

    val currentUser = initialUser
    fun insertUser(user: User) {
        viewModelScope.launch {
            userDao.insertUser(user)
        }
    }

    fun getCurrentUserById(userId: Int): User {
        return userDao.findById(userId)
    }

    fun updateUserName(newName: String) {
        viewModelScope.launch {
            val currentUser = userDao.findById(1)
            if (currentUser != null) {
                // Update the user's name
                currentUser.userName = newName
                userDao.updateUser(currentUser)
            }
        }
    }
}
