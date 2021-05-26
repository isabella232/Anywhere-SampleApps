package com.medallia.anywhere_versioning_sampleapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.medallia.anywhere_versioning_sampleapp.database.daos.ConfigurationDao
import com.medallia.anywhere_versioning_sampleapp.database.daos.FormDao
import com.medallia.anywhere_versioning_sampleapp.model.Configuration
import com.medallia.anywhere_versioning_sampleapp.model.Form
import com.medallia.anywhere_versioning_sampleapp.utils.Converters

@TypeConverters(Converters::class)
@Database(entities = [Form::class, Configuration::class], version = 2)
abstract class AnywhereDatabase: RoomDatabase() {

    companion object {
        const val DATABASE_NAME: String = "anywhere_db"
    }

    abstract fun formDao(): FormDao
    abstract fun configurationDao(): ConfigurationDao

}