package com.shuffleus.app.repository.room

import androidx.room.*
import com.shuffleus.app.data.User

@Dao
interface UserDao {
    @Query("SELECT * FROM user")
    fun getAll(): List<User>

    @Query("SELECT * FROM user WHERE (isActive = 1 AND wasChanged = 0) OR (wasChanged = 1 AND wasActive = 1)")
    fun getActiveUsers(): List<User>

    @Query("SELECT * FROM user WHERE isActive = 1")
    fun getRawActiveUsers(): List<User>

    @Update
    fun updateUsers(vararg users: User)

    @Insert
    fun insertAll(vararg users: User)

    @Delete
    fun delete(user: User)

}