package com.learnitevekri.ui.feature.courses.courses

import com.learnitevekri.data.courses.lessons.model.LessonData

interface TheoryAdapterListener {
    fun getCurrentLesson() : LessonData

    fun onBackToQuizClick()

}