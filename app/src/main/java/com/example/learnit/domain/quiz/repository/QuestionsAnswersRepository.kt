package com.example.learnit.domain.quiz.repository

import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel

interface QuestionsAnswersRepository {
    suspend fun getQuestionsAnswers(): List<QuestionsAnswersModel>
}