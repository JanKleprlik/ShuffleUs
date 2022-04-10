package com.shuffleus.app.repository.room

import androidx.room.*
import com.shuffleus.app.data.GroupNames
import com.shuffleus.app.data.User

@Dao
interface GroupNamesDao {
    @Query("SELECT * FROM GroupNames")
    suspend fun getAll(): List<GroupNames>

    @Query("SELECT name FROM GroupNames")
    suspend fun getNames(): List<String>

    @Query("SELECT names FROM GroupNames WHERE name = :name")
    suspend fun getNamesByGroupName(name: String): String

    @Insert
    suspend fun insertAll(vararg groupNames: GroupNames)
    
}