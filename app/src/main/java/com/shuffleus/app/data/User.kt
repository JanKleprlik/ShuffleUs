package com.shuffleus.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val name: String,
    val surname: String,
    var isActive: Boolean,
    var wasChanged: Boolean = false,
    var wasActive: Boolean = false)
{
    @PrimaryKey(autoGenerate = true)
    var userId: Long = 0
}