package com.medallia.anywhere_mobile_sampleapp.jwt

import android.util.Base64
import timber.log.Timber
import java.nio.charset.StandardCharsets
import javax.inject.Inject

class JWTokenParser @Inject constructor() {

    fun getPayload(jwt: String?): String? {
        if (jwt == null) {
            return null
        }
        try {
            val split = jwt.split(".").toTypedArray()
            return getJson(split[1])
        } catch (e: Exception) {
            Timber.e(e.message.toString())
        }
        return null
    }

    fun getJson(strEncoded: String): String? {
        try {
            val decodedBytes =
                Base64.decode(strEncoded, Base64.URL_SAFE)
            return String(decodedBytes, StandardCharsets.UTF_8)
        } catch (e: Exception) {
            Timber.e(e.message.toString())
        }
        return null
    }
}