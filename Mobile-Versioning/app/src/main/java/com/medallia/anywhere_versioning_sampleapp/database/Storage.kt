package com.medallia.anywhere_versioning_sampleapp.database

import android.content.Context
import androidx.datastore.DataStore
import androidx.datastore.preferences.*
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton


const val SHARED_PREFS_KEY = "com.medallia.anywhere_mobile_sampleapp.SHARED_PREFS_KEY"

@Singleton
class Storage @Inject constructor(@ApplicationContext context: Context) {

    private val dataStore: DataStore<Preferences> = context.createDataStore(
        SHARED_PREFS_KEY
    )

    companion object PreferencesKeys {
        val API_TOKEN = preferencesKey<String>("API_TOKEN")
        val ACCESS_TOKEN = preferencesKey<String>("ACCESS_TOKEN")
        val UUID = preferencesKey<String>("UUID")
        val UUID_URL = preferencesKey<String>("UUID_URL")
        val FIRST_INIT = preferencesKey<Boolean>("FIRST_INIT")
    }

    suspend fun putString(value: String, key: Preferences.Key<String>) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    suspend fun putBoolean(value: Boolean, key: Preferences.Key<Boolean>) {
        dataStore.edit { preferences ->
            preferences[key] = value
        }
    }


    fun getString(key: Preferences.Key<String>): Flow<String> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e(exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw  exception
            }
        }
        .map { preferences ->
            preferences[key] ?: ""
        }

    fun getBoolean(key: Preferences.Key<Boolean>): Flow<Boolean> = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                Timber.e(exception.message.toString())
                emit(emptyPreferences())
            } else {
                throw  exception
            }
        }
        .map { preferences ->
            preferences[key] ?: false
        }
}