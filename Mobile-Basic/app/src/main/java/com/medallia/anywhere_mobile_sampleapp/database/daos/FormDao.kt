package com.medallia.anywhere_mobile_sampleapp.database.daos

import androidx.room.*
import com.medallia.anywhere_mobile_sampleapp.model.Form

@Dao
interface FormDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertForm(form: Form)

    @Query("SELECT * FROM form")
    suspend fun getAllForms(): List<Form>

    @Query("DELETE FROM form")
    suspend fun deleteAllForms()
}