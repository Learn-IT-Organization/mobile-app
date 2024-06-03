package com.learnitevekri.data.courses.course.repository

import android.util.Log
import com.learnitevekri.data.RetrofitAdapter
import com.learnitevekri.data.courses.course.model.AddNewCourseData
import com.learnitevekri.data.courses.course.model.AddNewCourseResponseData
import com.learnitevekri.data.courses.course.model.CourseData
import com.learnitevekri.data.courses.course.model.EditCourseData
import com.learnitevekri.domain.course.CourseRepository

object CourseRepositoryImpl : CourseRepository {
    private val apiService = RetrofitAdapter.provideApiService()
    val TAG: String = CourseRepositoryImpl::class.java.simpleName

    override suspend fun getCourses(): List<CourseData> {
        try {
            val response = apiService.getCourses()

            if (response.isSuccessful) {
                val responseData = response.body()
                return responseData ?: emptyList()
            } else {
                Log.e(TAG, "Error fetching courses1: ${response.code()}")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching courses2: ${e.message}")
            throw e
        }
        return emptyList()
    }

    override suspend fun getMyCourses(): List<CourseData> {
        try {
            val response = apiService.getMyCourses()
            if (response.isSuccessful) {
                val responseData = response.body()
                return responseData ?: emptyList()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching my courses: ${e.message}")
            throw e
        }
        return emptyList()
    }

    override suspend fun addNewCourse(addNewCourseData: AddNewCourseData): Int? {
        try {
            val response = apiService.addNewCourse(addNewCourseData)
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, response.body()?.courseId.toString())
                return response.body()?.courseId
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error adding new course: ${e.message}")
            throw e
        }
        return null
    }

    override suspend fun editCourse(
        courseId: Int, editCourseData: EditCourseData
    ): AddNewCourseResponseData {
        try {
            val response = apiService.editCourse(courseId, editCourseData)
            Log.d(TAG, "Attempt to update course with ID $courseId")
            if (response.isSuccessful && response.body() != null) {
                Log.d(TAG, "Course updated successfully: ${response.body()}")
                return response.body()!!
            } else {
                Log.e(TAG, "Failed to update course: ${response.errorBody()?.string()}")
                throw RuntimeException("Failed to update course due to server error")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error updating course: ${e.message}")
            throw e
        }
    }

    override suspend fun getCourseById(courseId: Int): CourseData {
        try {
            val response = apiService.getCourseById(courseId)
            if (response.isSuccessful) {
                return response.body()!!
            } else {
                Log.e(TAG, "Error fetching course by ID: ${response.code()}")
                throw RuntimeException("Failed to fetch course by ID")
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching course by ID: ${e.message}")
            throw e
        }
    }
}