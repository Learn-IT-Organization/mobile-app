package com.learnitevekri.domain.course

import ChapterResultData
import com.learnitevekri.data.courses.chapters.model.AddNewChapterData
import com.learnitevekri.data.courses.chapters.model.AddNewChapterResponseData
import com.learnitevekri.data.courses.chapters.model.ChapterWithLessonsData
import com.learnitevekri.data.courses.chapters.model.EditChapterData
import com.learnitevekri.data.courses.course.model.AddNewCourseData
import com.learnitevekri.data.courses.course.model.AddNewCourseResponseData

interface ChaptersRepository {
    suspend fun getChaptersByCourseId(courseId: Int): List<ChapterWithLessonsData>
    suspend fun getChapterResult(courseId: Int, chapterId: Int): ChapterResultData
    suspend fun addNewChapter(addNewChapterData: AddNewChapterData): Int?
    suspend fun editChapter(chapterId: Int, editChapterData: EditChapterData): AddNewChapterResponseData
}