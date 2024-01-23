package com.example.learnit.ui.feature.courses.quiz.model

data class QuestionsAnswersModel(
    val questionId: Int,
    val lessonId: Int,
    val questionText: String,
    val questionType: String,
    val answers: List<String>,
    val chapterId: Int,
    val courseId: Int
)