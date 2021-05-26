package com.medallia.anywhere_versioning_sampleapp.model

import androidx.room.ColumnInfo
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName
import com.medallia.anywhere_versioning_sampleapp.utils.Converters
import java.io.Serializable

data class Page(
    @TypeConverters(Converters::class)
    @SerializedName("dynamicData")
    var dynamicData: List<DynamicData>? = null,

    @SerializedName("name")
    @ColumnInfo(name = "pageName")
    var name: String = ""
): Serializable