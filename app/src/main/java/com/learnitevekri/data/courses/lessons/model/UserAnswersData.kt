package com.learnitevekri.data.courses.lessons.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class UserAnswersData(
    @SerializedName("questionText")
    val questionText: String,
    @SerializedName("questionType")
    val questionType: String,
    @SerializedName("score")
    val score: Float,
    @SerializedName("userAnswer")
    val userAnswer: String,
    @SerializedName("correctness")
    val correctness: CorrectnessData
) : Serializable