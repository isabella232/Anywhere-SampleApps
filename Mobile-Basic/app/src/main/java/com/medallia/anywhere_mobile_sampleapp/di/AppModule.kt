package com.medallia.anywhere_mobile_sampleapp.di

import android.content.Context
import android.content.SharedPreferences
import androidx.datastore.DataStore
import androidx.datastore.preferences.Preferences
import androidx.datastore.preferences.createDataStore
import com.medallia.anywhere_mobile_sampleapp.database.SHARED_PREFS_KEY

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideSharedPreferences(@ApplicationContext context: Context): SharedPreferences {
        return context.getSharedPreferences(SHARED_PREFS_KEY, Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideDateStore(@ApplicationContext context: Context) : DataStore<Preferences> {
        return context.createDataStore(
            name = SHARED_PREFS_KEY
        )
    }
}