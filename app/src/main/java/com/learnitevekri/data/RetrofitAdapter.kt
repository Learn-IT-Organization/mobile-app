package com.learnitevekri.data

import com.franmontiel.persistentcookiejar.PersistentCookieJar
import com.franmontiel.persistentcookiejar.cache.SetCookieCache
import com.franmontiel.persistentcookiejar.persistence.SharedPrefsCookiePersistor
import com.google.gson.GsonBuilder
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory
import com.learnitevekri.data.ApiConstants.API_BASE_URL
import com.learnitevekri.data.courses.quiz.model.BaseQuestionData
import com.learnitevekri.data.courses.quiz.model.MatchingQuestionData
import com.learnitevekri.data.courses.quiz.model.MultipleChoiceQuestionData
import com.learnitevekri.data.courses.quiz.model.SortingQuestionData
import com.learnitevekri.data.courses.quiz.model.TrueFalseQuestionData
import com.learnitevekri.ui.App
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
                .addInterceptor(ErrorHandlingInterceptor())
                .cookieJar(cookieJar)
                .build()

        val gson = GsonBuilder()
            .registerTypeAdapterFactory(
                RuntimeTypeAdapterFactory.of(BaseQuestionData::class.java, "question_type")
                    .registerSubtype(TrueFalseQuestionData::class.java, "true_false")
                    .registerSubtype(MultipleChoiceQuestionData::class.java, "multiple_choice")
                    .registerSubtype(SortingQuestionData::class.java, "sorting")
                    .registerSubtype(MatchingQuestionData::class.java, "matching")
            )
            .create()


        retrofit = Retrofit.Builder()
            .baseUrl(API_BASE_URL).client(client)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    fun provideApiService(): ApiService =
        retrofit.create(ApiService::class.java)

}