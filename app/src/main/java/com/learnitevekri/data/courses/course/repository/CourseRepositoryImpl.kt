package com.learnitevekri.data.courses.course.repository

import android.util.Log
import com.learnitevekri.data.RetrofitAdapter
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
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching courses: ${e.message}")
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
        }
        return emptyList()
    }
}