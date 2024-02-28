package com.example.learnit.ui.feature.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.data.user.login.model.Data
import com.example.learnit.data.user.login.model.ForgotPasswordData
import com.example.learnit.data.user.login.model.LoginData
import com.example.learnit.data.user.login.model.ResetCodeData
import com.example.learnit.data.user.login.model.ResponseData
import com.example.learnit.ui.App
import com.example.learnit.ui.feature.login.model.LoginModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException

class LoginViewModel : ViewModel() {
    sealed class LoginPageState {
        data object Loading : LoginPageState()
        data class Success(val loginData: ResponseData<Data>?) : LoginPageState()
        data class Failure(val throwable: Throwable) : LoginPageState()
    }

    sealed class ResetCodeState {
        data object Loading : ResetCodeState()
        data class Success(val forgotPasswordData: ForgotPasswordData) : ResetCodeState()
        data class PasswordChangedSuccess(val loginData: ResponseData<Data>?) : ResetCodeState()
        data class Failure(val throwable: Throwable) : ResetCodeState()
    }

    private val mutableResetCodeState =
        MutableStateFlow<ResetCodeState>(ResetCodeState.Failure(IOException()))
    val resetCodeState: StateFlow<ResetCodeState> = mutableResetCodeState

    private val mutableState = MutableStateFlow<LoginPageState>(LoginPageState.Loading)
    val state: StateFlow<LoginPageState> = mutableState

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = LoginPageState.Failure(exception)
    }

    fun loadLoginInfo(uiLoginForm: LoginModel) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            val loginFormBo = uiLoginForm.userName?.let {
                uiLoginForm.userPassword?.let { it1 ->
                    LoginData(
                        it,
                        it1
                    )
                }
            }
            val loginData = loginFormBo?.let {
                App.instance.getLoginRepository().getLoginInformation(
                    it
                )
            }
            if (loginData != null) {
                if (loginData.success) {
                    mutableState.value = LoginPageState.Success(loginData)
                } else {
                    mutableState.value =
                        LoginPageState.Failure(IOException("Invalid username or password"))
                }
            } else {
                mutableState.value = LoginPageState.Failure(IOException())
            }
        }
    }

    fun sendPasswordReset(forgotPasswordData: ForgotPasswordData) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val response =
                    App.instance.getLoginRepository().requestResetCode(forgotPasswordData)
                if (response.success) {
                    mutableResetCodeState.value = ResetCodeState.Success(forgotPasswordData)
                } else {
                    mutableState.value =
                        LoginPageState.Failure(IOException("Password reset failed"))
                }
            } catch (e: Exception) {
                mutableResetCodeState.value = ResetCodeState.Failure(e)
            }
        }
    }

    fun changePasswordWithResetCode(resetCodeData: ResetCodeData) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val response =
                    App.instance.getLoginRepository().changePasswordWithResetCode(resetCodeData)
                if (response.success) {
                    mutableResetCodeState.value = ResetCodeState.PasswordChangedSuccess(null)
                } else {
                    mutableState.value =
                        LoginPageState.Failure(IOException("Password reset failed"))
                }
            } catch (e: Exception) {
                mutableResetCodeState.value = ResetCodeState.Failure(e)
            }
        }
    }

}