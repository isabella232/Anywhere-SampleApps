package com.medallia.anywhere_mobile_sampleapp.database.daos

import androidx.room.*
import com.medallia.anywhere_mobile_sampleapp.model.Configuration

@Dao
interface ConfigurationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(configuration: Configuration)

    @Query("SELECT * FROM configuration")
    suspend fun get(): Configuration

}