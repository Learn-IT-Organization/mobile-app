package com.example.learnit.data.courses.quiz.model

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class UserResponseData(
    @SerializedName("uqr_question_id") val uqrQuestionId: Int,
    @SerializedName("uqr_user_id") val uqrUserId: Int,
    @SerializedName("response") var response: Any,
    @SerializedName("response_time") val responseTime: Date,
    @SerializedName("score") val score: Float
)