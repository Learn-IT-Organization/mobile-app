package com.example.learnit.ui.feature.register.viewModel

import android.net.Uri
import android.provider.MediaStore
import android.util.Log
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

    private val selectedPhotoUri = MutableStateFlow<Uri?>(null)

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = RegisterPageState.Failure(exception)
    }

    fun setPhotoUri(uri: Uri) {
        selectedPhotoUri.value = uri
    }

    fun getPhotoUri(): Uri? {
        return selectedPhotoUri.value
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

    private fun getRealPathFromURI(uri: Uri?): String {
        if (uri == null) {
            Log.e(TAG, "getRealPathFromURI: Uri is null")
            return ""
        }

        Log.d(TAG, "getRealPathFromURI: Uri: $uri")

        val cursor = App.instance.contentResolver.query(uri, null, null, null, null)

        cursor.use { cursor ->
            if (cursor != null && cursor.moveToFirst()) {
                val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                val path = cursor.getString(columnIndex)
                Log.d(TAG, "getRealPathFromURI: Path: $path")
                return path
            } else {
                Log.e(TAG, "getRealPathFromURI: Cursor is null or empty")
            }
        }

        return ""
    }

}