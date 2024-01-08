package com.example.learnit.data.courses.quiz.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Answer(
    @SerialName("ansText")
    val ansText: String,

    @SerialName("isCorrect")
    val isCorrect: Boolean
)

@Serializable
data class MultipleChoiceQuestionAnswerData(
    @SerialName("question_id")
    val question_id: Int,

    @SerialName("qa_lesson_id")
    val qa_lesson_id: Int,

    @SerialName("question_text")
    val question_text: String,

    @SerialName("question_type")
    val question_type: String,

    @SerialName("answers")
    val answers: List<Answer>,

    @SerialName("qa_chapter_id")
    val qa_chapter_id: Int,

    @SerialName("qa_course_id")
    val qa_course_id: Int
)