package com.medallia.anywhere_mobile_sampleapp.model

import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import java.io.Serializable


data class FormMobileSettingsContract (
    @SerializedName("title")
    @ColumnInfo(name = "formMobileSettingsContractTitle")
    var title: String = ""
): Serializable