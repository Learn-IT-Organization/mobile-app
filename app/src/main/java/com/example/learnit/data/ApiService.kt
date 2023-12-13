package com.example.learnit.data

import com.example.learnit.data.chapters.model.ChaptersData
import com.example.learnit.data.courses.course.model.CourseData
import com.example.learnit.data.courses.lessons.model.LessonData
import com.example.learnit.data.user.login.model.Data
import com.example.learnit.data.user.login.model.LoggedUserData
import com.example.learnit.data.user.login.model.ResponseData
import com.example.learnit.data.user.login.model.ResponseUserData
import com.example.learnit.data.user.login.model.LoginData
import com.example.learnit.data.user.register.model.RegistrationData
import com.example.learnit.data.user.register.model.RegistrationResponseData

import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @GET("users")
    suspend fun getUsers(): Response<List<LoggedUserData>>

    @POST("login")
    suspend fun authorizeLogin(@Body loginForm: LoginData): Response<ResponseData<Data>>

    @GET("profile/user")
    suspend fun getLoggedInUser(): Response<ResponseUserData>

    @POST("register")
    suspend fun registerUser(@Body registerForm: RegistrationData): Response<RegistrationResponseData>

    @GET("/courses")
    suspend fun getCourses(): Response<List<CourseData>>

    @GET("/lessons")
    suspend fun getLessons(): Response<List<LessonData>>

    @GET("/chapters")
    suspend fun getChapters(): Response<List<ChaptersData>>
}