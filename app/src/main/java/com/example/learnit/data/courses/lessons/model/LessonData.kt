package com.example.learnit.data.courses.lessons.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class LessonData(
    @SerializedName("lesson_id")
    val lessonId: Int,
    @SerializedName("lesson_name")
    val lessonName: String,
    @SerializedName("lesson_chapter_id")
    val lessonChapterId: Int,
    @SerializedName("lesson_sequence_number")
    val lessonSequenceNumber: Int,
    @SerializedName("lesson_description")
    val lessonDescription: String,
    @SerializedName("lesson_type")
    val lessonType: String,
    @SerializedName("lesson_tags")
    val lessonTags: String
) : Serializable