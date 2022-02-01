package com.shuffleus.app.utils

import android.content.Context
import androidx.preference.PreferenceManager
import com.shuffleus.app.main.MainFragment

class PrefUtil {
    companion object {

        /*
         * Current time set on timer.
         */
        private const val TIMER_LENGTH_ID = "com.shuffleus.app.timer_length"

        fun getTimerLength(context: Context): Int{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getInt(TIMER_LENGTH_ID, 5)
        }

        fun setTimerLength(minutes: Int, context:Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putInt(TIMER_LENGTH_ID, minutes)
            editor.apply()
        }


        /*
         * Last set time on the timer.
         */
        private const val PREVIOUS_TIMER_LENGTH_SECONDS_ID = "com.shuffleus.app.previous_timer_length_seconds"

        fun getPreviousTimerLengthSeconds(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, 0)
        }

        fun setPreviousTimerLengthSeconds(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(PREVIOUS_TIMER_LENGTH_SECONDS_ID, seconds)
            editor.apply()
        }

        /*
         * Current state of the timer.
         *   -> running
         *   -> stopped
         *   -> paused (currently not used)
         */
        private const val TIMER_STATE_ID = "com.shuffleus.app.timer_state"

        fun getTimerState(context: Context): MainFragment.TimerState{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            val ordinal = preferences.getInt(TIMER_STATE_ID, 0)
            return MainFragment.TimerState.values()[ordinal]
        }

        fun setTimerState(state: MainFragment.TimerState, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            val ordinal = state.ordinal
            editor.putInt(TIMER_STATE_ID, ordinal)
            editor.apply()
        }


        /*
         * Remaining seconds on the timer.
         */
        private const val SECONDS_REMAINING_ID = "com.shuffleus.app.seconds_remaining"

        fun getSecondsRemaining(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return preferences.getLong(SECONDS_REMAINING_ID, 0)
        }

        fun setSecondsRemaining(seconds: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(SECONDS_REMAINING_ID, seconds)
            editor.apply()
        }


        /*
         * Time of the alarm.
         */
        private const val ALARM_SET_TIME_ID = "com.shuffleus.app.backgrounded_time"

        fun getAlarmSetTime(context: Context): Long{
            val preferences = PreferenceManager.getDefaultSharedPreferences(context)
            return  preferences.getLong(ALARM_SET_TIME_ID, 0)
        }

        fun setAlarmSetTime(time: Long, context: Context){
            val editor = PreferenceManager.getDefaultSharedPreferences(context).edit()
            editor.putLong(ALARM_SET_TIME_ID, time)
            editor.apply()
        }
    }
}