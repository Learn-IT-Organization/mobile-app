package com.learnitevekri.data.courses.course.model

import com.google.gson.annotations.SerializedName

data class AddNewCourseResponseData(
    @SerializedName("success")
    val success: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("courseId")
    val courseId: Int,
    @SerializedName("userId")
    val userId: Int
) : java.io.Serializable
