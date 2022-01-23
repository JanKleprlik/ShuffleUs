package com.shuffleus.app.repository.room

import android.app.Application
import android.content.Context
import com.shuffleus.app.data.User
import com.shuffleus.app.repository.Repository

class RoomRepository(context: Context): Repository {
    val db = AppDatabase.getInstance(context)

    override fun getActiveUsers(): List<User> {
        return db.userDao().getActiveUsers()
    }

    override fun getUsers(): List<User> {
        return db.userDao().getAll()
    }

    override fun addUser(vararg users:User){
        return db.userDao().insertAll(*users)
    }

    override fun getGroupName(index: Int, groupName: String): String {
        return db.groupNamesDao().getNamesByGroupName(groupName)[index]
    }

    override fun getGroupNames(): List<String> {
        return db.groupNamesDao().getNames()
    }

}