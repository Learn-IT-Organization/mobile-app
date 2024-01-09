package com.example.learnit.data.courses.quiz.repository

import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.courses.quiz.model.QuizResultData
import com.example.learnit.data.courses.quiz.model.UserResponseData
import com.example.learnit.domain.quiz.repository.QuizResultRepository

object QuizResultRepositoryImpl : QuizResultRepository {
    private val apiService = RetrofitAdapter.provideApiService()

    override suspend fun sendResponse(userResponseData: UserResponseData): QuizResultData {
        val response = apiService.sendResponse(userResponseData)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        }
        throw Exception("Error sending quiz response")
    }
}