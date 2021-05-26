package com.medallia.anywhere_versioning_sampleapp.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.medallia.anywhere_versioning_sampleapp.network.RestClient
import com.medallia.anywhere_versioning_sampleapp.utils.Constants
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object NetworkModule {

    @Singleton
    @Provides
    fun provideGsonBuilder(): Gson {
        return GsonBuilder()
            .create()
    }

    @Singleton
    @Provides
    fun provideRetrofit(gson: Gson): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
    }

    @Singleton
    @Provides
    fun provideRetrofitService(retrofit: Retrofit.Builder): RestClient {
        return retrofit
            .build()
            .create(RestClient::class.java)
    }
}