package com.medallia.anywhere_mobile_sampleapp.ui.activities.home

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.medallia.anywhere_mobile_sampleapp.database.Storage
import com.medallia.anywhere_mobile_sampleapp.jwt.AccessToken
import com.medallia.anywhere_mobile_sampleapp.jwt.ApiToken
import com.medallia.anywhere_mobile_sampleapp.model.Form
import com.medallia.anywhere_mobile_sampleapp.repository.MainRepository
import com.medallia.anywhere_mobile_sampleapp.utils.DataState
import com.medallia.anywhere_mobile_sampleapp.utils.ExternalError
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber


class MainViewModel @ViewModelInject constructor(
    private var mainRepository: MainRepository,
    var storage: Storage
) : ViewModel() {

    private var initializing = false
    private var apiToken: ApiToken? = null

    suspend fun init(token: String) {
        if (initializing) {
            Timber.i(ExternalError.SDK_IS_ALREADY_INITIALIZED.toString())
            return
        }

        initializing = true
        apiToken = mainRepository.getApiToken(token)


        if (apiToken == null) {
            initializing = false
            deleteAllForms()
            return
        }

        val accessToken = getAccessToken()

        when {
            storage.getBoolean(Storage.FIRST_INIT).first() -> {
                deleteAllForms()
                accessToken?.let { mainRepository.getConfiguration(it) }
            }
            mainRepository.checkIfUUIDIsChanged() -> {
                deleteAllForms()
                accessToken?.let { mainRepository.getConfiguration(it) }
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

    private suspend fun getAccessToken(): AccessToken? {
        val tokenString = storage.getString(Storage.ACCESS_TOKEN).first()

        if (tokenString != "") {
            val accessToken = mainRepository.createAccessToken(tokenString)
            accessToken?.let { return accessToken }
        }

        return apiToken?.let { mainRepository.getAccessToken(it) }
    }
}
