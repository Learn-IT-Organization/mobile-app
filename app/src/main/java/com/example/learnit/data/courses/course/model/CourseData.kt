package com.example.learnit.data.courses.course.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.Date

data class CourseData(
    @SerializedName("course_id")
    val course_id: Int,
    @SerializedName("course_name")
    val course_name: String,
    @SerializedName("create_date")
    val create_date: Date,
    @SerializedName("programming_language")
    val programming_language: String,
    @SerializedName("course_description")
    val course_description: String,
    @SerializedName("course_user_id")
    val course_user_id: Int
) : Serializable