package com.learnitevekri.data.courses.lessons.model

import com.google.gson.annotations.SerializedName

data class AddNewLessonData(
    @SerializedName("lesson_id")
    var lessonId: Int?,
    @SerializedName("lesson_name")
    var lessonName: String,
    @SerializedName("lesson_chapter_id")
    val lessonChapterId: Int,
    @SerializedName("lesson_sequence_number")
    val lessonSequenceNumber: Int,
    @SerializedName("lesson_description")
    var lessonDescription: String,
    @SerializedName("lesson_type")
    var lessonType: String,
    @SerializedName("lesson_tags")
    var lessonTags: String,
    @SerializedName("lesson_user_id") val lessonUserId: Int
)
