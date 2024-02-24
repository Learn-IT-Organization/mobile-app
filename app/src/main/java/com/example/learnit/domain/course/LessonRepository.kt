package com.example.learnit.domain.course

import com.example.learnit.data.courses.lessons.model.LessonContentData
import com.example.learnit.data.courses.lessons.model.LessonData
import com.example.learnit.data.courses.lessons.model.LessonProgressData
import com.example.learnit.data.courses.lessons.model.UserAnswersData

interface LessonRepository {
    suspend fun getLessons(): List<LessonData>
    suspend fun getLessonsByChapterId(chapterId: Int): List<LessonData>
    suspend fun getLessonContentByLessonId(lessonId: Int): List<LessonContentData>
    suspend fun getLessonProgress(): List<LessonProgressData>
    suspend fun getLessonById(lessonId: Int): LessonData?
    suspend fun getLessonResultWithValidation(lessonId: Int): List<UserAnswersData>
}