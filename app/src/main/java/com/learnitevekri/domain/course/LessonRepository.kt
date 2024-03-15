package com.learnitevekri.domain.course

import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.data.courses.lessons.model.LessonData
import com.learnitevekri.data.courses.lessons.model.LessonProgressData
import com.learnitevekri.data.courses.lessons.model.UserAnswersData

interface LessonRepository {
    suspend fun getLessonsByChapterId(chapterId: Int): List<LessonData>
    suspend fun getLessonContentByLessonId(lessonId: Int): List<LessonContentData>
    suspend fun getLessonProgress(): List<LessonProgressData>
    suspend fun getLessonById(lessonId: Int): LessonData?
    suspend fun getLessonResultWithValidation(lessonId: Int): List<UserAnswersData>
}