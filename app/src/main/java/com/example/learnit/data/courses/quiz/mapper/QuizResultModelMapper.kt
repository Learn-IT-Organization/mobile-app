package com.example.learnit.data.courses.quiz.mapper

import com.example.learnit.data.courses.quiz.model.QuizResponseData
import com.example.learnit.data.courses.quiz.model.QuizResultData
import com.example.learnit.ui.feature.courses.quiz.model.QuizResponseModel
import com.example.learnit.ui.feature.courses.quiz.model.QuizResultModel

fun QuizResultData.mapToQuizResult()  = QuizResultModel(
    uqrQuestionId = uqr_question_id,
    uqrUserId = uqr_user_id,
    response = response.map { it.mapToQuizResponse() },
    isCorrect = is_correct,
    score = score,
    responseTime = response_time
)

fun QuizResponseData.mapToQuizResponse() = QuizResponseModel(
    optionText = option_text,
    isCorrect = is_correct
)