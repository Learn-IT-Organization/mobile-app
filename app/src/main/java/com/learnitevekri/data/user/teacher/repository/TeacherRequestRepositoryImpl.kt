package com.learnitevekri.data.user.teacher.repository

import android.util.Log
import com.learnitevekri.data.RetrofitAdapter
import com.learnitevekri.data.user.login.repository.UserRepositoryImpl
import com.learnitevekri.data.user.teacher.model.Message
import com.learnitevekri.data.user.teacher.model.TeacherRequestData
import com.learnitevekri.data.user.teacher.model.TeacherRequestDataFull
import com.learnitevekri.data.user.teacher.model.TeacherRequestInfo
import com.learnitevekri.data.user.teacher.model.TeacherRequestResponseData
import com.learnitevekri.domain.user.TeacherRequestRepository

object TeacherRequestRepositoryImpl : TeacherRequestRepository {
    private val apiService = RetrofitAdapter.provideApiService()
    private val TAG = UserRepositoryImpl::class.java.simpleName

    override suspend fun getTeacherRequests(): List<TeacherRequestDataFull> {
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

    override suspend fun sendTeacherRequest(teacherRequestData: TeacherRequestData): TeacherRequestResponseData? {
        val apiService = RetrofitAdapter.provideApiService()
        val response = apiService.sendTeacherRequest(teacherRequestData)
        if (response.isSuccessful && response.body() != null) {
            return response.body()
        }
        return null
    }

    override suspend fun getUserRequests(): TeacherRequestDataFull? {
        try {
            val response = apiService.getUserRequests()
            if (response.isSuccessful) {
                return response.body()
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching teacher requests: ${e.message}")
            throw e
        }
        return null
    }
}