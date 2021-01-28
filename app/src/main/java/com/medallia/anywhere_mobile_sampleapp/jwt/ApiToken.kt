package com.medallia.anywhere_mobile_sampleapp.jwt

data class ApiToken(
    var authUrl: String,
    var propertyId: Long
) : JWToken(token = "")





