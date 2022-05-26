package com.shuffleus.app.repository.room

import android.app.Application
import com.shuffleus.app.data.GroupNames
import com.shuffleus.app.data.User
import com.shuffleus.app.repository.Repository

class RoomRepository(app: Application): Repository {
    val db = AppDatabase.getInstance(app.baseContext)

    override suspend fun getActiveUsers(): List<User> {
        return db.userDao().getActiveUsers()
    }

    override suspend fun getRawActiveUsers(): List<User> {
        return db.userDao().getRawActiveUsers()
    }

    override suspend fun getUsers(): List<User> {
        return db.userDao().getAll()
    }

    override suspend fun addUser(vararg users:User){
        return db.userDao().insertAll(users = users)
    }

    override suspend fun makeUsersCurrent() {
        val users = db.userDao().getAll()
        users.forEach{
            it.wasChanged = false
            it.wasActive = false
        }
        db.userDao().updateUsers(*users.toTypedArray())
    }

    override suspend fun getGroupName(index: Int, groupName: String): String {
        return db.groupNamesDao().getNamesByGroupName(groupName).split(',')[index]
    }

    override suspend fun getGroupNamesAll(): List<GroupNames>{
        return db.groupNamesDao().getAll()
    }

    override suspend fun getGroupNames(): List<String> {
        return db.groupNamesDao().getNames()
    }

    override suspend fun updateUser(user: User) {
        db.userDao().updateUsers(user)
    }

    override suspend fun deleteUser(user: User) {
        db.userDao().delete(user)
    }

}