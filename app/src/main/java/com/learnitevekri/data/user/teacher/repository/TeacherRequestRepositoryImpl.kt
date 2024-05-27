package com.learnitevekri.data.user.teacher.repository

import android.util.Log
import com.learnitevekri.data.RetrofitAdapter
import com.learnitevekri.data.user.login.repository.UserRepositoryImpl
import com.learnitevekri.data.user.teacher.model.Message
import com.learnitevekri.data.user.teacher.model.TeacherRequestData
import com.learnitevekri.data.user.teacher.model.TeacherRequestInfo
import com.learnitevekri.domain.user.TeacherRequestRepository

object TeacherRequestRepositoryImpl : TeacherRequestRepository {
    private val apiService = RetrofitAdapter.provideApiService()
    private val TAG = UserRepositoryImpl::class.java.simpleName

    override suspend fun getTeacherRequests(): List<TeacherRequestData> {
        try {
            val response = apiService.getTeacherRequests()
            if (response.isSuccessful) {
                val responseData = response.body()
                val data = responseData ?: emptyList()
                Log.d(TAG, response.raw().toString())
                return data
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching teacher requests: ${e.message}")
            throw e
        }
        return emptyList()
    }

    override suspend fun acceptTeacherRequest(teacherRequestInfo: TeacherRequestInfo): Message {
        val response = apiService.acceptTeacherRequest(teacherRequestInfo)
        if (response.isSuccessful) {
            val responseData = response.body()
            Log.d(TAG, response.raw().toString())
            return responseData!!
        }
        return Message("Failed to accept teacher request")
    }

    override suspend fun declineTeacherRequest(teacherRequestInfo: TeacherRequestInfo): Message {
        val response = apiService.declineTeacherRequest(teacherRequestInfo)
        if (response.isSuccessful) {
            val responseData = response.body()
            Log.d(TAG, response.raw().toString())
            return responseData!!
        }
        return Message("Failed to decline teacher request")
    }
}