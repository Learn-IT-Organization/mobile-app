package com.example.learnit.data

import com.example.learnit.data.ApiConstants.API_BASE_URL
import com.example.learnit.ui.App
import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitAdapter {

    private val retrofit: Retrofit

    init {

        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val cookieJar =
            PersistentCookieJar(SetCookieCache(), SharedPrefsCookiePersistor(App.instance))

        val client =
            OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .cookieJar(cookieJar)
                .build()

        retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }

    fun provideApiService(): ApiService = retrofit.create(ApiService::class.java)

}