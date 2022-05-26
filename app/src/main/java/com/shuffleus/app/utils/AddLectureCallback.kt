package com.shuffleus.app.utils

import com.shuffleus.app.data.Lecture


interface LectureCallbackListener : CallbackListener {
    fun onLectureAdded(newLecture: Lecture)
    fun onLecturesDeleted()
}