package com.shuffleus.app.repository.room

import androidx.room.*
import com.shuffleus.app.data.GroupNames
import com.shuffleus.app.data.User

@Dao
interface GroupNamesDao {
    @Query("SELECT * FROM GroupNames")
    fun getAll(): List<GroupNames>

    @Query("SELECT name FROM GroupNames")
    fun getNames(): List<String>

    @Query("SELECT names FROM GroupNames WHERE name = :name")
    fun getNamesByGroupName(name: String): List<String>

    @Insert
    fun insertAll(vararg users: GroupNames)
    
}