package com.example.learnit.domain.course.repository

import com.example.learnit.ui.feature.courses.courses.model.CourseModel
import com.example.learnit.ui.feature.chapters.model.ChapterModel
import com.example.learnit.ui.feature.courses.model.CourseModel

interface CourseRepository {
    suspend fun getCourses(): List<CourseModel>
}