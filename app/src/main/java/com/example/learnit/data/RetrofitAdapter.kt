package com.example.learnit.data

import com.example.learnit.data.ApiConstants.API_BASE_URL
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.ArrayList

import java.util.HashMap




object RetrofitAdapter {
    private val retrofit: Retrofit

    init {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Cookie", "token=YOUR_TOKEN_HERE") // Helyettesítsd be a tokent
                    .build()
                chain.proceed(request)
            }
            .build()


        retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun provideApiService(): ApiService =
        retrofit.create(ApiService::class.java)

}