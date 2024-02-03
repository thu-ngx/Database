package com.example.database.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import androidx.room.Upsert

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertUser(user: User)

    @Update
    fun updateUser(user: User)

    @Delete
    fun deleteUser(user: User)

    @Query("SELECT * FROM users")
    fun loadAllUsers(): List<User>

    @Query("SELECT * FROM users WHERE user_name LIKE :userName")
    fun findByName(userName: String): User

    @Query("SELECT * FROM users WHERE id LIKE :id")
    fun findById(id: Int): User
}