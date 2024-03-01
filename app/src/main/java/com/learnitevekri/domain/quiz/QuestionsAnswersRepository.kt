package com.learnitevekri.domain.quiz

import com.learnitevekri.data.courses.quiz.model.BaseQuestionData

interface QuestionsAnswersRepository {
    suspend fun getQuestionsAnswersByCourseIdChapterIdLessonId(
        courseId: Int,
        chapterId: Int,
        lessonId: Int
    ): List<BaseQuestionData>
}