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
        private val KEY_GROUPSIZE = intPreferencesKey("groupsize")
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
     * Read last groupname from preferences
     */
    suspend fun getGroupnamesIdx(): Int = context.dataStore.data.map { preferences ->
        preferences[KEY_GROUPNAMES_IDX] ?: 0
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
}