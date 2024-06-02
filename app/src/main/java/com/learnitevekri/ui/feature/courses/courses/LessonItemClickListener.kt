package com.learnitevekri.ui.feature.courses.courses

import com.learnitevekri.data.courses.lessons.model.AddNewLessonData
import com.learnitevekri.data.courses.lessons.model.EditLessonData

interface LessonItemClickListener {
    fun onEditClick(addNewLessonData: AddNewLessonData)
    fun onSaveClick(currentLessonId: Int, editLessonData: EditLessonData)
}