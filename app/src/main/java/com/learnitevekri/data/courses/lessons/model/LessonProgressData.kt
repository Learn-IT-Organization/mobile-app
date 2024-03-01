package com.learnitevekri.data.courses.lessons.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LessonProgressData(
    @SerializedName("user_id")
    val userId: Int,
    @SerializedName("lesson_id")
    val lessonId: Int,
    @SerializedName("lesson_score")
    val lessonScore: Float,
    @SerializedName("is_completed")
    val isCompleted: Boolean
) : Serializable