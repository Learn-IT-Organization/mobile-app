package com.example.learnit.ui.feature.courses.quiz.model

import com.example.learnit.data.courses.quiz.model.Answer

data class Answer(
    val ansText: String,
    val isCorrect: Boolean
)

data class MultipleChoiceQuestionAnswerModel(
    val questionId: Int,
    val qaLessonId: Int,
    val questionText: String,
    val questionType: String,
    val answers: List<Answer>,
    val qaChapterId: Int,
    val qaCourseId: Int
)