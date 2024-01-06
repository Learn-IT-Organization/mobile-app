package com.example.learnit.data.courses.quiz.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.courses.quiz.mapper.mapToQuestionAnswersList
import com.example.learnit.domain.quiz.repository.QuestionsAnswersRepository
import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel

object QuestionsAnswersRepositoryImpl : QuestionsAnswersRepository {
    private val apiService = RetrofitAdapter.provideApiService()
    override suspend fun getQuestionsAnswers(): List<QuestionsAnswersModel> {
        try {
            val response = QuestionsAnswersRepositoryImpl.apiService.getQuestionsAnswers()
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

}