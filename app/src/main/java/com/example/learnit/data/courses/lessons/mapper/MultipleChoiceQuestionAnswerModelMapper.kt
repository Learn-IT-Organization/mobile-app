package com.example.learnit.data.courses.lessons.mapper

import com.example.learnit.data.courses.lessons.model.MultipleChoiceQuestionAnswerData
import com.example.learnit.ui.feature.courses.lessons.model.MultipleChoiceQuestionAnswerModel

fun MultipleChoiceQuestionAnswerData.mapToMultipleChoiceQA() = MultipleChoiceQuestionAnswerModel(
    questionId = this.question_id,
    qaLessonId = this.qa_lesson_id,
    questionText = this.question_text,
    questionType = this.question_type,
    answers = this.answers,
    qaChapterId = this.qa_chapter_id,
    qaCourseId = this.qa_course_id
)

fun List<MultipleChoiceQuestionAnswerData>.mapToMultipleChoiceQAList() = map { it.mapToMultipleChoiceQA() }