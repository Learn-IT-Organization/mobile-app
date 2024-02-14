package com.example.learnit.data.courses.lessons.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LessonResultData(
    @SerializedName("userScore")
    var userScore: Float = 0f,
    @SerializedName("totalScore")
    var totalScore: Float = 0f,
) : Serializable