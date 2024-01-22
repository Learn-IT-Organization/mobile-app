package com.example.learnit.ui.feature.courses.quiz.model

data class QuestionsAnswersModel<T>(
    val questionId: Int,
    val lessonId: Int,
    val questionText: String,
    val questionType: String,
    val answers: List<T>,
    val chapterId: Int,
    val courseId: Int
)

data class AnswerModel(
    val optionText: String,
)