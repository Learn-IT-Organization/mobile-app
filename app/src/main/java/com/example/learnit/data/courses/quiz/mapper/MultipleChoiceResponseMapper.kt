package com.example.learnit.data.courses.quiz.mapper

import com.example.learnit.data.courses.quiz.model.MultipleChoiceResponseData
import com.example.learnit.ui.feature.courses.quiz.model.MultipleChoiceResponseModel


fun MultipleChoiceResponseModel.mapToResponse() = MultipleChoiceResponseData(
    uqr_question_id = this.uqrQuestionId,
    uqr_user_id = this.uqrUserId,
    response = this.response,
    is_correct = this.isCorrect,
    score = this.score,
    response_time = this.responseTime
)