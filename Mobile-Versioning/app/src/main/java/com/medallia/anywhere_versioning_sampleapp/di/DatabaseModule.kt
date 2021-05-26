package com.medallia.anywhere_versioning_sampleapp.di

import android.content.Context
import androidx.room.Room
import com.medallia.anywhere_versioning_sampleapp.database.AnywhereDatabase
import com.medallia.anywhere_versioning_sampleapp.database.daos.ConfigurationDao
import com.medallia.anywhere_versioning_sampleapp.database.daos.FormDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@InstallIn(ApplicationComponent::class)
@Module
object DatabaseModule {

    @Singleton
    @Provides
    fun provideDb(@ApplicationContext context: Context): AnywhereDatabase {
        return Room
            .databaseBuilder(
                context,
                AnywhereDatabase::class.java,
                AnywhereDatabase.DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideFormDao(anywhereDatabase: AnywhereDatabase) : FormDao {
        return anywhereDatabase.formDao()
    }

    @Singleton
    @Provides
    fun provideConfigurationDao(anywhereDatabase: AnywhereDatabase) : ConfigurationDao {
        return anywhereDatabase.configurationDao()
    }
}