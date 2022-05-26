package com.shuffleus.app.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class GroupNames (
    val name: String,
    val names: List<String>,
    )
{
    @PrimaryKey(autoGenerate = true)
    var groupNameId: Long = 0
}