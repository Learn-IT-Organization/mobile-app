package com.learnitevekri.ui.feature.splash.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnitevekri.data.RetrofitAdapter
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.domain.course.CourseRepository
import com.learnitevekri.ui.App
import com.learnitevekri.ui.feature.splash.SplashNavigationListener
import kotlinx.coroutines.launch
import java.io.IOException

class SplashViewModel : ViewModel() {

    private var navigationListener: SplashNavigationListener? = null

    fun setNavigationListener(listener: SplashNavigationListener) {
        navigationListener = listener
    }

    companion object {
        val TAG: String = SplashViewModel::class.java.simpleName
    }

    fun verifyTokenValid(): Boolean {
        Log.d(TAG, "verifyTokenValid: ${SharedPreferences.getExpiresTime()}")
        return SharedPreferences.getExpiresTime() > System.currentTimeMillis()
    }

    private val apiService = RetrofitAdapter.provideApiService()

    fun checkTheServer() {
        viewModelScope.launch {
            try {
                val courses = apiService.getCourses()
                Log.d(TAG, "Server is reachable, received courses: $courses")
                if (courses.isSuccessful && courses.body() != null) {
                    Log.d(TAG, "Server is reachable, received courses: $courses")
                    navigationListener?.initSplashScreen()
                } else if (courses.isSuccessful && courses.body() == null) {
                    Log.d(TAG, "Server is reachable, but no courses received")
                    navigationListener?.navigateToNoConnectionFragment()
                }
                    else if (courses.code() == 401) {
                    Log.d(TAG, "Token expired")
                    navigationListener?.navigateToLoginFragment()
                }
            } catch (e: IOException) {
                Log.e(TAG, "Network error: ${e.message}")
                navigationListener?.navigateToErrorFragment()
            } catch (e: Exception) {
                Log.e(TAG, "Server is not reachable: ${e.message}")
                navigationListener?.navigateToErrorFragment()
            }
        }
    }
}