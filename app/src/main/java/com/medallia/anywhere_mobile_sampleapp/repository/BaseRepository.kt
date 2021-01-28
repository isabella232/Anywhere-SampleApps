package com.medallia.anywhere_mobile_sampleapp.repository

import com.google.gson.Gson
import com.medallia.anywhere_mobile_sampleapp.database.Storage
import com.medallia.anywhere_mobile_sampleapp.jwt.AccessToken
import com.medallia.anywhere_mobile_sampleapp.jwt.ApiToken
import com.medallia.anywhere_mobile_sampleapp.jwt.JWTokenParser
import com.medallia.anywhere_mobile_sampleapp.network.RestClient
import com.medallia.anywhere_mobile_sampleapp.utils.Constants
import com.medallia.anywhere_mobile_sampleapp.utils.ExternalError
import kotlinx.coroutines.flow.first
import timber.log.Timber

open class BaseRepository constructor(var restClient: RestClient,
                                      var gson: Gson,
                                      var jwTokenParser: JWTokenParser,
                                      var storage: Storage) {

    private  var accessToken: AccessToken? = null

    suspend fun createAccessToken(jsonString: String): AccessToken? {
        try {
            val arr = jwTokenParser.getPayload(jsonString)
            accessToken = gson.fromJson(arr.toString(), AccessToken::class.java)
            accessToken?.token = jsonString
            accessToken?.let {
                if (isTtlPassed(it)) {
                    return null
                }
            }
            accessToken?.token?.let { storage.putString(it, Storage.ACCESS_TOKEN) }
            Timber.i("Create Access Token")
            return accessToken
        } catch (e: Exception){
            Timber.e("%s%s", ExternalError.ACCESS_TOKEN_PARSE_ERROR.toString(),
                e.message.toString())
        }
        return null
    }

    suspend fun getHeadersForAccessToken(): HashMap<String, String> {
        val headers = HashMap<String, String>()

        val accessTokenString = storage.getString(Storage.ACCESS_TOKEN).first()
        headers[Constants.AUTHORIZATION] = Constants.BEARER + accessTokenString
        headers[Constants.CONTENT_TYPE] = Constants.APPLICATION_JSON

        return headers
    }

    suspend fun getAccessToken(apiToken: ApiToken): AccessToken? {

        try {
            val response =
                apiToken.authUrl.let { restClient.post(getHeadersForToken(apiToken.token), it) }
            var jsonString = ""

            if (response.raw().code == 200) {
                val jsonObject = response.body()
                if (jsonObject != null) {
                    if (jsonObject.has("accessToken")) {
                        jsonString = jsonObject.get("accessToken").asString
                    }
                }
                return createAccessToken(jsonString)
            } else if (response.raw().code == 401) {
                Timber.e(ExternalError.AUTHORIZATION_FAILED.toString())
            }
            Timber.i("Get Access token")
        } catch (e: Exception) {
            Timber.e("%s%s", ExternalError.AUTHORIZATION_FAILED.toString(),
                e.message.toString())
        }
        return null
    }

    private fun getHeadersForToken(token: String): HashMap<String, String> {
        val headers = HashMap<String, String>()

        headers[Constants.AUTHORIZATION] = Constants.BEARER + token
        headers[Constants.CONTENT_TYPE] = Constants.APPLICATION_JSON

        return headers
    }

    private fun isTtlPassed(accessToken: AccessToken): Boolean {
        try {
            Timber.i("Check if Access Token Is Valid")
            accessToken.let {
                return System.currentTimeMillis() - it.createTime > it.ttl - 600000
            }
        } catch (e: Exception) {
            Timber.e("%s%s", ExternalError.ACCESS_TOKEN_TTL_IS_PASSED.toString(),
                e.message.toString())
        }
        return true
    }
}