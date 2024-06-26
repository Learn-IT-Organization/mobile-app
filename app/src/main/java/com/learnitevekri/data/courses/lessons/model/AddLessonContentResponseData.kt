package com.learnitevekri.data.courses.lessons.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddLessonContentResponseData(
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("lessonId")
    val lessonId: Int,
    @SerializedName("contentId")
    val contentId: Int
) : Serializable