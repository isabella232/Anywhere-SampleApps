package com.medallia.anywhere_mobile_sampleapp.ui

import android.app.Application
import com.medallia.anywhere_mobile_sampleapp.BuildConfig
import com.medallia.anywhere_mobile_sampleapp.database.Storage
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltAndroidApp
class AnywhereApplication : Application() {

    @Inject
    lateinit var preferencesData: Storage

    private var tokenString = ""

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        /**
         * Note: replace the value of tokenString to your property token.
         */
        tokenString = "REPLACE TO ACCOUNT API TOKEN"

        CoroutineScope(Main).launch {
            if (tokenString == preferencesData.getString(Storage.API_TOKEN).first()) {
                preferencesData.putBoolean(false, Storage.FIRST_INIT)

            } else {
                preferencesData.putString("", Storage.ACCESS_TOKEN)
                preferencesData.putString("", Storage.UUID)
                preferencesData.putString("", Storage.UUID_URL)
                preferencesData.putBoolean(true, Storage.FIRST_INIT)
                preferencesData.putString(tokenString, Storage.API_TOKEN)
            }
        }
    }

}