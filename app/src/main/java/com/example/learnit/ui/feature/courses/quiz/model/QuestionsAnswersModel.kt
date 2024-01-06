package com.example.learnit.ui.feature.courses.quiz.model

data class QuestionsAnswersModel(
    val questionId: Int?,
    val lessonId: Int?,
    val questionText: String?,
    val questionType: String?,
    val answers: List<AnswersModel>,
    val chapterId: Int?,
    val courseId: Int?
)

data class AnswersModel(
    val optionText: String?,
    val isCorrect: Boolean?
)
