package com.example.learnit.domain.quiz.repository

import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel
import com.example.learnit.ui.feature.courses.quiz.model.AnswerModel

interface QuestionsAnswersRepository {
    suspend fun getQuestionsAnswers(): List<QuestionsAnswersModel<AnswerModel>>
    suspend fun getQuestionsAnswersByCourseIdChapterIdLessonId(
        courseId: Int,
        chapterId: Int,
        lessonId: Int
    ): List<QuestionsAnswersModel<AnswerModel>>
}