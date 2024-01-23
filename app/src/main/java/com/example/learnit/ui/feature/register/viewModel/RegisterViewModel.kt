package com.example.learnit.ui.feature.register.viewModel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.data.user.register.mapper.mapToRegistration
import com.example.learnit.data.user.register.model.RegistrationResponseData
import com.example.learnit.ui.App
import com.example.learnit.ui.feature.register.model.RegistrationModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class RegisterViewModel : ViewModel() {
    sealed class RegisterPageState {
        object Loading : RegisterPageState()
        data class Success(val registerData: RegistrationResponseData?) : RegisterPageState()
        data class Failure(val throwable: Throwable) : RegisterPageState()
    }

    private val mutableState =
        MutableStateFlow<RegisterPageState>(RegisterPageState.Loading)
    val state: StateFlow<RegisterPageState> = mutableState

    private val selectedPhotoUri = MutableStateFlow<Uri?>(null)

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = RegisterPageState.Failure(exception)
    }

    fun registerUser(uiRegisterForm: RegistrationModel) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val registrationData = uiRegisterForm.mapToRegistration()
                val registrationResponse =
                    App.instance.getRegisterRepository().registerUser(registrationData)
                mutableState.value = RegisterPageState.Success(registrationResponse)
            } catch (e: Exception) {
                mutableState.value = RegisterPageState.Failure(e)
            }
        }
    }

    fun setPhotoUri(uri: Uri) {
        selectedPhotoUri.value = uri
    }

    fun getPhotoUri(): Uri? {
        return selectedPhotoUri.value
    }
}