package com.learnitevekri.domain.course

import com.learnitevekri.data.courses.course.model.AddNewCourseData
import com.learnitevekri.data.courses.course.model.AddNewCourseResponseData
import com.learnitevekri.data.courses.course.model.CourseData

interface CourseRepository {
    suspend fun getCourses(): List<CourseData>
    suspend fun getMyCourses(): List<CourseData>
    suspend fun addNewCourse(addNewCourseData: AddNewCourseData): Int?
}