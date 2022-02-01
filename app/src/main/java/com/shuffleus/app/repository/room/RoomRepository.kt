package com.shuffleus.app.repository.room

import android.app.Application
import android.content.Context
import com.shuffleus.app.data.Group
import com.shuffleus.app.data.GroupNames
import com.shuffleus.app.data.User
import com.shuffleus.app.repository.Repository

class RoomRepository(context: Context): Repository {
    val db = AppDatabase.getInstance(context)

    override fun getActiveUsers(): List<User> {
        return db.userDao().getActiveUsers()
    }

    override fun getRawActiveUsers(): List<User> {
        return db.userDao().getRawActiveUsers()
    }

    override fun getUsers(): List<User> {
        return db.userDao().getAll()
    }

    override fun addUser(vararg users:User){
        return db.userDao().insertAll(*users)
    }

    override fun makeUsersCurrent() {
        val users = db.userDao().getAll()
        users.forEach{
            it.wasChanged = false
            it.wasActive = false
        }
        db.userDao().updateUsers(*users.toTypedArray())
    }

    override fun getGroupName(index: Int, groupName: String): String {
        return db.groupNamesDao().getNamesByGroupName(groupName).split(',')[index]
    }

    override fun getGroupNamesAll(): List<GroupNames>{
        return db.groupNamesDao().getAll()
    }

    override fun getGroupNames(): List<String> {
        return db.groupNamesDao().getNames()
    }

    override fun updateUser(user: User) {
        db.userDao().updateUsers(user)
    }

    override fun deleteUser(user: User) {
        db.userDao().delete(user)
    }

}