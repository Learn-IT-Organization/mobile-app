package com.example.learnit.domain.quiz.repository

import com.example.learnit.data.courses.quiz.model.BaseQuestionData

interface QuestionsAnswersRepository {
    suspend fun getQuestionsAnswersByCourseIdChapterIdLessonId(
        courseId: Int,
        chapterId: Int,
        lessonId: Int
    ): List<BaseQuestionData>
}