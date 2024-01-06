package com.example.learnit.data.courses.quiz.mapper

import com.example.learnit.data.courses.quiz.model.AnswersData
import com.example.learnit.data.courses.quiz.model.QuestionsAnswersData
import com.example.learnit.ui.feature.courses.quiz.model.AnswersModel
import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel

fun QuestionsAnswersData.mapToQuestionsAnswers() = QuestionsAnswersModel(
    questionId = question_id,
    lessonId = qa_lesson_id,
    questionText = question_text,
    questionType = question_type,
    answers = answers.map { it.mapToAnswer() },
    chapterId = qa_chapter_id,
    courseId = qa_course_id
)

fun AnswersData.mapToAnswer() = AnswersModel(
    optionText = option_text,
    isCorrect = is_correct
)

fun List<QuestionsAnswersData>.mapToQuestionAnswersList() = map { it.mapToQuestionsAnswers() }