package com.example.learnit.domain.course.repository

import com.example.learnit.ui.feature.courses.lessons.model.LessonModel

interface LessonRepository {
    suspend fun getLessons(): List<LessonModel>
    suspend fun getLessonsByChapterId(chapterId: Int): List<LessonModel>

}