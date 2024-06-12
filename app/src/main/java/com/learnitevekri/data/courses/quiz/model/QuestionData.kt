package com.learnitevekri.data.courses.quiz.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class AddSortingAnswer(
    @SerializedName("ansUpText") val ansUpText: String,
    @SerializedName("ansDownText") val ansDownText: String,
    @SerializedName("up") val up: List<String>,
    @SerializedName("down") val down: List<String>,
) : Serializable

data class AddMatchingAnswer(
    @SerializedName("textLeft") val textLeft: String,
    @SerializedName("textRight") val textRight: String,
) : Serializable

data class AddTrueFalseQuestionData(
    @SerializedName("qa_lesson_id") val qaLessonId: Int,
    @SerializedName("question_id") val questionId: Int,
    @SerializedName("question_text") val questionText: String?,
    @SerializedName("question_type") val questionType: String,
    @SerializedName("answers") val answers: List<AddMultipleChoiceAnswer>,
    @SerializedName("qa_chapter_id") val qaChapterId: Int,
    @SerializedName("qa_course_id") val qaCourseId: Int,
    @SerializedName("question_user_id") val questionUserId: Int,
    ) : Serializable

data class AddMultipleChoiceAnswer(
    @SerializedName("is_correct") val isCorrect: Boolean,
    @SerializedName("option_text") val optionText: String,
) : Serializable

data class AddMultipleChoiceQuestionData(
    @SerializedName("qa_lesson_id") val qaLessonId: Int,
    @SerializedName("question_text") val questionText: String?,
    @SerializedName("question_type") val questionType: String,
    @SerializedName("answers") val answers: List<AddMultipleChoiceAnswer>,
    @SerializedName("qa_chapter_id") val qaChapterId: Int,
    @SerializedName("qa_course_id") val qaCourseId: Int,
    @SerializedName("question_user_id") val questionUserId: Int,

    ) : Serializable

data class AddSortingQuestionData(
    @SerializedName("qa_lesson_id") val qaLessonId: Int,
    @SerializedName("question_id") val questionId: Int,
    @SerializedName("question_text") val questionText: String?,
    @SerializedName("question_type") val questionType: String,
    @SerializedName("answers") val answers: List<AddSortingAnswer>,
    @SerializedName("qa_chapter_id") val qaChapterId: Int,
    @SerializedName("qa_course_id") val qaCourseId: Int,
    @SerializedName("question_user_id") val questionUserId: Int,

    ) : Serializable

data class AddMatchingQuestionData(
    @SerializedName("qa_lesson_id") val qaLessonId: Int,
    @SerializedName("question_id") val questionId: Int,
    @SerializedName("question_text") val questionText: String?,
    @SerializedName("question_type") val questionType: String,
    @SerializedName("answers") val answers: List<AddMatchingAnswer>,
    @SerializedName("qa_chapter_id") val qaChapterId: Int,
    @SerializedName("qa_course_id") val qaCourseId: Int,
    @SerializedName("question_user_id") val questionUserId: Int,

    ) : Serializable