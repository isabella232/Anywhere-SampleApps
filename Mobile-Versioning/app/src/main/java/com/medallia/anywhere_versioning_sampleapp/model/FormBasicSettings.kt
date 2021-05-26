package com.medallia.anywhere_versioning_sampleapp.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class FormBasicSettings (
    @SerializedName("submitButtonLabel")
    var submitButtonLabel: String = ""
): Serializable