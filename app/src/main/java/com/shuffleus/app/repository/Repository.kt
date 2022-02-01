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
    fun getActiveUsers(): List<User>

    /**
     * Get raw actuver users.
     * Their flag isActive is on.
     */
    fun getRawActiveUsers(): List<User>

    /**
     * Get all users.
     */
    fun getUsers(): List<User>

    /**
     * Get name of group.
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

    /**
     * Delete user.
     */
    fun deleteUser(user: User): Unit


    // can be deleted
    fun getGroupNamesAll(): Iterable<GroupNames>

    /**
     * Adds users into the DB.
     */
    fun addUser(vararg users: User)

    /**
     * Make users current
     * Updates wasChanged flag
     */
    fun makeUsersCurrent(): Unit
}