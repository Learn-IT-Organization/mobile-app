package com.example.learnit.data.courses.lessons.model

import com.example.learnit.ui.feature.courses.lessons.model.Response
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class ResponseAnswer(
    @SerialName("ansText") val ansText: String,
    @SerialName("isCorrect") val isCorrect: Boolean
)

@Serializable
data class Response(
    @SerialName("answer") val answer: List<ResponseAnswer>
)

@Serializable
data class MultipleChoiceResponseData(
    @SerialName("uqr_question_id") val uqr_question_id: Int,
    @SerialName("uqr_user_id") val uqr_user_id: Int,
    @SerialName("response") val response: Response,
    @SerialName("is_correct") val is_correct: Boolean,
    @SerialName("score") val score: Float,
    @SerialName("response_time") val response_time: Date
)