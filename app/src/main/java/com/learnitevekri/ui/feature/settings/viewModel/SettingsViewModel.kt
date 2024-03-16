package com.learnitevekri.ui.feature.settings.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnitevekri.data.user.logout.LogoutResponseData
import com.learnitevekri.ui.App
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException


class SettingsViewModel : ViewModel() {
    private val TAG = SettingsViewModel::class.java.simpleName
    private val repository = App.instance.getLogoutRepository()
    private val mutableState = MutableStateFlow<LogoutPageState>(LogoutPageState.Loading)
    val state: StateFlow<LogoutPageState> = mutableState

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = LogoutPageState.Failure(exception)
    }
    sealed class LogoutPageState {
        data object Loading : LogoutPageState()
        data class Success(val logoutData: LogoutResponseData) : LogoutPageState()
        data class Failure(val throwable: Throwable) : LogoutPageState()
    }


    fun logOut() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            val logoutData = repository.logOut()
            if (logoutData.success) {
                mutableState.value = LogoutPageState.Success(logoutData)
                Log.d(TAG, "Logout successful")
            } else {
                mutableState.value = LogoutPageState.Failure(IOException())
                Log.d(TAG, "Logout failed")
            }
        }
    }
}