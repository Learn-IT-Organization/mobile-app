package com.example.learnit.ui.feature.courses.courses.model

import java.util.Date

data class CourseModel(
    //Miert nullable minden parameter?
    val courseId: Int?,
    val courseName: String?,
    val createDate: Date?,
    val programmingLanguage: String?,
    val courseDescription: String?,
    val courseUserId: Int?
)