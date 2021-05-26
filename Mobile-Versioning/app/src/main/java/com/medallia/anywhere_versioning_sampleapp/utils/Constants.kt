package com.medallia.anywhere_versioning_sampleapp.utils

object Constants {
    enum class ApiVersion(s: String) {
        v1("v1"),
        v2("v2.0"),
        v3("v3.0")
    }
    const val CONTENT_TYPE = "Content-Type"
    const val APPLICATION_JSON = "application/json"
    const val BEARER = "Bearer_"
    const val AUTHORIZATION = "Authorization"
    const val BASE_URL = "https://api-qa.kampyle.com/api/" //change this with yours --> You get this from the Command Center
    var ACCESS_TOKEN_ENDPOINT = "${ApiVersion.v2}/accessToken"
    var GET_CONFIGURATION_ENDPOINT= "${ApiVersion.v2}/configuration"
    var SUBMIT_FEEDBACK_ENDPOINT = "${ApiVersion.v2}/feedback"
}