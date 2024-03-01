package com.learnitevekri.domain.quiz

import com.learnitevekri.data.courses.lessons.model.DeleteResponseData
import com.learnitevekri.data.courses.quiz.model.QuizResponseData
import com.learnitevekri.data.courses.quiz.model.QuizResultData

interface QuizResultRepository {
    suspend fun sendResponse(quizResponseData: QuizResponseData): QuizResultData
    suspend fun deleteResponses(lessonId: Int): DeleteResponseData
}