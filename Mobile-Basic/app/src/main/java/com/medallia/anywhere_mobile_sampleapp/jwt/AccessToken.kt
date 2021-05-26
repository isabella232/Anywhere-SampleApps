package com.medallia.anywhere_mobile_sampleapp.jwt

data class AccessToken(
     val getConfigUrl: String,
     val propertyId: Long,
     val createTime: Long,
     val ttl: Long
) : JWToken(token = "")