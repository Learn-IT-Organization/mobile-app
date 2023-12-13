package com.example.learnit.data.courses.lessons.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LessonData(
    @SerialName("lesson_id")
    val lesson_id: Int,
    @SerialName("lesson_name")
    val lesson_name: String,
    @SerialName("lesson_chapter_id")
    val lesson_chapter_id: Int,
    @SerialName("lesson_sequence_number")
    val lesson_sequence_number: Int,
    @SerialName("lesson_description")
    val lesson_description: String,
    @SerialName("lesson_type")
    val lesson_type: String,
    @SerialName("lesson_tags")
    val lesson_tags: String,
)