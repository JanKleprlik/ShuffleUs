package com.shuffleus.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Lecture(
var day : Int,
var startTime : Int,
var endTime : Int,
var subject : String,
var lecturer : String,
var lection : Boolean,
var room : String,
var code : String)
{
    @PrimaryKey(autoGenerate = true)
    var lectureId: Long = 0
}