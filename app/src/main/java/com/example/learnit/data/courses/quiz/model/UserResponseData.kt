package com.example.learnit.data.courses.quiz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class UserResponseData(
    @SerialName("uqr_question_id")
    val uqr_question_id: Int,
    @SerialName("uqr_user_id")
    val uqr_user_id: Int,
    @SerialName("response")
    var response: List<UserAnswerData>,
    @SerialName("is_correct")
    val is_correct: Int,
    @SerialName("score")
    val score: Int,
    @SerialName("response_time")
    val response_time: Date
)

data class UserAnswerData(
    val optionText: String,
    val isCorrect: Boolean
)