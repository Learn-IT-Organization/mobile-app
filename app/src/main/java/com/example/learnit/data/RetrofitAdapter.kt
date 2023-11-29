package com.example.learnit.data

import com.example.learnit.data.Constants.API_BASE_URL
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAdapter {
    private val retrofit: Retrofit

    init {

        retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    fun provideApiService(): ApiService =
        retrofit.create(ApiService::class.java)

}