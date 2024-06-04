package com.learnitevekri.data.courses.quiz.model

import com.google.gson.annotations.SerializedName

class AddQuestionAnswerResponseData (
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("message")
    val message: String,
    @SerializedName("questionsAnswersId")
    val questionsAnswersId: Int,
    @SerializedName("lessonId")
    val lessonId: Int
) : java.io.Serializable