package com.learnitevekri.data.courses.course.repository

import android.util.Log
import com.learnitevekri.data.RetrofitAdapter
import com.learnitevekri.data.courses.course.model.AddNewCourseData
import com.learnitevekri.data.courses.course.model.CourseData
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
}