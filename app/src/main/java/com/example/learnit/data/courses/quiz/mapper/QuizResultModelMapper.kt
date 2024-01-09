package com.example.learnit.data.courses.quiz.mapper

import com.example.learnit.data.courses.quiz.model.QuizResultData
import com.example.learnit.data.courses.quiz.model.UserAnswerData
import com.example.learnit.data.courses.quiz.model.UserResponseData
import com.example.learnit.ui.feature.courses.quiz.model.QuizResultModel
import com.example.learnit.ui.feature.courses.quiz.model.UserAnswerModel
import com.example.learnit.ui.feature.courses.quiz.model.UserResponseModel

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
        response = response.map { it.mapToUserAnswerData() },
        is_correct = isCorrect,
        score = score,
        response_time = responseTime
    )
}

fun UserAnswerModel.mapToUserAnswerData(): UserAnswerData {
    return UserAnswerData(
        optionText = optionText,
        isCorrect = isCorrect
    )
}