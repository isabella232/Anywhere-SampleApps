package com.medallia.anywhere_mobile_sampleapp.model

import androidx.room.*
import com.google.gson.annotations.SerializedName
import java.io.Serializable

@Entity(tableName = "form")
data class Form(
    @PrimaryKey
    @SerializedName("formId")
    @ColumnInfo(name = "form_id")
    var formId: String = "",

    @SerializedName("customParams")
    @ColumnInfo(name = "custom_params")
    var customParams: String = "",

    @SerializedName("formJson")
    @Embedded
    var formJson: FormJson? = null,

    @SerializedName("formLanguage")
    @ColumnInfo(name = "form_language")
    var formLanguage: String = "",

    @SerializedName("formType")
    @ColumnInfo(name = "form_Type")
    var formType: String = "",

    @SerializedName("inviteData")
    @Embedded
    var inviteData: Any? = null,

    @SerializedName("name")
    @ColumnInfo(name = "form_name")
    var name: String = "",

    @SerializedName("partialTranslations")
    @Embedded
    var partialTranslations: Any? = null,

    @SerializedName("title")
    @ColumnInfo(name = "form_title")
    var title: String = "",

    @SerializedName("triggerData")
    @Embedded
    var triggerData: Any? = null
) : Serializable