package com.applock.nfc.utils

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "app_lock_preferences")

class PreferencesManager(private val context: Context) {

    companion object {
        private val IS_LOCKED = booleanPreferencesKey("is_locked")
        private val LOCK_TIMESTAMP = longPreferencesKey("lock_timestamp")
        private val AUTO_UNLOCK_DURATION = intPreferencesKey("auto_unlock_duration")
        private val PIN_ENABLED = booleanPreferencesKey("pin_enabled")
        private val PIN_CODE = stringPreferencesKey("pin_code")
    }

    val isLocked: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[IS_LOCKED] ?: false
    }

    val lockTimestamp: Flow<Long> = context.dataStore.data.map { preferences ->
        preferences[LOCK_TIMESTAMP] ?: 0L
    }

    val autoUnlockDuration: Flow<Int> = context.dataStore.data.map { preferences ->
        preferences[AUTO_UNLOCK_DURATION] ?: 20 // Default 20 minutes
    }

    val isPinEnabled: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[PIN_ENABLED] ?: false
    }

    suspend fun setLocked(locked: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_LOCKED] = locked
            if (locked) {
                preferences[LOCK_TIMESTAMP] = System.currentTimeMillis()
            }
        }
    }

    suspend fun setAutoUnlockDuration(minutes: Int) {
        context.dataStore.edit { preferences ->
            preferences[AUTO_UNLOCK_DURATION] = minutes
        }
    }

    suspend fun setPinEnabled(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PIN_ENABLED] = enabled
        }
    }

    suspend fun setPin(pin: String) {
        context.dataStore.edit { preferences ->
            preferences[PIN_CODE] = pin
            preferences[PIN_ENABLED] = true
        }
    }

    suspend fun verifyPin(pin: String): Boolean {
        val storedPin = context.dataStore.data.map { preferences ->
            preferences[PIN_CODE]
        }
        var result = false
        storedPin.collect { stored ->
            result = stored == pin
        }
        return result
    }

    suspend fun getPin(): String? {
        var pin: String? = null
        context.dataStore.data.map { preferences ->
            preferences[PIN_CODE]
        }.collect { stored ->
            pin = stored
        }
        return pin
    }
}
