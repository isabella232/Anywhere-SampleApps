package com.medallia.anywhere_mobile_sampleapp.repository

import com.google.gson.Gson
import com.medallia.anywhere_mobile_sampleapp.database.Storage
import com.medallia.anywhere_mobile_sampleapp.database.daos.ConfigurationDao
import com.medallia.anywhere_mobile_sampleapp.jwt.JWTokenParser
import com.medallia.anywhere_mobile_sampleapp.model.*
import com.medallia.anywhere_mobile_sampleapp.network.RestClient
import com.medallia.anywhere_mobile_sampleapp.utils.ExternalError
import timber.log.Timber
import javax.inject.Inject

class SubmitFeedbackRepository @Inject constructor(restClient: RestClient, gson: Gson,
                                                   jwTokenParser: JWTokenParser,
                                                   private var configurationDao: ConfigurationDao,
                                                   storage: Storage
) : BaseRepository(restClient, gson, jwTokenParser, storage ) {

    lateinit var configuration: Configuration

    suspend fun submitFeedBack(form: Form) : FeedbackData {
        configuration = configurationDao.get()
        val feedbackData = createFeedbackData(form);
        configuration.let {
            try {
                val response =
                    configuration.feedbackSubmitUrl.let {
                        restClient.postFeedback(
                            getHeadersForAccessToken(),
                            it,
                            feedbackData
                        )
                    }
                Timber.i("Submit feedback")

            } catch (e: Exception) {
                Timber.e("%s%s", ExternalError.SUBMIT_FEEDBACK_ERROR.toString(),
                    e.message.toString())
            }
        }

        return feedbackData
    }

    private fun createFeedbackData(form: Form): FeedbackData {
        val components = ArrayList<Component>()
        val pages = ArrayList<FeedbackPage>()

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

        return FeedbackData(form.formId.toInt(), FeedbackDynamicData(pages), form.formLanguage)
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