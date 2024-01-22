package com.example.learnit.data.courses.quiz.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.courses.quiz.model.QuizResultData
import com.example.learnit.data.courses.quiz.model.UserResponseData
import com.example.learnit.domain.quiz.repository.QuizResultRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

object QuizResultRepositoryImpl : QuizResultRepository {
    private val apiService = RetrofitAdapter.provideApiService()
    private val TAG: String = QuizResultRepositoryImpl::class.java.simpleName

    private val _latestScore = MutableStateFlow<Float>(0f)
    val latestScore = _latestScore.asStateFlow()

    override suspend fun sendResponse(userResponseData: UserResponseData): QuizResultData {
        val response = apiService.sendResponse(userResponseData)
        if (response.isSuccessful && response.body() != null) {
            val score = response.body()!!.score
            _latestScore.value = score
            Log.d(TAG, "Score: $score")
            return response.body()!!
        }
        throw Exception("Error sending quiz response")
    }
}