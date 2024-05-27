package com.learnitevekri.domain.user

import com.learnitevekri.data.user.teacher.model.Message
import com.learnitevekri.data.user.teacher.model.TeacherRequestData
import com.learnitevekri.data.user.teacher.model.TeacherRequestInfo

interface TeacherRequestRepository {
    suspend fun getTeacherRequests(): List<TeacherRequestData>
    suspend fun acceptTeacherRequest(teacherRequestInfo: TeacherRequestInfo): Message
    suspend fun declineTeacherRequest(teacherRequestInfo: TeacherRequestInfo): Message
}