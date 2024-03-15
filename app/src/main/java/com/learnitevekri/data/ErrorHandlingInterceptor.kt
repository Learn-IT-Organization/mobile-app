package com.learnitevekri.data

import android.util.Log
import androidx.navigation.fragment.findNavController
import com.learnitevekri.R
import okhttp3.Interceptor
import okhttp3.Response

class ErrorHandlingInterceptor() : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        Log.d("ErrorHandlingInterceptor", "intercept: ${request.url}")
        val response: Response
        try {
            response = chain.proceed(request)
            Log.d("ErrorHandlingInterceptor", "intercept: ${response.code}")
        } catch (e: Exception) {
            Log.e("ErrorHandlingInterceptor", "Network error: ${e.message}")
            throw e
        }
        return response
    }
}