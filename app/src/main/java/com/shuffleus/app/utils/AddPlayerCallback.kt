package com.shuffleus.app.utils

import com.shuffleus.app.data.User

interface CallbackListener{

}

interface AddPlayerCallbackListener : CallbackListener {
    fun onPlayerAdded(player: User)
}