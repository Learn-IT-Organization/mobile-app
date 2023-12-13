package com.example.learnit.data.courses.course.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.Date

@Serializable
data class CourseData(
    @SerialName("course_id")
    val course_id: Int,
    @SerialName("course_name")
    val course_name: String,
    @SerialName("create_date")
    val create_date: Date,
    @SerialName("programming_language")
    val programming_language: String,
    @SerialName("course_description")
    val course_description: String,
    @SerialName("course_user_id")
    val course_user_id: Int
)