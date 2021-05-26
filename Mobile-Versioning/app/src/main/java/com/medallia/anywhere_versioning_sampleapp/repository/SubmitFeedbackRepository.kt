package com.medallia.anywhere_versioning_sampleapp.repository

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.medallia.anywhere_versioning_sampleapp.database.Storage
import com.medallia.anywhere_versioning_sampleapp.database.daos.ConfigurationDao
import com.medallia.anywhere_versioning_sampleapp.jwt.JWTokenParser
import com.medallia.anywhere_versioning_sampleapp.model.*
import com.medallia.anywhere_versioning_sampleapp.network.RestClient
import com.medallia.anywhere_versioning_sampleapp.utils.Constants
import com.medallia.anywhere_versioning_sampleapp.utils.ExternalError
import timber.log.Timber
import java.util.*
import java.lang.reflect.Type
import javax.inject.Inject

class SubmitFeedbackRepository @Inject constructor(
    restClient: RestClient, gson: Gson,
    jwTokenParser: JWTokenParser,
    private var configurationDao: ConfigurationDao,
    storage: Storage
) : BaseRepository(restClient, gson, jwTokenParser, storage) {

    lateinit var configuration: Configuration
    private val submitFeedbackUrl = Constants.BASE_URL + Constants.SUBMIT_FEEDBACK_ENDPOINT

    suspend fun submitFeedBack(form: Form): FeedbackData {
        configuration = configurationDao.get()
        val feedbackData = createFeedbackData(form);

        try {
            val response = restClient.postFeedback(
                getHeadersForAccessToken(),
                submitFeedbackUrl,
                feedbackData
            )
            if (response.raw().code == 200) {
                Timber.i("Submit feedback success")

            } else if (response.raw().code == 401) {
                Timber.i("Submit feedback failed")
            }

        } catch (e: Exception) {
            Timber.e(
                "%s%s", ExternalError.SUBMIT_FEEDBACK_ERROR.toString(),
                e.message.toString()
            )
        }


        return feedbackData
    }

    private fun createFeedbackData(form: Form): FeedbackData {
        val components = ArrayList<Component>()
        val pages = ArrayList<FeedbackPage>()
        val customParams = ArrayList<CustomParam>()

        //This Custom Param field is showing in command center inbox
        customParams.add(createCustomParamsData(form.customParams))

        try {
            form.formJson?.pages?.let { list ->
                for (page in list) {
                    page.dynamicData?.let {
                        for (dynamicData in it) {
                            components.add(
                                Component(
                                    dynamicData.id, dynamicData.component,
                                    dynamicData.unique_name, checkWhichComponentToSend(dynamicData)
                                )
                            )
                        }
                    }
                    pages.add(FeedbackPage(components))
                }
            }
            Timber.i("Create Feedback Data")
        } catch (e: Exception) {
            Timber.e(e.message.toString())
        }

        return FeedbackData(
            form.formId.toInt(),
            FeedbackDynamicData(pages, customParams),
            form.formLanguage
        )
    }

    private fun createCustomParamsData(customParams: String): CustomParam {
        val cParamArrayType: Type = object : TypeToken<ArrayList<CustomParam>>() {}.type
        val cParams: ArrayList<CustomParam> = gson.fromJson(customParams, cParamArrayType)

        return cParams[(0 until cParams.size).random()]
    }

    private fun checkWhichComponentToSend(dynamicData: DynamicData): Any {
        val type = dynamicData.component
        val checkBoxSelection = ArrayList<String>()
        var options = ArrayList<HashMap<String, String>>()

        if (dynamicData.optionsById != null) options =
            dynamicData.optionsById as ArrayList<HashMap<String, String>>

        when (type) {
            "textInput", "textArea" -> return dynamicData.label
            "select", "radio" -> {
                for (option in options) {
                    return option["id"] ?: ""
                }
            }
            "checkbox" -> {
                for (option in options) {
                    option["id"]?.let { checkBoxSelection.add(it) }
                }
                return checkBoxSelection
            }
            "grading" -> {
                return (0..5).random()
            }
            "grading1to10" -> {
                return (1..10).random()
            }
            "grading0to10", "nps" -> {
                return (0..10).random()
            }
            else -> {
                return "image"
            }

        }
        return ""
    }
}