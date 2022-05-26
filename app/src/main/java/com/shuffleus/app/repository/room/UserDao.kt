package com.shuffleus.app.repository.room

import androidx.room.*
import com.shuffleus.app.data.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    suspend fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE (isActive = 1 AND wasChanged = 0) OR (wasChanged = 1 AND wasActive = 1)")
    suspend fun getActiveUsers(): List<User>

    @Query("SELECT * FROM user WHERE isActive = 1")
    suspend fun getRawActiveUsers(): List<User>

    @Update
    suspend fun updateUsers(vararg users: User)

    @Insert
    suspend fun insertAll(vararg users: User)

    @Delete
    suspend fun delete(user: User)

}