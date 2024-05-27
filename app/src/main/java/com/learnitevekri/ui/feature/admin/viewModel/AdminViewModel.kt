package com.learnitevekri.ui.feature.admin.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnitevekri.data.user.teacher.model.TeacherRequestData
import com.learnitevekri.domain.user.TeacherRequestRepository
import com.learnitevekri.ui.App
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminViewModel : ViewModel() {
    private val repository: TeacherRequestRepository = App.instance.getTeacherRequestRepository()

    private val _state = MutableStateFlow<TeacherRequestsScreenState>(TeacherRequestsScreenState.Loading)
    val state: StateFlow<TeacherRequestsScreenState> = _state

    companion object {
        val TAG: String = AdminViewModel::class.java.simpleName
    }

    sealed class TeacherRequestsScreenState {
        object Loading : TeacherRequestsScreenState()
        data class Success(val teacherRequests: List<TeacherRequestData>) : TeacherRequestsScreenState()
        data class Failure(val throwable: Throwable) : TeacherRequestsScreenState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        _state.value = TeacherRequestsScreenState.Failure(exception)
    }

    fun loadTeacherRequests() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val requests = repository.getTeacherRequests()
                _state.value = TeacherRequestsScreenState.Success(requests)
            } catch (e: Exception) {
                _state.value = TeacherRequestsScreenState.Failure(e)
            }
        }
    }

}