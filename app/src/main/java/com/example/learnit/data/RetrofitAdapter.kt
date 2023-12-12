package com.example.learnit.data

import com.example.learnit.data.Constants.API_BASE_URL
import retrofit2.Retrofit
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