package com.shuffleus.app.repository

import com.shuffleus.app.data.Group
import com.shuffleus.app.data.GroupNames
import com.shuffleus.app.data.Lecture

interface LectureRepository
{
    /**
     * Get all lectures.
     */
    fun getLectures(): List<Lecture>

    /**
     * Delete all lectures.
     */
    fun deleteLectures(): Unit

    /**
     * Adds users into the DB.
     */
    fun addLecture(vararg lectures: Lecture)

}