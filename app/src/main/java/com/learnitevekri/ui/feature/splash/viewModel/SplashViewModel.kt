package com.learnitevekri.ui.feature.splash.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.courses.notifications.TokenData
import com.learnitevekri.domain.login.LoginRepository
import com.learnitevekri.ui.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    private val repository: LoginRepository = App.instance.getLoginRepository()


    companion object {
        val TAG: String = SplashViewModel::class.java.simpleName
    }

    fun verifyTokenValid(): Boolean {
        Log.d(TAG, "verifyTokenValid: ${SharedPreferences.getExpiresTime()}")
        return SharedPreferences.getExpiresTime() > System.currentTimeMillis()
    }

    fun sendFCMToken(token: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                repository.sendFCMToken(TokenData(token))
            } catch (e: Exception) {
                Log.e(TAG, "sendFCMToken: $e")
            }
        }
    }

}