package com.medallia.anywhere_versioning_sampleapp.utils

import java.lang.Exception

sealed class DataState<out R> {

    data class Success<out T>(val data: T): DataState<T>()
    data class Error(val exception: Exception): DataState<Nothing>()
    object Loading : DataState<Nothing>()
}