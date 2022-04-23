package com.alwan.bangkitbpaai2.util

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class UserPreferences private constructor(private val dataStore: DataStore<Preferences>) {

    private val userKey = stringPreferencesKey("user_key")

    fun getUserKey(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[userKey] ?: ""
        }
    }

    suspend fun saveUserKey(key: String) {
        dataStore.edit { preferences ->
            preferences[userKey] = key
        }
    }

    suspend fun deleteUserKey() {
        dataStore.edit { preferences ->
            preferences.remove(userKey)
        }
    }

    companion object {
        @Volatile
        private var INSTANCE: UserPreferences? = null

        fun getInstance(dataStore: DataStore<Preferences>): UserPreferences {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreferences(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}