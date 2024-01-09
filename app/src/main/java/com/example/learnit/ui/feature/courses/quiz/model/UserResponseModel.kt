package com.example.learnit.ui.feature.courses.quiz.model

import java.util.Date

data class UserResponseModel(
    val uqrQuestionId: Int,
    val uqrUserId: Int,
    var response: List<UserAnswerModel>,
    val isCorrect: Int,
    val score: Int,
    val responseTime: Date
)

data class UserAnswerModel(
    val optionText: String,
    val isCorrect: Boolean
)