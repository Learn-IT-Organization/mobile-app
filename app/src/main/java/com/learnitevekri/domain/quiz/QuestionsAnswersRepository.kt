package com.learnitevekri.domain.quiz

import com.learnitevekri.data.courses.quiz.model.AddMatchingQuestionData
import com.learnitevekri.data.courses.quiz.model.AddMultipleChoiceQuestionData
import com.learnitevekri.data.courses.quiz.model.AddQuestionAnswerResponseData
import com.learnitevekri.data.courses.quiz.model.AddSortingQuestionData
import com.learnitevekri.data.courses.quiz.model.AddTrueFalseQuestionData
import com.learnitevekri.data.courses.quiz.model.BaseQuestionData
import com.learnitevekri.data.courses.quiz.model.EditQuestionAnswerData

interface QuestionsAnswersRepository {
    suspend fun getQuestionsAnswersByCourseIdChapterIdLessonId(
        courseId: Int,
        chapterId: Int,
        lessonId: Int,
    ): List<BaseQuestionData>

    suspend fun createQuestionAnswerMatching(
        questionData: AddMatchingQuestionData,
    ): AddQuestionAnswerResponseData

    suspend fun createTrueFalseQuestionAnswer(questionData: AddTrueFalseQuestionData): AddQuestionAnswerResponseData

    suspend fun createMultipleChoiceQuestionAnswer(questionData: AddMultipleChoiceQuestionData): AddQuestionAnswerResponseData

    suspend fun createSortingQuestionAnswer(questionData: AddSortingQuestionData): AddQuestionAnswerResponseData

    suspend fun <T> editQuestionAnswer(
        questionId: Int,
        editQuestionAnswerData: EditQuestionAnswerData<T>,
    ): AddQuestionAnswerResponseData

}