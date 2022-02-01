package com.shuffleus.app

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.shuffleus.app.AppSettings.Companion.APP_PREFERENCES
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(APP_PREFERENCES)


class AppSettings(private val context: Context) {

    companion object {
        const val APP_PREFERENCES = "shuffleus_client"
        private val KEY_APP_LAUNCH_COUNT = intPreferencesKey("app_launches")
        private val KEY_GROUPNAMES_IDX = intPreferencesKey("groupnames")
        private val KEY_OLD_GROUPNAMES_IDX = intPreferencesKey("old_groupnames")
        private val KEY_GROUPSIZE = intPreferencesKey("groupsize")
        private val KEY_OLD_GROUPSIZE = intPreferencesKey("old_groupsize")
        private val KEY_SEED = intPreferencesKey("seed")
        private val KEY_OLD_SEED = intPreferencesKey("old_seed")
        private val KEY_TIMER_IDX = intPreferencesKey("timer_idx")

    }


    /**
     * Get how many times the application was launched.
     */
    suspend fun getAppLaunchesCount() = context.dataStore.data.map { preferences ->
        preferences[KEY_APP_LAUNCH_COUNT] ?: 0
    }.first()

    /**
     * Increase app launched counter.
     */
    suspend fun increaseAppLaunchesCount() {
        context.dataStore.edit { preferences ->
            val appLaunchCount = preferences[KEY_APP_LAUNCH_COUNT] ?: 0
            preferences[KEY_APP_LAUNCH_COUNT] = appLaunchCount + 1
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
    suspend fun getSeed() = context.dataStore.data.map { preferences ->
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