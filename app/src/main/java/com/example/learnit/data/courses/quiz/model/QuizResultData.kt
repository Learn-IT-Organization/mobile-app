package com.example.learnit.data.courses.quiz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class QuizResultData(
    @SerialName("uqr_question_id") val uqr_question_id: Int,
    @SerialName("uqr_user_id") val uqr_user_id: Int,
    @SerialName("response") var response: List<QuizResponseData>,
    @SerialName("is_correct") val is_correct: Int,
    @SerialName("score") val score: Int,
    @SerialName("response_time") val response_time: Date?
)

data class QuizResponseData(
    @SerialName("option_text") val option_text: String,
    @SerialName("is_correct") val is_correct: Boolean
)
