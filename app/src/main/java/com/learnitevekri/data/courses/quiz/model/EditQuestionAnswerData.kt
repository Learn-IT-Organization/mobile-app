package com.learnitevekri.data.courses.quiz.model

import com.google.gson.annotations.SerializedName

class EditQuestionAnswerData<T>(
    @SerializedName("question_id")
    var question_text: String,
    @SerializedName("answers")
    var answers: T
) : java.io.Serializable