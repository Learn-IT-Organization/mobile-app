package com.learnitevekri.data.courses.course.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class AddNewCourseData(
    @SerializedName("course_name")
    val course_name: String,
    @SerializedName("programming_language")
    val programming_language: String,
    @SerializedName("course_description")
    val course_description: String,
    @SerializedName("course_user_id")
    val course_user_id: Int
)
