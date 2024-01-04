package com.example.learnit.data.courses.course.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.courses.course.mapper.mapToCourseList
import com.example.learnit.domain.course.repository.CourseRepository
import com.example.learnit.ui.feature.courses.courses.model.CourseModel

object CourseRepositoryImpl : CourseRepository {
    private val apiService = RetrofitAdapter.provideApiService()

    override suspend fun getCourses(): List<CourseModel> {
        try {
            val response = apiService.getCourses()
            if (response.isSuccessful) {
                //1 line. Tul sok a foloslegesen letrehozott valtozo
                val responseData = response.body()
                val data = responseData ?: emptyList()
                return data.mapToCourseList()
            }
        } catch (e: Exception) {
            //Szinten egy constans TAG, ne hasznaljunk hardcoded TAG-eket
            Log.e("CourseRepository", "Error fetching courses: ${e.message}")
        }
        return emptyList()
    }
}