package com.example.learnit.data.courses.quiz.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.courses.quiz.mapper.mapToQuestionAnswersList
import com.example.learnit.domain.quiz.repository.QuestionsAnswersRepository
import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel
import com.example.learnit.ui.feature.courses.quiz.model.AnswerModel

object QuestionsAnswersRepositoryImpl : QuestionsAnswersRepository {
    private val TAG = QuestionsAnswersRepositoryImpl::class.java.simpleName
    private val apiService = RetrofitAdapter.provideApiService()
    override suspend fun getQuestionsAnswers(): List<QuestionsAnswersModel<AnswerModel>> {
        try {
            val response = apiService.getQuestionsAnswers()
            if (response.isSuccessful) {
                val responseData = response.body()
                Log.d("QuestionsAnswersResponse", response.raw().toString())
                return (responseData ?: emptyList()).mapToQuestionAnswersList()
            }
        } catch (e: Exception) {
            Log.e("QuestionsAnswersRepositoryImpl", "Error fetching questionsAnswers: ${e.message}")
        }
        return emptyList()
    }

    override suspend fun getQuestionsAnswersByCourseIdChapterIdLessonId(
        courseId: Int,
        chapterId: Int,
        lessonId: Int
    ): List<QuestionsAnswersModel<AnswerModel>> {
        try {
            val response =
                apiService.getQuestionsAnswersByCourseIdChapterIdLessonId(
                    courseId,
                    chapterId,
                    lessonId
                )
            if (response.isSuccessful) {
                val responseData = response.body()
                val data = responseData ?: emptyList()
                return data.mapToQuestionAnswersList()
            }
        } catch (e: Exception) {
            Log.e(
                TAG,
                "Error fetching questions and answers by course id, chapter id and lesson id: ${e.message}"
            )
        }
        return emptyList()
    }
}