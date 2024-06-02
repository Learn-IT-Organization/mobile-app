package com.learnitevekri.data.courses.lessons.model

import com.google.gson.annotations.SerializedName

data class AddNewLessonResponseData(
    @SerializedName("success")
    val success: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("lessonId")
    val lessonId: Int,
    @SerializedName("userId")
    val userId: Int
) : java.io.Serializable
