package com.example.learnit.ui.feature.courses.lessons.model

data class LessonModel(
    val lessonId: Int,
    val lessonName: String,
    val lessonChapterId: Int,
    val lessonSequenceNumber: Int,
    val lessonDescription: String,
    val lessonType: String,
    val lessonTags: String
)