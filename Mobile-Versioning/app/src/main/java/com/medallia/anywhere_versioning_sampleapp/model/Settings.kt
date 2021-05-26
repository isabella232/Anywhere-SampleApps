package com.medallia.anywhere_versioning_sampleapp.model

import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Settings(
    @SerializedName("formBasicSettings")
    @Embedded
    var formBasicSettings: FormBasicSettings? = null,

    @SerializedName("formMobileSettingsContract")
    @Embedded
    var formMobileSettingsContract: FormMobileSettingsContract? = null
): Serializable