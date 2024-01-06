package com.example.learnit.ui.feature.courses.lessons.model

import java.util.Date

data class ResponseAnswer(
    val ansText: String,
    val isCorrect: Boolean
)

data class Response(
    val answer: List<ResponseAnswer>
)

data class MultipleChoiceResponseModel(
    val uqrQuestionId: Int,
    val uqrUserId: Int,
    val response: Response,
    val isCorrect: Boolean,
    val score: Float,
    val responseTime: Date
)