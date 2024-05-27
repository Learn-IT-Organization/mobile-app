package com.learnitevekri.ui.feature.admin

import com.learnitevekri.data.user.teacher.model.TeacherRequestData

interface AdminRequestListener {
    fun onAcceptClicked(request: TeacherRequestData)
    fun onDeclineClicked(request: TeacherRequestData)
}