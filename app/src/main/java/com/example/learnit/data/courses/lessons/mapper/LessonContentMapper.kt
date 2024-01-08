package com.example.learnit.data.courses.lessons.mapper

import com.example.learnit.data.courses.lessons.model.LessonContentData
import com.example.learnit.ui.feature.courses.lessons.model.LessonContentModel

fun LessonContentData.mapToLessonContent() = LessonContentModel(
    contentId = this.content_id,
    contentType = this.content_type,
    url = this.url,
    attachments = this.attachments,
    contentLessonId = this.content_lesson_id
)

fun List<LessonContentData>.mapToLessonContentList() = map { it.mapToLessonContent() }