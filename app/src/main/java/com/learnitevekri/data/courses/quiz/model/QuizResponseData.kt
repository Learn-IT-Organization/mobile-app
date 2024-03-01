package com.learnitevekri.data.courses.quiz.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class QuizResponseData(
    @SerializedName("uqr_question_id") val uqrQuestionId: Int,
    @SerializedName("uqr_user_id") val uqrUserId: Int,
    @SerializedName("response") var response: Any,
    @SerializedName("response_time") val responseTime: Date,
    @SerializedName("score") val score: Float
) : Serializable