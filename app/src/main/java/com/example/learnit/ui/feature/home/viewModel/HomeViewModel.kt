package com.example.learnit.ui.feature.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.domain.user.repository.UserRepository
import com.example.learnit.ui.App
import com.example.learnit.ui.feature.home.model.LoggedUserModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    private val repository: UserRepository = App.instance.getUserRepository()
    private var userList: List<LoggedUserModel> = mutableListOf()

    companion object {
        val TAG = HomeViewModel::class.java.simpleName
    }

    sealed class UserPageState {
        object Loading : UserPageState()
        data class Success(val userData: List<LoggedUserModel>) : UserPageState()
        data class Failure(val throwable: Throwable) : UserPageState()
    }

    private val mutableState = MutableStateFlow<UserPageState>(UserPageState.Loading)
    val state: StateFlow<UserPageState> = mutableState

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = UserPageState.Failure(exception)
    }

    init {
        loadAndLogUsers()
    }

    private fun loadAndLogUsers() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                userList = repository.getUsers()
                mutableState.value = UserPageState.Success(userList)
                for (user in userList) {
                    Log.d(TAG, "User: $user")

                }
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching users: ${e.message}")

                mutableState.value = UserPageState.Failure(e)
            }
        }
    }
}