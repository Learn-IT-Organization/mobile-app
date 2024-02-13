package com.example.learnit.domain.course.repository

import com.example.learnit.data.courses.lessons.model.LessonData

interface LessonRepository {
    suspend fun getLessons(): List<LessonData>
    suspend fun getLessonsByChapterId(chapterId: Int): List<LessonData>

}