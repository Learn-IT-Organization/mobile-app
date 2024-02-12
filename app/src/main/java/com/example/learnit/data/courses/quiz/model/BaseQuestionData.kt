package com.example.learnit.data.courses.quiz.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

abstract class BaseQuestionData(
    @SerializedName("question_id") val questionId: Int = 0,
    @SerializedName("qa_lesson_id") val qaLessonId: Int = 0,
    @SerializedName("question_text") val questionText: String = "",
//    @SerializedName("question_type") val questionType: String = "",
    @SerializedName("qa_chapter_id") val qaChapterId: Int = 0,
    @SerializedName("qa_course_id") val qaCourseId: Int = 0
) : Serializable

class TrueFalseQuestionData(
    @SerializedName("answers") val answers: List<String>
) : BaseQuestionData()

class MultipleChoiceQuestionData(
    @SerializedName("answers") val answers: List<String>
) : BaseQuestionData()

class SortingQuestionData(
    @SerializedName("answers") val answers: List<SortingAnswer>
) : BaseQuestionData()

data class SortingAnswer(
    @SerializedName("ansUpText") val ansUpText: String,
    @SerializedName("ansDownText") val ansDownText: String,
    @SerializedName("concepts") val concepts: List<String>
)