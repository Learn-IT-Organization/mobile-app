package com.learnitevekri.domain.course

import com.learnitevekri.data.courses.course.model.CourseData

interface CourseRepository {
    suspend fun getCourses(): List<CourseData>
    suspend fun getMyCourses(): List<CourseData>
}