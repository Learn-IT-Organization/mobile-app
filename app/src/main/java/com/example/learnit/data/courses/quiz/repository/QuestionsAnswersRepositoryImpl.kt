package com.example.learnit.data.courses.quiz.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.courses.quiz.model.BaseQuestionData
import com.example.learnit.domain.quiz.repository.QuestionsAnswersRepository

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
        }
        return emptyList()
    }
}