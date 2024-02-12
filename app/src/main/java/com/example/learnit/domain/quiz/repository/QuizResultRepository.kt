package com.example.learnit.domain.quiz.repository

import com.example.learnit.data.courses.quiz.model.QuizResultData
import com.example.learnit.data.courses.quiz.model.QuizResponseData

interface QuizResultRepository {
    suspend fun sendResponse(quizResponseData: QuizResponseData): QuizResultData
}