package com.example.learnit.domain

import com.example.learnit.data.courses.quiz.model.MultipleChoiceResponseData
import com.example.learnit.ui.feature.courses.lessons.model.LessonModel
import com.example.learnit.ui.feature.courses.lessons.model.LessonContentModel
import com.example.learnit.ui.feature.courses.quiz.model.MultipleChoiceQuestionAnswerModel

interface LessonRepository {
    suspend fun getLessons(): List<LessonModel>
    suspend fun getLessonsByChapterId(chapterId: Int): List<LessonModel>
    suspend fun getQuestionsAnswersByCourseIdChapterIdLessonIdMultipleChoice(
        courseId: Int,
        chapterId: Int,
        lessonId: Int
    ): List<MultipleChoiceQuestionAnswerModel>

    suspend fun postMultipleChoiceResponse(multipleChoiceResponseData: MultipleChoiceResponseData): MultipleChoiceResponseData
    suspend fun getLessonContentByLessonId(lessonId: Int): List<LessonContentModel>
}