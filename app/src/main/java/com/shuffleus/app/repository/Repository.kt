package com.shuffleus.app.repository

import com.shuffleus.app.data.Group
import com.shuffleus.app.data.GroupNames
import com.shuffleus.app.data.User

interface Repository
{
    /**
     * Get active users.
     * Their fag isActive is on, and wasChanged is off.
     */
    suspend fun getActiveUsers(): List<User>

    /**
     * Get raw actuver users.
     * Their flag isActive is on.
     */
    suspend fun getRawActiveUsers(): List<User>

    /**
     * Get all users.
     */
    suspend fun getUsers(): List<User>

    /**
     * Get name of group.
     */
    suspend fun getGroupName(index: Int, groupName: String): String

    /**
     *  Get types of names of groups
     */
    suspend fun getGroupNames(): Iterable<String>

    /**
     * Update user information.
     * Such as whether it is active.
     */
    suspend fun updateUser(user: User): Unit

    /**
     * Delete user.
     */
    suspend fun deleteUser(user: User): Unit


    // can be deleted
    suspend fun getGroupNamesAll(): Iterable<GroupNames>

    /**
     * Adds users into the DB.
     */
    suspend fun addUser(vararg users: User)

    /**
     * Make users current
     * Updates wasChanged flag
     */
    suspend fun makeUsersCurrent(): Unit
}