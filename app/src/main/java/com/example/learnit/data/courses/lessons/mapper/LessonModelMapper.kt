package com.example.learnit.data.courses.lessons.mapper

import com.example.learnit.data.courses.lessons.model.LessonData
import com.example.learnit.ui.feature.courses.lessons.model.LessonModel

fun LessonData.mapToLesson() = LessonModel(
    lessonId = this.lesson_id,
    lessonName = this.lesson_name,
    lessonChapterId = this.lesson_chapter_id,
    lessonSequenceNumber = this.lesson_sequence_number,
    lessonDescription = this.lesson_description,
    lessonType = this.lesson_type,
    lessonTags = this.lesson_tags
)

fun List<LessonData>.mapToLessonList() = map { it.mapToLesson() }