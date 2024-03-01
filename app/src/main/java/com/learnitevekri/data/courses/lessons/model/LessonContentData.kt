package com.learnitevekri.data.courses.lessons.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LessonContentData(
    @SerializedName("content_id") val contentId: Int,
    @SerializedName("content_type") val contentType: String,
    @SerializedName("url") val url: String,
    @SerializedName("content_lesson_id") val contentLessonId: Int,
    @SerializedName("content_title") val contentTitle: String,
    @SerializedName("content_description") val contentDescription: String
) : Serializable