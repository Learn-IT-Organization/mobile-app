package com.example.learnit.data.courses.mapper

import com.example.learnit.data.courses.model.CourseData
import com.example.learnit.ui.feature.courses.model.CourseModel

fun CourseData.mapToCourse() = CourseModel(
    courseId = this.course_id,
    courseName = this.course_name,
    createDate = this.create_date,
    programmingLanguage = this.programming_language,
    courseDescription = this.course_description,
    courseUserId = this.course_user_id
)

fun List<CourseData>.mapToCourseList() = map { it.mapToCourse() }