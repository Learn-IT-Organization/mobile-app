package com.example.learnit.domain.quiz.repository

import com.example.learnit.data.courses.quiz.model.QuizResultData
import com.example.learnit.data.courses.quiz.model.UserResponseData

interface QuizResultRepository {
    suspend fun sendResult(quizResultData: QuizResultData): UserResponseData?

}