package com.example.learnit.data.courses.quiz.mapper

import com.example.learnit.data.courses.quiz.model.QuestionsAnswersData
import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel

fun QuestionsAnswersData.mapToQuestionAnswer() = QuestionsAnswersModel(
    questionId = question_id,
    lessonId = qa_lesson_id,
    questionText = question_text,
    questionType = question_type,
    answers = answers.map { it },
    chapterId = qa_chapter_id,
    courseId = qa_course_id
)

fun List<QuestionsAnswersData>.mapToQuestionAnswersList(): List<QuestionsAnswersModel> {
    return map { it.mapToQuestionAnswer() }
}