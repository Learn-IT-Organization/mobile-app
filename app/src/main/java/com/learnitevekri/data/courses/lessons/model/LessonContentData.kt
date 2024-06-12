package com.learnitevekri.data.courses.lessons.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LessonContentData(
    @SerializedName("content_id") var contentId: Int,
    @SerializedName("content_type") var contentType: String,
    @SerializedName("url") var url: String,
    @SerializedName("content_lesson_id") val contentLessonId: Int,
    @SerializedName("content_title") var contentTitle: String,
    @SerializedName("content_description") var contentDescription: String,
    @SerializedName("content_user_id") var contentUserId: Int
) : Serializable