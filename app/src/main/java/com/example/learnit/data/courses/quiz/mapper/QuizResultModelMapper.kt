package com.example.learnit.data.courses.quiz.mapper

import QuizResponseModel
import UserResponseModel
import com.example.learnit.data.courses.quiz.model.QuizResponseData
import com.example.learnit.data.courses.quiz.model.QuizResultData
import com.example.learnit.data.courses.quiz.model.UserResponseData
import com.example.learnit.ui.feature.courses.quiz.model.QuizResultModel

fun QuizResultData.mapToQuizResultData(): QuizResultModel {
    return QuizResultModel(
        success = success,
        message = message,
        score = score
    )
}

fun UserResponseModel.mapToUserResponseData(): UserResponseData {
    return UserResponseData(
        uqr_question_id = uqrQuestionId,
        uqr_user_id = uqrUserId,
        response = response.mapToQuizResponseData(),
        response_time = responseTime
    )
}

fun QuizResponseModel.mapToQuizResponseData(): QuizResponseData {
    return QuizResponseData(
        answer = answer
    )
}