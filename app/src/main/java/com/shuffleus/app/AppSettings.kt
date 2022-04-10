package com.shuffleus.app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.shuffleus.app.AppSettings.Companion.APP_PREFERENCES
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(APP_PREFERENCES)


class AppSettings(private val context: Context) {

    companion object {
        const val APP_PREFERENCES = "shuffleus_client"
        private val KEY_GROUPNAMES_IDX = intPreferencesKey("groupnames")
        private val KEY_OLD_GROUPNAMES_IDX = intPreferencesKey("old_groupnames")
        private val KEY_GROUPSIZE = intPreferencesKey("groupsize")
        private val KEY_OLD_GROUPSIZE = intPreferencesKey("old_groupsize")
        private val KEY_SEED = intPreferencesKey("seed")
        private val KEY_OLD_SEED = intPreferencesKey("old_seed")
        private val KEY_TIMER_IDX = intPreferencesKey("timer_idx")
        private val KEY_TIMER_LENGTH = intPreferencesKey("timer_length")
        private val KEY_TIMER_PREVIOUS_LENGTH = longPreferencesKey("timer_previous_length")
        private val KEY_TIMER_STATE = intPreferencesKey("timer_state")
        private val KEY_SECONDS_REMAINING = longPreferencesKey("seconds_remaining")
        private val KEY_ALARM_TIME = longPreferencesKey("alarm_time")
    }


    /**
     * Read timer state
     */
    suspend fun getAlarmSetTime(): Long = context.dataStore.data.map { preferences ->
        preferences[KEY_ALARM_TIME] ?: 0
    }.first()

    /**
     * Set timer state
     */
    suspend fun setAlarmSetTime(value: Long) {
        context.dataStore.edit { preferences ->
            preferences[KEY_ALARM_TIME] = value
        }
    }
    /**
     * Read remaining seconds
     */
    suspend fun getSecondsRemaining(): Long = context.dataStore.data.map { preferences ->
        preferences[KEY_SECONDS_REMAINING] ?: 1
    }.first()

    /**
     * Set remaining seconds
     */
    suspend fun setSecondsRemaining(value: Long) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SECONDS_REMAINING] = value
        }
    }
    /**
     * Read timer state
     */
    suspend fun getTimerState(): Int = context.dataStore.data.map { preferences ->
        preferences[KEY_TIMER_STATE] ?: 1
    }.first()

    /**
     * Set timer state
     */
    suspend fun setTimerState(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TIMER_STATE] = value
        }
    }

    /**
     * Read timer length
     */
    suspend fun getTimerLength(): Int = context.dataStore.data.map { preferences ->
        preferences[KEY_TIMER_LENGTH] ?: 1
    }.first()

    /**
     * Set timer length
     */
    suspend fun setTimerLength(value: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TIMER_LENGTH] = value
        }
    }

    /**
     * Read timer previous length
     */
    suspend fun getPreviousTimerLengthSeconds(): Long = context.dataStore.data.map { preferences ->
        preferences[KEY_TIMER_PREVIOUS_LENGTH] ?: 1
    }.first()

    /**
     * Set timer previous length
     */
    suspend fun setPreviousTimerLengthSeconds(value: Long) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TIMER_PREVIOUS_LENGTH] = value
        }
    }

    /**
     * Read groupname from preferences
     */
    suspend fun getGroupnamesIdx(): Int = context.dataStore.data.map { preferences ->
        preferences[KEY_GROUPNAMES_IDX] ?: 1
    }.first()

    /**
     * Set groupname to preferences
     */
    suspend fun setGroupnamesIdx(groupnameIdx: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_GROUPNAMES_IDX] = groupnameIdx
        }
    }

    /**
     * Read last groupname from preferences
     * Returns -1 if never used
     */
    suspend fun getOldGroupnamesIdx(): Int = context.dataStore.data.map { preferences ->
        preferences[KEY_OLD_GROUPNAMES_IDX] ?: -1
    }.first()

    /**
     * Update last groupname in preferences
     */
    suspend fun updateGroupnamesIdx() {
        context.dataStore.edit { preferences ->
            preferences[KEY_OLD_GROUPNAMES_IDX] = getGroupnamesIdx()
        }
    }


    /**
     * Set group size to preferences
     */
    suspend fun getGroupSize() = context.dataStore.data.map { preferences ->
        preferences[KEY_GROUPSIZE] ?: 2
    }.first()

    /**
     * Set group size to preferences
     */
    suspend fun setGroupSize(size: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_GROUPSIZE] = size
        }
    }

    /**
     * Get last group size to preferences
     * Returns -1 if never used
     */
    suspend fun getOldGroupSize() = context.dataStore.data.map { preferences ->
        preferences[KEY_OLD_GROUPSIZE] ?: -1
    }.first()

    /**
     * Update last group size in preferences
     */
    suspend fun updateOldGroupSize() {
        context.dataStore.edit { preferences ->
            preferences[KEY_OLD_GROUPSIZE] = getGroupSize()
        }
    }

    /**
     * Get seed
     */
    private suspend fun getSeed() = context.dataStore.data.map { preferences ->
        preferences[KEY_SEED] ?: 1
    }.first()

    /**
     * Set seed
     */
    suspend fun incrementSeed() {
        context.dataStore.edit { preferences ->
            val seed = preferences[KEY_SEED] ?: 0
            preferences[KEY_SEED] = seed + 1
        }
    }

    /**
     * Get last seed
     * Returns -1 if never used
     */
    suspend fun getOldSeed() = context.dataStore.data.map { preferences ->
        preferences[KEY_OLD_SEED] ?: -1
    }.first()

    /**
     * Update last seed
     */
    suspend fun updateOldSeed() {
        context.dataStore.edit { preferences ->
            preferences[KEY_OLD_SEED] = getSeed()
        }
    }

    /**
     * Get timer index to determine time interval
     */
    suspend fun getTimerIdx() = context.dataStore.data.map { preferences ->
        preferences[KEY_TIMER_IDX] ?: 1
    }.first()

    /**
     * Set timer index to determine time interval
     */
    suspend fun setTimerIdx(newIdx: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TIMER_IDX] = newIdx
        }
    }
}