package com.learnitevekri.data.user.logout

import android.util.Log
import com.learnitevekri.data.RetrofitAdapter
import com.learnitevekri.domain.logout.LogOutRepository

object LogOutRepositoryImpl : LogOutRepository {
    private val TAG = LogOutRepositoryImpl::class.java.simpleName
    private val apiService = RetrofitAdapter.provideApiService()

    override suspend fun logOut(): LogoutResponseData {
        try {
            val response = apiService.logOut()
            if (response.isSuccessful) {
                val responseData = response.body()
                Log.d(TAG, response.raw().toString())
                return (responseData ?: LogoutResponseData())
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error: ${e.message}")
            throw e
        }
        return LogoutResponseData()
    }
}