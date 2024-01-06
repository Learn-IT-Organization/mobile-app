package com.example.learnit.data

import com.example.learnit.data.courses.chapters.model.ChaptersData
import com.example.learnit.data.courses.course.model.CourseData
import com.example.learnit.data.courses.lessons.model.LessonData
import com.example.learnit.data.courses.lessons.model.MultipleChoiceQuestionAnswerData
import com.example.learnit.data.courses.lessons.model.MultipleChoiceResponseData
import com.example.learnit.data.courses.quiz.model.QuestionsAnswersData
import com.example.learnit.data.courses.quiz.model.QuizResultData
import com.example.learnit.data.courses.quiz.model.UserResponseData
import com.example.learnit.data.user.login.model.Data
import com.example.learnit.data.user.login.model.LoggedUserData
import com.example.learnit.data.user.login.model.LoginData
import com.example.learnit.data.user.login.model.ResponseData
import com.example.learnit.data.user.register.model.RegistrationData
import com.example.learnit.data.user.register.model.RegistrationResponseData
import com.example.learnit.ui.feature.courses.quiz.model.QuizResultModel

import retrofit2.http.GET
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("users")
    suspend fun getUsers(): Response<List<LoggedUserData>>

    @POST("login")
    suspend fun authorizeLogin(@Body loginForm: LoginData): Response<ResponseData<Data>>

    @GET("profile/user")
    suspend fun getLoggedInUser(): Response<ResponseData<LoggedUserData>>

    @POST("register")
    suspend fun registerUser(@Body registerForm: RegistrationData): Response<RegistrationResponseData>

    @GET("/courses")
    suspend fun getCourses(): Response<List<CourseData>>

    @GET("/lessons")
    suspend fun getLessons(): Response<List<LessonData>>

    @GET("/chapters")
    suspend fun getChapters(): Response<List<ChaptersData>>

    @GET("/course/{id}/chapters")
    suspend fun getChaptersByCourseId(@Path("id") courseId: Int): Response<List<ChaptersData>>

    @GET("/chapters/{id}/lessons")
    suspend fun getLessonsByChapterId(@Path("id") id: Int): Response<List<LessonData>>

    @GET("/questionsAnswers")
    suspend fun getQuestionsAnswers(): Response<List<QuestionsAnswersData>>

    @POST("responses")
    suspend fun sendResult(@Body quizResultData: QuizResultData): Response<UserResponseData>
    @GET("/course/{courseId}/chapters/{chapterId}/lesson/{lessonId}/questionsAnswers/multiple_choice")
    suspend fun getQuestionsAnswersByCourseIdChapterIdLessonIdMultipleChoice(
        @Path("courseId") courseId: Int,
        @Path("chapterId") chapterId: Int,
        @Path("lessonId") lessonId: Int
    ): Response<List<MultipleChoiceQuestionAnswerData>>

    @POST("/respond")
    suspend fun postMultipleChoiceResponse(@Body multipleChoiceResponseData: MultipleChoiceResponseData): Response<MultipleChoiceResponseData>

}