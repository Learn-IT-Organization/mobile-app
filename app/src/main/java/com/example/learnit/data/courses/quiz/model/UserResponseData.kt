package com.example.learnit.data.courses.quiz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class UserResponseData(
    @SerialName("uqr_question_id") val uqr_question_id: Int,
    @SerialName("uqr_user_id") val uqr_user_id: Int,
    @SerialName("response") var response: QuizResponseData,
    @SerialName("response_time") val response_time: Date
)

@Serializable
data class QuizResponseData(
    @SerialName("answer") val answer: List<Boolean>
)