package com.learnitevekri.domain.course

import ChapterResultData
import com.learnitevekri.data.courses.chapters.model.AddNewChapterData
import com.learnitevekri.data.courses.chapters.model.AddNewChapterResponseData
import com.learnitevekri.data.courses.chapters.model.ChapterData
import com.learnitevekri.data.courses.chapters.model.ChapterWithLessonsData
import com.learnitevekri.data.courses.chapters.model.EditChapterData

interface ChaptersRepository {
    suspend fun getChaptersByCourseId(courseId: Int): List<ChapterWithLessonsData>
    suspend fun getChapterResult(courseId: Int, chapterId: Int): ChapterResultData
    suspend fun addNewChapter(addNewChapterData: AddNewChapterData): Int?
    suspend fun editChapter(
        chapterId: Int, editChapterData: EditChapterData
    ): AddNewChapterResponseData

    suspend fun getChapterById(chapterId: Int): ChapterData

}