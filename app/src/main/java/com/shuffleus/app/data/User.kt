package com.shuffleus.app.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    val name: String,
    val surname: String,
    var isActive: Boolean)
{
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var userId: Long = 0
}