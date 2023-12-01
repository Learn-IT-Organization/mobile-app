package com.example.learnit.ui.feature.login.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.data.user.login.model.Data
import com.example.learnit.data.user.login.model.ResponseData
import com.example.learnit.data.user.model.LoginData
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
        object Loading : LoginPageState()
        data class Success(val loginData: ResponseData<Data>?) : LoginPageState()
        data class Failure(val throwable: Throwable) : LoginPageState()
    }

    private val mutableState = MutableStateFlow<LoginPageState>(LoginPageState.Loading)
    val state: StateFlow<LoginPageState> = mutableState

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = LoginPageState.Failure(exception)
    }

    fun loadLoginInfo(uiLoginForm: LoginModel) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            val loginFormBo = uiLoginForm.userName?.let { uiLoginForm.userPassword?.let { it1 ->
                LoginData(it,
                    it1
                )
            } }
            val loginData = loginFormBo?.let {
                App.instance.getLoginRepository().getLoginInformation(
                    it
                )
            }
            if (loginData != null) {
                if (loginData.success) {
                    mutableState.value = LoginPageState.Success(loginData)
                } else {
                    mutableState.value = LoginPageState.Failure(IOException("Invalid username or password"))
                }
            } else {
                mutableState.value = LoginPageState.Failure(IOException())
            }
        }
    }
}