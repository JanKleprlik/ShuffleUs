package com.shuffleus.app.repository.memory

import com.shuffleus.app.data.Group
import com.shuffleus.app.data.GroupNames
import com.shuffleus.app.data.Lecture
import com.shuffleus.app.repository.LectureRepository

class InMemoryLectureRepository :LectureRepository {

    override fun getLectures(): List<Lecture> {
        return listOf(
            Lecture(0,720,810,"la","Hladík",true,"S4","nswi140")
        )
    }

    override fun deleteLectures() {
        throw NotImplementedError("Operation: deleteLecture is not implemented on MemoryRepository.")
    }


    override fun addLecture(vararg lectures: Lecture) {
        throw NotImplementedError("Operation: addLectures is not implemented on MemoryRepository.")
    }
}