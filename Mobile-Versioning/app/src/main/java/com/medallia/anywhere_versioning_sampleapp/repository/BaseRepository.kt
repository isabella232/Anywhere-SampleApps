package com.medallia.anywhere_versioning_sampleapp.repository

import com.google.gson.Gson
import com.medallia.anywhere_versioning_sampleapp.database.Storage
import com.medallia.anywhere_versioning_sampleapp.jwt.JWTokenParser
import com.medallia.anywhere_versioning_sampleapp.model.ApiToken
import com.medallia.anywhere_versioning_sampleapp.network.RestClient
import com.medallia.anywhere_versioning_sampleapp.utils.Constants
import com.medallia.anywhere_versioning_sampleapp.utils.ExternalError
import timber.log.Timber
import com.medallia.anywhere_versioning_sampleapp.model.AccessToken
import kotlinx.coroutines.flow.first

open class BaseRepository constructor(
    var restClient: RestClient,
    var gson: Gson,
    var jwTokenParser: JWTokenParser,
    var storage: Storage
) {

    private var accessToken: AccessToken? = null
    private val getAccessTokenUrl = Constants.BASE_URL + Constants.ACCESS_TOKEN_ENDPOINT

    suspend fun createAccessToken(jsonString: String): AccessToken? {
        try {

            accessToken = gson.fromJson(jsonString, AccessToken::class.java)
            accessToken.let {
                if (it != null) {
                    storage.putString(jsonString, Storage.ACCESS_TOKEN)
                }
                Timber.i("Create Access Token")
                return accessToken
            }
        } catch (e: Exception) {
            Timber.e(
                "%s%s", ExternalError.ACCESS_TOKEN_PARSE_ERROR.toString(),
                e.message.toString()
            )
        }
        return null
    }


    suspend fun getHeadersForAccessToken(): HashMap<String, String> {
        val headers = HashMap<String, String>()
        val accessTokenString = storage.getString(Storage.ACCESS_TOKEN).first()
        val accessTokenObject = gson.fromJson(accessTokenString, AccessToken::class.java)

        headers[Constants.AUTHORIZATION] = Constants.BEARER + accessTokenObject.accessToken
        headers[Constants.CONTENT_TYPE] = Constants.APPLICATION_JSON


        return headers
    }

    suspend fun getAccessToken(apiToken: ApiToken): AccessToken? {
        try {
            val response = restClient.post(getHeadersForToken(apiToken.tokenString), getAccessTokenUrl)

            if (response.raw().code == 200) {
                val jsonObject = response.body()
                return jsonObject?.let { createAccessToken(it.toString()) }
            } else if (response.raw().code == 401) {
                Timber.e(ExternalError.AUTHORIZATION_FAILED.toString())
            }
            Timber.i("Get Access token")
        } catch (e: Exception) {
            Timber.e(
                "%s%s", ExternalError.AUTHORIZATION_FAILED.toString(),
                e.message.toString()
            )
        }
        return null
    }

    private fun getHeadersForToken(token: String): HashMap<String, String> {
        val headers = HashMap<String, String>()

        headers[Constants.AUTHORIZATION] = Constants.BEARER + token
        headers[Constants.CONTENT_TYPE] = Constants.APPLICATION_JSON

        return headers
    }

    fun isTtlPassed(accessToken: AccessToken?): Boolean {
        try {
            Timber.i("Check if Access Token Is Valid")
            accessToken.let {
                if (it != null) {
                    return System.currentTimeMillis() - it.createTime > it.ttl - 600000
                }
            }

        } catch (e: Exception) {
            Timber.e(
                "%s%s", ExternalError.ACCESS_TOKEN_TTL_IS_PASSED.toString(),
                e.message.toString()
            )
        }
        return true
    }
}