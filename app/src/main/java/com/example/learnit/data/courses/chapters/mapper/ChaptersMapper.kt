package com.example.learnit.data.courses.chapters.mapper

import com.example.learnit.data.courses.chapters.model.ChaptersData
import com.example.learnit.ui.feature.courses.chapters.model.ChapterModel
//Folosleges Elvis operatorok
//A "kotlin+java" package name eleg nem helyen valo, oda valami altalanos kene. peldaul "main" vagy valami
fun ChaptersData.mapToChapter() = ChapterModel(
    chapterId = this.chapter_id,
    chapterName = this.chapter_name ?: "",
    chapterCourseId = this.chapter_course_id,
    chapterDescription = this.chapter_description ?: "",
    chapterSequenceNumber = this.chapter_sequence_number
)
fun List<ChaptersData>.mapToChapterList() = map { it.mapToChapter() }