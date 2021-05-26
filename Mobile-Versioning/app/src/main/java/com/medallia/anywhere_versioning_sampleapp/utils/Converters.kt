package com.medallia.anywhere_versioning_sampleapp.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.medallia.anywhere_versioning_sampleapp.model.Form
import com.medallia.anywhere_versioning_sampleapp.model.Page

class Converters  {
    var gson = Gson()

    @TypeConverter
    fun pageToSomePageList(data: String?): List<Page>? {
        val notesType = object : TypeToken<List<Page>>() {}.type
        return gson.fromJson<List<Page>>(data, notesType)
    }

    @TypeConverter
    fun pageListToPage(someObjects: List<Page?>?): String? {
        return gson.toJson(someObjects)
    }

    @TypeConverter
    fun formToSomeFormList(data: String?): List<Form>? {
        val notesType = object : TypeToken<List<Form>>() {}.type
        return gson.fromJson<List<Form>>(data, notesType)
    }

    @TypeConverter
    fun formListToForm(someObjects: List<Form?>?): String? {
        return gson.toJson(someObjects)
    }

}