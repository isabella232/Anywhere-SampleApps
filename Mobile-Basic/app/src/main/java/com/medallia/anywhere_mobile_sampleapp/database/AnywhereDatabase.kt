package com.medallia.anywhere_mobile_sampleapp.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.medallia.anywhere_mobile_sampleapp.database.daos.*
import com.medallia.anywhere_mobile_sampleapp.jwt.AccessToken
import com.medallia.anywhere_mobile_sampleapp.model.*
import com.medallia.anywhere_mobile_sampleapp.utils.Converters

@TypeConverters(Converters::class)
@Database(entities = [Form::class, Configuration::class], version = 2)
abstract class AnywhereDatabase: RoomDatabase() {

    companion object {
        const val DATABASE_NAME: String = "anywhere_db"
    }

    abstract fun formDao(): FormDao
    abstract fun configurationDao(): ConfigurationDao

}