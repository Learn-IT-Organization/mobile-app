package com.learnitevekri.ui.feature.courses.courses.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnitevekri.data.courses.course.model.AddNewCourseData
import com.learnitevekri.data.courses.course.model.AddNewCourseResponseData
import com.learnitevekri.data.courses.course.model.CourseData
import com.learnitevekri.data.courses.course.model.EditCourseData
import com.learnitevekri.domain.course.CourseRepository
import com.learnitevekri.ui.App
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CoursesViewModel : ViewModel() {
    private val repository: CourseRepository = App.instance.getCourseRepository()

    private val mutableState = MutableStateFlow<CoursesPageState>(CoursesPageState.Loading)
    val state: StateFlow<CoursesPageState> = mutableState

    private val mutableState2 = MutableStateFlow<EditCourseState>(EditCourseState.Loading)
    val state2: StateFlow<EditCourseState> = mutableState2

    companion object {
        val TAG: String = CoursesViewModel::class.java.simpleName
    }

    sealed class CoursesPageState {
        object Loading : CoursesPageState()
        data class Success(val courseData: List<CourseData>) : CoursesPageState()
        data class Failure(val throwable: Throwable) : CoursesPageState()
    }

    sealed class EditCourseState {
        object Loading : EditCourseState()
        data class Success(val courseData: CourseData) : EditCourseState()
        data class Updated(val response: AddNewCourseResponseData) : EditCourseState()
        data class Failure(val throwable: Throwable) : EditCourseState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = CoursesPageState.Failure(exception)
    }

    init {
        loadCourses()
    }

    fun loadCourses() {
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

    fun loadCourseById(courseId: Int) {
        viewModelScope.launch {
            try {
                val course = repository.getCourseById(courseId)
                mutableState2.value = EditCourseState.Success(course)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching course by id: ${e.message}")
                mutableState2.value = EditCourseState.Failure(e)
            }
        }
    }

    suspend fun addNewCourse(addNewCourseData: AddNewCourseData): Int? {
        return withContext(Dispatchers.IO) {
            try {
                repository.addNewCourse(addNewCourseData)
            } catch (e: Exception) {
                Log.e(TAG, "Error adding new course: ${e.message}")
                null
            }
        }
    }

    suspend fun editCourse(
        courseId: Int, editCourseData: EditCourseData
    ): AddNewCourseResponseData {
        return withContext(Dispatchers.IO) {
            try {
                repository.editCourse(courseId, editCourseData).also {
                    Log.d(TAG, "Course updated successfully: $courseId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating course: ${e.message}")
                throw e
            }
        }
    }

    fun deleteCourse(courseId: Int) {
        viewModelScope.launch {
            val isSuccess = try {
                withContext(Dispatchers.IO) {
                    repository.deleteCourse(courseId)
                }
                true
            } catch (e: Exception) {
                Log.e(TAG, "Error deleting course: ${e.message}")
                false
            }

            if (isSuccess) {
                loadCourses()
            }
        }
    }
}