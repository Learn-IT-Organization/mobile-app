package com.learnitevekri.ui.feature.courses.courses.viewModel

import androidx.lifecycle.ViewModel
import com.learnitevekri.data.user.teacher.model.TeacherRequestData
import com.learnitevekri.domain.user.TeacherRequestRepository
import com.learnitevekri.ui.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TeacherRequestViewModel : ViewModel() {

    private val repository: TeacherRequestRepository = App.instance.getTeacherRequestRepository()

    companion object {
        val TAG: String = TeacherRequestViewModel::class.java.simpleName
    }

    suspend fun sendTeacherRequest(teacherRequestData: TeacherRequestData): Boolean {
        return withContext(Dispatchers.IO) {
            try {
                repository.sendTeacherRequest(teacherRequestData)
                true
            } catch (e: Exception) {
                false
            }
        }
    }
}