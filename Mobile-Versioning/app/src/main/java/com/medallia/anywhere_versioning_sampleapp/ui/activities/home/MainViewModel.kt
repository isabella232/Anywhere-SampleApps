package com.medallia.anywhere_versioning_sampleapp.ui.activities.home

import android.os.Build
import android.text.TextUtils
import androidx.annotation.RequiresApi
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.medallia.anywhere_mobile_sampleapp.repository.MainRepository
import com.medallia.anywhere_versioning_sampleapp.database.Storage
import com.medallia.anywhere_versioning_sampleapp.model.AccessToken
import com.medallia.anywhere_versioning_sampleapp.model.ApiToken
import com.medallia.anywhere_versioning_sampleapp.model.Form
import com.medallia.anywhere_versioning_sampleapp.utils.DataState
import com.medallia.anywhere_versioning_sampleapp.utils.ExternalError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber


class MainViewModel @ViewModelInject constructor(
    private var mainRepository: MainRepository,
    var storage: Storage
) : ViewModel() {

    private var initializing = false
    private var apiToken: ApiToken? = null
    private var accessToken: AccessToken? = null

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    suspend fun init(token: String) {
        Timber.i("Init SDK")
        if (initializing) {
            Timber.i(ExternalError.SDK_IS_ALREADY_INITIALIZED.toString())
            return
        }
        initializing = true

        try {
            apiToken = mainRepository.getApiToken(token)
            accessToken = apiToken?.let { getAccessToken(it) }

        } catch (e: Exception) {
            Timber.e(
                "%s %s", ExternalError.API_TOKEN_NOT_VALID.toString(),
                e.message.toString()
            )
        }

        if (apiToken == null) {
            initializing = false
            deleteAllForms()
            return
        }

        when {
            storage.getBoolean(Storage.FIRST_INIT).first() -> {
                deleteAllForms()
                accessToken.let {
                    if (it != null) {
                        mainRepository.getConfiguration()
                    }
                }
            }
            mainRepository.checkIfUUIDIsChanged() -> {
                deleteAllForms()
                accessToken.let {
                    if (it != null) {
                        mainRepository.getConfiguration()
                    }
                }
            }
            else -> {
                mainRepository.getLocalConfiguration()

            }
        }
    }

    suspend fun getForms(): LiveData<DataState<List<Form>>> {
        return mainRepository.getForms().asLiveData()
    }

    private fun deleteAllForms() {
        viewModelScope.launch {
            mainRepository.deleteAllForms()
        }
    }

    private suspend fun getAccessToken(apiToken: ApiToken): AccessToken? {
        val jsonString = storage.getString(Storage.ACCESS_TOKEN).first()
        if (!TextUtils.isEmpty(jsonString)) {
            val token = mainRepository.createAccessToken(jsonString)
             if (token != null && !mainRepository.isTtlPassed(token)) {
                 return token
            }
        }
        return mainRepository.getAccessToken(apiToken)

    }
}
