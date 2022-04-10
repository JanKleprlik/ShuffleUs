package com.shuffleus.app.repository

import com.shuffleus.app.data.Lecture

interface LectureRepository
{
    /**
     * Get all lectures.
     */
    suspend fun getLectures(): List<Lecture>

    /**
     * Delete all lectures.
     */
    suspend fun deleteLectures()

    /**
     * Adds users into the DB.
     */
    suspend fun addLecture(vararg lectures: Lecture)

}