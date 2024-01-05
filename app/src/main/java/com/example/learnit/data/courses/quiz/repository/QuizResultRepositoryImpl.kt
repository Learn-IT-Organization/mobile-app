package com.example.learnit.data.courses.quiz.repository

import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.courses.quiz.model.QuizResultData
import com.example.learnit.data.courses.quiz.model.UserResponseData
import com.example.learnit.domain.quiz.repository.QuizResultRepository

object QuizResultRepositoryImpl : QuizResultRepository {
    override suspend fun sendResult(quizResultData: QuizResultData): UserResponseData? {
        val apiService = RetrofitAdapter.provideApiService()
        val response = apiService.sendResult(quizResultData)
        if (response.isSuccessful && response.body() != null) {
            return response.body()
        }
        return null
    }
}