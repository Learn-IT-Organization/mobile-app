package com.learnitevekri.data.courses.quiz.repository

import android.util.Log
import com.learnitevekri.data.RetrofitAdapter
import com.learnitevekri.data.courses.quiz.model.AddQuestionAnswerResponseData
import com.learnitevekri.data.courses.quiz.model.BaseQuestionData
import com.learnitevekri.data.courses.quiz.model.EditQuestionAnswerData
import com.learnitevekri.domain.quiz.QuestionsAnswersRepository

object QuestionsAnswersRepositoryImpl : QuestionsAnswersRepository {
    private val TAG = QuestionsAnswersRepositoryImpl::class.java.simpleName
    private val apiService = RetrofitAdapter.provideApiService()
    override suspend fun getQuestionsAnswersByCourseIdChapterIdLessonId(
        courseId: Int,
        chapterId: Int,
        lessonId: Int
    ): List<BaseQuestionData> {
        try {
            val response =
                apiService.getQuestionsAnswersByCourseIdChapterIdLessonId(
                    courseId,
                    chapterId,
                    lessonId
                )
            if (response.isSuccessful) {
                val responseData = response.body()
                return responseData ?: emptyList()
            }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "Error fetching questions and answers by course id, chapter id and lesson id: ${e.message}"
            )
            throw e
        }
        return emptyList()
    }

    override suspend fun createQuestionAnswer(questionData: BaseQuestionData): BaseQuestionData {
        try {
            val response = apiService.createQuestionAnswer(questionData)
            if (response.isSuccessful) {
                return response.body() ?: questionData
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error creating question and answer: ${e.message}")
            throw e
        }
        return questionData
    }

    override suspend fun <T> editQuestionAnswer(
        questionId: Int,
        editQuestionAnswerData: EditQuestionAnswerData<T>
    ): AddQuestionAnswerResponseData {
        try {
            val response = apiService.editQuestionAnswer(questionId, editQuestionAnswerData)
            if (response.isSuccessful) {
                return response.body()!!
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error editing question and answer: ${e.message}")
            throw e
        }
        return null!!
    }
}