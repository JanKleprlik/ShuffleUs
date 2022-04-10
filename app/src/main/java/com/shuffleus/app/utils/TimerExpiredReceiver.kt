package com.shuffleus.app.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.app.TaskStackBuilder
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.shuffleus.app.App
import com.shuffleus.app.R
import com.shuffleus.app.main.MainActivity
import com.shuffleus.app.main.MainFragment

class TimerExpiredReceiver : BroadcastReceiver() {

    private val notificationID = 1

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action){
            Constants.ACTION_START -> {
                fireNotification(context)

                TimerPreferences.setTimerState(MainFragment.TimerState.Stopped, context)
                TimerPreferences.setAlarmSetTime(0, context)
            }
        }
    }

    // fire notification with restart button
    private fun fireNotification(context: Context) {
        // intent for the notification
        val notificationIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val notificationPendingIntent = TaskStackBuilder.create(context).run {
            addNextIntentWithParentStack(notificationIntent)
            getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
        }


        // the notification itself
        val notification = NotificationCompat.Builder(
            context,
            App.NOTIFICATION_CHANNEL_MAIN_ID
        )
            .setSmallIcon(R.drawable.ic_timer)
            .setContentTitle(context.getString(R.string.app_name))
            .setContentText(context.getString(R.string.timer_notification))
            .setContentIntent(notificationPendingIntent)
            .setAutoCancel(true)
            .build()

        // launch notification
        with(NotificationManagerCompat.from(context)) {
            notify(notificationID, notification)
        }
    }
}