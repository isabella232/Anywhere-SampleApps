package com.medallia.anywhere_versioning_sampleapp.model

data class ApiToken(
    var tokenString: String,
    var propertyId: Long
) : JWToken(token = "")
