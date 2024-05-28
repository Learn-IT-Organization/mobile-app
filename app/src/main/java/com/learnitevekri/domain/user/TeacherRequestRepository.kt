package com.learnitevekri.domain.user

import com.learnitevekri.data.user.teacher.model.Message
import com.learnitevekri.data.user.teacher.model.TeacherRequestData
import com.learnitevekri.data.user.teacher.model.TeacherRequestDataFull
import com.learnitevekri.data.user.teacher.model.TeacherRequestInfo
import com.learnitevekri.data.user.teacher.model.TeacherRequestResponseData

interface TeacherRequestRepository {
    suspend fun getTeacherRequests(): List<TeacherRequestDataFull>
    suspend fun acceptTeacherRequest(teacherRequestInfo: TeacherRequestInfo): Message
    suspend fun declineTeacherRequest(teacherRequestInfo: TeacherRequestInfo): Message
    suspend fun sendTeacherRequest(teacherRequestData: TeacherRequestData): TeacherRequestResponseData?
    suspend fun getUserRequests(): TeacherRequestDataFull?
}