package com.learnitevekri.domain.course

import com.learnitevekri.data.courses.course.model.AddNewCourseData
import com.learnitevekri.data.courses.course.model.AddNewCourseResponseData
import com.learnitevekri.data.courses.course.model.CourseData
import com.learnitevekri.data.courses.course.model.EditCourseData

interface CourseRepository {
    suspend fun getCourses(): List<CourseData>
    suspend fun getMyCourses(): List<CourseData>
    suspend fun addNewCourse(addNewCourseData: AddNewCourseData): Int?
    suspend fun editCourse(courseId: Int, editCourseData: EditCourseData): AddNewCourseResponseData
    suspend fun getCourseById(courseId: Int): CourseData
}