package com.example.learnit.data.courses.quiz.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class QuizResultData(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("score")
    val score: Float,
) : Serializable