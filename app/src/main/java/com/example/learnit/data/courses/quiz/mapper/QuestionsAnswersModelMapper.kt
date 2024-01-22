package com.example.learnit.data.courses.quiz.mapper

import com.example.learnit.data.courses.quiz.model.AnswerData
import com.example.learnit.data.courses.quiz.model.QuestionsAnswersData
import com.example.learnit.ui.feature.courses.quiz.model.AnswerModel
import com.example.learnit.ui.feature.courses.quiz.model.QuestionsAnswersModel

fun QuestionsAnswersData<AnswerData>.mapToQuestionAnswer() = QuestionsAnswersModel(
    questionId = question_id,
    lessonId = qa_lesson_id,
    questionText = question_text,
    questionType = question_type,
    answers = answers.map { it.mapToAnswer() },
    chapterId = qa_chapter_id,
    courseId = qa_course_id
)

fun AnswerData.mapToAnswer() = AnswerModel(
    optionText = option_text,
)

fun List<QuestionsAnswersData<AnswerData>>.mapToQuestionAnswersList(): List<QuestionsAnswersModel<AnswerModel>> {
    return map { it.mapToQuestionAnswer() }
}

fun QuestionsAnswersModel<AnswerModel>.mapToQuestionAnswerData() = QuestionsAnswersData(
    question_id = questionId,
    qa_lesson_id = lessonId,
    question_text = questionText,
    question_type = questionType,
    answers = answers.map { it.mapToAnswerData() },
    qa_chapter_id = chapterId,
    qa_course_id = courseId
)

fun AnswerModel.mapToAnswerData() = AnswerData(
    option_text = optionText,
)