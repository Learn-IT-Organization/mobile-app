package com.learnitevekri.data.user.teacher.repository

import com.learnitevekri.data.RetrofitAdapter
import com.learnitevekri.data.user.teacher.model.TeacherRequestData
import com.learnitevekri.data.user.teacher.model.TeacherRequestResponseData
import com.learnitevekri.domain.user.TeacherRequestRepository

object TeacherRequestRepositoryImpl : TeacherRequestRepository {
    override suspend fun sendTeacherRequest(teacherRequestData: TeacherRequestData): TeacherRequestResponseData? {
        val apiService = RetrofitAdapter.provideApiService()
        val response = apiService.sendTeacherRequest(teacherRequestData)
        if (response.isSuccessful && response.body() != null) {
            return response.body()
        }
        return null
    }
}