package com.shuffleus.app.repository.room

import android.app.Application
import android.content.Context
import com.shuffleus.app.data.Lecture
import com.shuffleus.app.repository.Repository
import com.shuffleus.app.repository.LectureRepository

class LectureRoomRepository(context: Context): LectureRepository {
    val db = AppDatabase.getInstance(context)

    override suspend fun getLectures(): List<Lecture> {
        return db.lectureDao().getAll()
    }

    override suspend fun addLecture(vararg lectures: Lecture) {
        return db.lectureDao().insertAll(*lectures)
    }

    override suspend fun deleteLectures() {
        db.lectureDao().delete()
    }
}