package com.example.learnit.ui.feature.splash.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.learnit.data.SharedPreferences

class SplashViewModel : ViewModel() {
    fun verifyTokenValid(): Boolean {
        Log.d("SplashViewModel", "verifyTokenValid: ${SharedPreferences.getExpiresTime()}")
        return SharedPreferences.getExpiresTime() > System.currentTimeMillis()
    }
}