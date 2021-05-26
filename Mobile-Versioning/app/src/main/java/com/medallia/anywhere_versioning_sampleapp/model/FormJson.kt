package com.medallia.anywhere_versioning_sampleapp.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.medallia.anywhere_versioning_sampleapp.utils.Converters
import java.io.Serializable

data class FormJson(
    @SerializedName("creationDate")
    var creationDate: String = "",

    @SerializedName("id")
    @ColumnInfo(name = "formJsonId")
    var id: String = "",

    @SerializedName("name")
    @ColumnInfo(name = "formJsonName")
    var name: String = "",

    @SerializedName("pages")
    @TypeConverters(Converters::class)
    var pages: List<Page>? = null,

    @SerializedName("settings")
    @Embedded
    var settings: Settings? = null
): Serializable