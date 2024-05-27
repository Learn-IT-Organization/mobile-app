package com.learnitevekri.domain.user

import com.learnitevekri.data.user.teacher.model.TeacherRequestData
import com.learnitevekri.data.user.teacher.model.TeacherRequestResponseData

interface TeacherRequestRepository {
    suspend fun sendTeacherRequest(teacherRequestData: TeacherRequestData): TeacherRequestResponseData?

}