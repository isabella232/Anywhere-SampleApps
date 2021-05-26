package com.medallia.anywhere_mobile_sampleapp.repository

import android.text.TextUtils
import com.google.gson.Gson
import com.medallia.anywhere_mobile_sampleapp.database.Storage
import com.medallia.anywhere_mobile_sampleapp.database.daos.ConfigurationDao
import com.medallia.anywhere_mobile_sampleapp.database.daos.FormDao
import com.medallia.anywhere_mobile_sampleapp.jwt.AccessToken
import com.medallia.anywhere_mobile_sampleapp.jwt.ApiToken
import com.medallia.anywhere_mobile_sampleapp.jwt.JWTokenParser
import com.medallia.anywhere_mobile_sampleapp.model.*
import com.medallia.anywhere_mobile_sampleapp.network.RestClient
import com.medallia.anywhere_mobile_sampleapp.utils.DataState
import com.medallia.anywhere_mobile_sampleapp.utils.ExternalError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import timber.log.Timber
import javax.inject.Inject


class MainRepository @Inject constructor(
    jwTokenParser: JWTokenParser,
    restClient: RestClient,
    gson: Gson,
    storage: Storage,
    private val formDao: FormDao,
    private val configurationDao: ConfigurationDao
) : BaseRepository(restClient, gson, jwTokenParser, storage) {

    private var apiToken: ApiToken? = null
    private var configurationUUID: ConfigurationUUID? = null
    private var configuration: Configuration? = null



    fun getApiToken(apiTokenString: String): ApiToken? {
        Timber.i("Get Api token")

        if (TextUtils.isEmpty(apiTokenString)) {
            Timber.e(ExternalError.API_TOKEN_NOT_VALID.toString())
        } else {
            try {
                val jsonString = jwTokenParser.getPayload(apiTokenString)
                apiToken = gson.fromJson(jsonString.toString(), ApiToken::class.java)
                apiToken?.token = apiTokenString
                apiToken?.let {
                    if (validate(it)) {
                        return apiToken
                    }
                }

            } catch (e: Exception) {
               Timber.e("%s %s", ExternalError.API_TOKEN_NOT_VALID.toString(),
                   e.message.toString())
            }
        }
        return null
    }

    private fun validate(apiToken: ApiToken): Boolean {
        Timber.i("Validate Api token")

        if (TextUtils.isEmpty(apiToken.authUrl)) {
            return false
        } else if (apiToken.propertyId.toInt() == -1) {
            return false
        }
        return true

    }

    suspend fun getConfiguration(accessToken: AccessToken) {
        try {
            Timber.i("Get Configuration Start")

            val response =
                accessToken.getConfigUrl.let { restClient.get(getHeadersForAccessToken(), it) }
            configuration = gson.fromJson(response.body(), Configuration::class.java)

            configuration?.configurationUUID?.url?.let { storage.putString(it,Storage.UUID_URL) }
            configuration?.configurationUUID?.uuid?.let { storage.putString(it, Storage.UUID) }

            persistData()

        } catch (e: Exception) {
            Timber.e("%s%s", ExternalError.CONFIGURATION_ERROR.toString(),
                e.message.toString())
        }
    }

    private suspend fun persistData() {
        try {
            configuration?.let { configurationDao.insert(it) }
            configuration?.forms?.let {
                for (form in it) {
                    formDao.insertForm(form)
                }
            }
            Timber.i("Save configuration to DB")

        } catch (e: Exception){
            Timber.e(e.message.toString())

        }
    }
    suspend fun getLocalConfiguration() {
        configuration = configurationDao.get()
    }

    suspend fun deleteAllForms() {
        formDao.deleteAllForms()
    }

    suspend fun getForms(): Flow<DataState<List<Form>>> = flow {
        emit(DataState.Loading)
        try {

            val formList = formDao.getAllForms()
            emit(DataState.Success(formList))
            Timber.i("Get All Forms")

        } catch (e: Exception) {
            emit(DataState.Error(e))
            Timber.e(e.message.toString())
        }
    }

    suspend fun checkIfUUIDIsChanged(): Boolean {
        val url = storage.getString(Storage.UUID_URL).first()
        val uuid = storage.getString(Storage.UUID).first()

        if (url == "" || uuid == "") {
            return true
        }

        try {
            val response = restClient.get(HashMap(), url)
            configurationUUID = gson.fromJson(response.body(), ConfigurationUUID::class.java)
            if (configurationUUID?.uuid != uuid) {
                Timber.i("New UUID is not equal to previous using remote configuration")
                return true
            }
        } catch (e: Exception) {
            Timber.e("%s%s", ExternalError.GET_UUID_ERROR.toString(),
                e.message.toString())
        }
        Timber.i("Uuid is equal -> using previous configuration")

        return false
    }
}

