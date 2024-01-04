package com.example.learnit.ui.feature.courses.courses.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.learnit.domain.course.repository.CourseRepository
import com.example.learnit.ui.App
import com.example.learnit.ui.feature.courses.courses.model.CourseModel
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class CoursesViewModel : ViewModel() {
    private val repository: CourseRepository = App.instance.getCourseRepository()

    private val mutableState = MutableStateFlow<CoursesPageState>(CoursesPageState.Loading)
    val state: StateFlow<CoursesPageState> = mutableState

    companion object {
        val TAG: String = CoursesViewModel::class.java.simpleName
    }

    sealed class CoursesPageState {
        object Loading : CoursesPageState()
        data class Success(val courseData: List<CourseModel>) : CoursesPageState()
        data class Failure(val throwable: Throwable) : CoursesPageState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = CoursesPageState.Failure(exception)
    }

    init {
        loadCourses()
    }

    private fun loadCourses() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val loadedCourses = repository.getCourses()
                mutableState.value = CoursesPageState.Success(loadedCourses)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching courses: ${e.message}")
                mutableState.value = CoursesPageState.Failure(e)
            }
        }
    }
}