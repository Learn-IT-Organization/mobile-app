package com.example.learnit.ui.feature.splash.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.data.SharedPreferences
import com.example.learnit.data.courses.notifications.TokenData
import com.example.learnit.domain.login.LoginRepository
import com.example.learnit.ui.App
import com.example.learnit.ui.feature.courses.courses.viewModel.CoursesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val repository: LoginRepository = App.instance.getLoginRepository()

    fun verifyTokenValid(): Boolean {
        Log.d("SplashViewModel", "verifyTokenValid: ${SharedPreferences.getExpiresTime()}")
        return SharedPreferences.getExpiresTime() > System.currentTimeMillis()
    }

    fun sendFCMToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.sendFCMToken(TokenData(token))
            } catch (e: Exception) {
                Log.e(CoursesViewModel.TAG, "Error fetching courses: ${e.message}")
            }
        }
    }

}