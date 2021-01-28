package com.medallia.anywhere_mobile_sampleapp.network

import com.google.gson.JsonObject
import com.medallia.anywhere_mobile_sampleapp.model.FeedbackData
import retrofit2.Response
import retrofit2.http.*

interface RestClient {
    @POST
    suspend fun postFeedback(@HeaderMap headers: Map<String, String>, @Url endPoint: String, @Body feedbackData: FeedbackData) : Response<JsonObject>
    @POST
    suspend fun post(@HeaderMap headers: Map<String, String>, @Url endPoint: String) : Response<JsonObject>
    @GET
    suspend fun get(@HeaderMap headers: Map<String, String>, @Url endPoint: String) : Response<JsonObject>
}