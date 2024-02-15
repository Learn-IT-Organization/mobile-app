package com.example.learnit.data.courses.lessons.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LessonResultData(
    @SerializedName("userScore") val userScore: Float = 0f,
    @SerializedName("totalScore") val totalScore: Float = 0f,
) : Serializable