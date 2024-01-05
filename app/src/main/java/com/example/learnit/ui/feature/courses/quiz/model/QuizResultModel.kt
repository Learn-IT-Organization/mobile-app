package com.example.learnit.ui.feature.courses.quiz.model
import java.util.Date

data class QuizResultModel(
    val uqrQuestionId: Int?,
    val uqrUserId: Int?,
    val response: List<QuizResponseModel>?,
    val isCorrect: Int?,
    val score: Int?,
    val responseTime: Date?
)
data class QuizResponseModel(
    val optionText: String?,
    val isCorrect: Boolean?
)