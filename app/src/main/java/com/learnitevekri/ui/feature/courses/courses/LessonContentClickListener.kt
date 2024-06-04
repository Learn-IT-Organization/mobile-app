package com.learnitevekri.ui.feature.courses.courses

import com.learnitevekri.data.courses.lessons.model.EditLessonContentData
import com.learnitevekri.data.courses.lessons.model.LessonContentData

interface LessonContentClickListener {
    fun onEditClick(editLessonContentData: LessonContentData)
    fun onSaveClick(currentLessonContentId: Int, editLessonContentData: EditLessonContentData)
    fun onAddQuestionClick(currentPosition: Int): Boolean
}