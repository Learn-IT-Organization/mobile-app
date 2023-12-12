package com.example.learnit.data.courses.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.courses.mapper.mapToCourseList
import com.example.learnit.domain.course.repository.CourseRepository
import com.example.learnit.ui.feature.courses.model.CourseModel

object CourseRepositoryImpl : CourseRepository {
    private val apiService = RetrofitAdapter.provideApiService()

    override suspend fun getCourses(): List<CourseModel> {
        try {
            val response = apiService.getCourses()
            if (response.isSuccessful) {
                val responseData = response.body()
                val data = responseData ?: emptyList()
                Log.d("Response", response.raw().toString())
                return data.mapToCourseList()
            }
        } catch (e: Exception) {
            Log.e("CourseRepository", "Error fetching courses: ${e.message}")
        }
        return emptyList()
    }

}