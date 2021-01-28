package com.medallia.anywhere_mobile_sampleapp.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class DynamicData(
    @SerializedName("id")
    @ColumnInfo(name = "dynamicDataId")
    var id: Int = 0,

    @SerializedName("component")
    var component: String = "",

    @SerializedName("description")
    var description: String = "",

    @SerializedName("display")
    var display: Boolean = false,

    @SerializedName("index")
    var index: Int = 0,

    @SerializedName("label")
    var label: String = "",

    @SerializedName("options")
    @Embedded
    var options: List<Any>?= null,

    @SerializedName("optionsById")
    @Embedded
    var optionsById: Any? = null,

    @SerializedName("placeholder")
    var placeholder: String = "",

    @SerializedName("ratingScales")
    @Embedded
    var ratingScales: Any? = null,

    @SerializedName("required")
    var required: Boolean = false,

    @SerializedName("unique_name")
    var unique_name: String = ""
): Serializable