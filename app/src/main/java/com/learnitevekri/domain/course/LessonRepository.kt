package com.learnitevekri.domain.course

import com.learnitevekri.data.courses.lessons.model.AddLessonContentResponseData
import com.learnitevekri.data.courses.lessons.model.AddNewLessonData
import com.learnitevekri.data.courses.lessons.model.AddNewLessonResponseData
import com.learnitevekri.data.courses.lessons.model.DeleteResponseData
import com.learnitevekri.data.courses.lessons.model.EditLessonContentData
import com.learnitevekri.data.courses.lessons.model.EditLessonData
import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.data.courses.lessons.model.LessonData
import com.learnitevekri.data.courses.lessons.model.LessonProgressData
import com.learnitevekri.data.courses.lessons.model.UserAnswersData

interface LessonRepository {
    suspend fun getLessonsByChapterId(chapterId: Int): List<LessonData>
    suspend fun getLessonContentByLessonId(lessonId: Int): List<LessonContentData>
    suspend fun getLessonProgress(): List<LessonProgressData>
    suspend fun getLessonById(lessonId: Int): LessonData
    suspend fun getLessonResultWithValidation(lessonId: Int): List<UserAnswersData>
    suspend fun addNewLesson(addNewLessonData: AddNewLessonData): Int?
    suspend fun editLesson(lessonId: Int, editLessonData: EditLessonData): AddNewLessonResponseData
    suspend fun createLessonContent(lessonContentData: LessonContentData): Int?
    suspend fun editLessonContent(
        lessonContentId: Int,
        editLessonContentData: EditLessonContentData
    ): AddLessonContentResponseData

    suspend fun deleteLesson(lessonId: Int): DeleteResponseData
}