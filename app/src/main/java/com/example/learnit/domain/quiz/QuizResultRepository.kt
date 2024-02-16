package com.example.learnit.domain.quiz

import com.example.learnit.data.courses.lessons.model.DeleteResponseData
import com.example.learnit.data.courses.quiz.model.QuizResponseData
import com.example.learnit.data.courses.quiz.model.QuizResultData

interface QuizResultRepository {
    suspend fun sendResponse(quizResponseData: QuizResponseData): QuizResultData
    suspend fun deleteResponses(lessonId: Int): DeleteResponseData
}