package com.shuffleus.app.data

import androidx.room.Entity

@Entity(primaryKeys = ["groupId", "userId"])
data class UserGroups(
    val groupId: Long,
    val userId: Long)