package com.example.learnit.domain.course

import com.example.learnit.data.courses.course.model.CourseData

interface CourseRepository {
    suspend fun getCourses(): List<CourseData>
    suspend fun getMyCourses(): List<CourseData>
}