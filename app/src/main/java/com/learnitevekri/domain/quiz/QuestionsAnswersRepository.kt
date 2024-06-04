package com.learnitevekri.domain.quiz

import com.learnitevekri.data.courses.quiz.model.AddQuestionAnswerResponseData
import com.learnitevekri.data.courses.quiz.model.BaseQuestionData
import com.learnitevekri.data.courses.quiz.model.EditQuestionAnswerData

interface QuestionsAnswersRepository {
    suspend fun getQuestionsAnswersByCourseIdChapterIdLessonId(
        courseId: Int,
        chapterId: Int,
        lessonId: Int
    ): List<BaseQuestionData>

    suspend fun createQuestionAnswer(
        questionData: BaseQuestionData
    ): BaseQuestionData

    suspend fun <T> editQuestionAnswer(
        questionId: Int,
        editQuestionAnswerData: EditQuestionAnswerData<T>
    ): AddQuestionAnswerResponseData
}