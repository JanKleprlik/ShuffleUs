package com.shuffleus.app.repository

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
}