package com.example.learnit.data.courses.quiz.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.courses.lessons.model.DeleteResponseData
import com.example.learnit.data.courses.quiz.model.QuizResultData
import com.example.learnit.data.courses.quiz.model.QuizResponseData
import com.example.learnit.domain.quiz.repository.QuizResultRepository

object QuizResultRepositoryImpl : QuizResultRepository {
    private val apiService = RetrofitAdapter.provideApiService()
    private val TAG: String = QuizResultRepositoryImpl::class.java.simpleName

    override suspend fun sendResponse(quizResponseData: QuizResponseData): QuizResultData {
        val response = apiService.sendResponse(quizResponseData)
        if (response.isSuccessful && response.body() != null) {
            val score = response.body()!!.score
            Log.d(TAG, "Score: $score")
            return response.body()!!
        }
        throw Exception("Error sending quiz response")
    }

    override suspend fun deleteResponses(lessonId: Int): DeleteResponseData {
        val response = apiService.deleteResponses(lessonId)
        if (response.isSuccessful && response.body() != null) {
            val message = response.body()!!.message
            Log.d(TAG, "Message: $message")
            return response.body()!!
        }
        throw Exception("Error deleting quiz response")
    }
}