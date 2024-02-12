package com.example.learnit.ui.feature.register.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.data.user.register.model.RegistrationData
import com.example.learnit.data.user.register.model.RegistrationResponseData
import com.example.learnit.ui.App
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    companion object {
        val TAG: String = RegisterViewModel::class.java.simpleName
    }

    sealed class RegisterPageState {
        data object Loading : RegisterPageState()
        data class Success(val registerData: RegistrationResponseData?) : RegisterPageState()
        data class Failure(val throwable: Throwable) : RegisterPageState()
    }

    private val mutableState =
        MutableStateFlow<RegisterPageState>(RegisterPageState.Loading)
    val state: StateFlow<RegisterPageState> = mutableState

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = RegisterPageState.Failure(exception)
    }

    fun registerUser(uiRegisterForm: RegistrationData) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val registrationResponse =
                    App.instance.getRegisterRepository().registerUser(uiRegisterForm)
                mutableState.value = RegisterPageState.Success(registrationResponse)
            } catch (e: Exception) {
                mutableState.value = RegisterPageState.Failure(e)
            }
        }
    }

}