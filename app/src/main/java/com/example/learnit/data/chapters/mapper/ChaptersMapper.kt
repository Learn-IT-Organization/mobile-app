package com.example.learnit.data.chapters.mapper

import com.example.learnit.data.chapters.model.ChaptersData
import com.example.learnit.ui.feature.chapters.model.ChapterModel

fun ChaptersData.mapToChapter() = ChapterModel(
    chapterId = this.chapter_id,
    chapterName = this.chapter_name ?: "",
    chapterCourseId = this.chapter_course_id,
    chapterDescription = this.chapter_description ?: "",
    chapterSequenceNumber = this.chapter_sequence_number
)
fun List<ChaptersData>.mapToChapterList() = map { it.mapToChapter() }