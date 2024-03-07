package com.learnitevekri.ui.feature.splash.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.learnitevekri.data.SharedPreferences

class SplashViewModel : ViewModel() {

    companion object {
        val TAG: String = SplashViewModel::class.java.simpleName
    }

    fun verifyTokenValid(): Boolean {
        Log.d(TAG, "verifyTokenValid: ${SharedPreferences.getExpiresTime()}")
        return SharedPreferences.getExpiresTime() > System.currentTimeMillis()
    }

}