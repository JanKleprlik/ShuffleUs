package com.shuffleus.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import androidx.core.app.NotificationManagerCompat

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_MAIN_ID,
                NOTIFICATION_CHANNEL_MAIN_DESCRIPTION,
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = NOTIFICATION_CHANNEL_MAIN_DESCRIPTION
            }
            NotificationManagerCompat.from(this).createNotificationChannel(channel)
        }
    }

    companion object {
        const val NOTIFICATION_CHANNEL_MAIN_ID = "channel_main"
        const val NOTIFICATION_CHANNEL_MAIN_DESCRIPTION = "Main channel"
    }
}