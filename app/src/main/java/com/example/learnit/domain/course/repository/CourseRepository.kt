package com.example.learnit.domain.course.repository

import com.example.learnit.ui.feature.courses.model.CourseModel

interface CourseRepository {
    suspend fun getCourses(): List<CourseModel>
}