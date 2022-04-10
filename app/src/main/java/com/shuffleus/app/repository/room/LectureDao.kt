package com.shuffleus.app.repository.room

import androidx.room.*
import com.shuffleus.app.data.Lecture
import com.shuffleus.app.data.User

@Dao
interface LectureDao {
    @Query("SELECT * FROM lecture")
    suspend fun getAll(): List<Lecture>

    @Insert
    suspend fun insertAll(vararg lectures: Lecture)

    @Query("DELETE FROM lecture")
    suspend fun delete()

}