package com.example.learnit.domain

import com.example.learnit.ui.feature.courses.courses.model.CourseModel
interface CourseRepository {
    suspend fun getCourses(): List<CourseModel>
}