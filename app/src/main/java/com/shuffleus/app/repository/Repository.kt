package com.shuffleus.app.repository

import com.shuffleus.app.data.User

interface Repository
{
    /**
     * Get user detail for given username.
     */
    fun getActiveUsers(): List<User>

    /**
     * Get list of repositories for given username.
     */
    fun getUsers(): List<User>
}