package com.learnitevekri.ui.feature.home.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.courses.course.model.CourseData
import com.learnitevekri.data.user.login.model.LoggedUserData
import com.learnitevekri.data.user.teacher.model.TeacherRequestDataFull
import com.learnitevekri.domain.course.CourseRepository
import com.learnitevekri.domain.user.UserRepository
import com.learnitevekri.ui.App
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {
    companion object {
        val TAG: String = HomeViewModel::class.java.simpleName
    }

    private val repository: UserRepository = App.instance.getUserRepository()
    private val teacherRequestRepository = App.instance.getTeacherRequestRepository()
    private val coursesRepository: CourseRepository = App.instance.getCourseRepository()

    private var userList: List<LoggedUserData> = mutableListOf()
    private var courseList: List<CourseData> = mutableListOf()

    private val mutableUserImagePath = MutableStateFlow<String?>(null)

    sealed class UserPageState {
        object Loading : UserPageState()
        data class Success(
            val userData: List<LoggedUserData>,
            val courseData: List<CourseData>,
        ) :
            UserPageState()

        data class ImagePathSuccess(val imagePath: String?) : UserPageState()
        data class Failure(val throwable: Throwable) : UserPageState()
    }

    private val mutableState = MutableStateFlow<UserPageState>(UserPageState.Loading)
    val state: StateFlow<UserPageState> = mutableState

    sealed class TeacherRequestState {
        object Loading : TeacherRequestState()
        data class Success(val teacherRequest: TeacherRequestDataFull?) :
            TeacherRequestState()

        data class Failure(val throwable: Throwable) : TeacherRequestState()
    }

    private val mutableTeacherRequestState = MutableStateFlow<TeacherRequestState>(
        TeacherRequestState.Loading
    )
    val teacherRequestState: StateFlow<TeacherRequestState> = mutableTeacherRequestState

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = UserPageState.Failure(exception)
    }

    fun loadAndLogUsers() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                userList = repository.getUsers()
                courseList = coursesRepository.getMyCourses()
                mutableState.value = UserPageState.Success(userList, courseList)
                Log.d(TAG, courseList.toString())
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching users: ${e.message}")
                mutableState.value = UserPageState.Failure(e)
            }
        }
    }

    fun getUserById(loggedUserId: Long): LoggedUserData? {
        if (userList.isNotEmpty()) {
            return userList.find {
                it.userId == loggedUserId
            }
        }
        return null
    }

    fun getUserImagePath() {
        mutableState.value =
            UserPageState.ImagePathSuccess(SharedPreferences.getUserImagePath(context = App.instance))
    }

    fun setUserImagePath(imagePath: String) {
        mutableUserImagePath.value = imagePath
        SharedPreferences.setUserImagePath(App.instance, imagePath)
    }

    fun getTeacherRequest() {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val teacherRequest = teacherRequestRepository.getUserRequests()
                mutableTeacherRequestState.value = TeacherRequestState.Success(teacherRequest)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching teacher request: ${e.message}")
                mutableTeacherRequestState.value = TeacherRequestState.Failure(e)
            }
        }
    }

}