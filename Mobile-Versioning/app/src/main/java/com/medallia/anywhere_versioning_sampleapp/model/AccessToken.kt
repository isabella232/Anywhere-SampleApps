package com.medallia.anywhere_versioning_sampleapp.model

data class AccessToken(
    val accessToken: String,
    val ttl: Long,
    val createTime: Long
) : JWToken(token = "")