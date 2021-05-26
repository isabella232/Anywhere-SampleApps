package com.medallia.anywhere_versioning_sampleapp.model

import androidx.room.*
import com.medallia.anywhere_versioning_sampleapp.utils.Converters

@Entity(tableName = "configuration")
data class Configuration(
    @Embedded
    var configurationUUID: ConfigurationUUID? = null,

    @TypeConverters(Converters::class)
    var forms: List<Form>? = null,

    @PrimaryKey
    var propertyId: Int = 0
)