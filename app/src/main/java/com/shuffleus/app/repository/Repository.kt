package com.shuffleus.app.repository

import com.shuffleus.app.data.GroupNames
import com.shuffleus.app.data.User

interface Repository
{
    /**
     * Get active users.
     */
    fun getActiveUsers(): List<User>

    /**
     * Get all users.
     */
    fun getUsers(): List<User>

    /**
     * Get name of group
     */
    fun getGroupName(index: Int, groupName: String): String

    /**
     *  Get types of names of groups
     */
    fun getGroupNames(): Iterable<String>

    /**
     * Update user information.
     * Such as whether it is active.
     */
    fun updateUser(user: User): Unit

    // can be deleted
    fun getGroupNamesAll(): Iterable<GroupNames>

    fun addUser(vararg users: User)
}