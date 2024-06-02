package com.learnitevekri.ui.feature.courses.courses

import com.learnitevekri.data.courses.chapters.model.AddNewChapterData
import com.learnitevekri.data.courses.chapters.model.EditChapterData

interface ChapterItemClickListener {
    fun onEditClick(addNewChapterData: AddNewChapterData)
    fun onSaveClick(currentChapterId: Int, editChapterData: EditChapterData)
    fun onAddLessonClick(currentPosition: Int):Boolean
}