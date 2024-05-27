package com.learnitevekri.data

import ChapterResultData
import com.learnitevekri.data.courses.chapters.model.ChapterData
import com.learnitevekri.data.courses.chapters.model.ChapterWithLessonsData
import com.learnitevekri.data.courses.course.model.CourseData
import com.learnitevekri.data.courses.lessons.model.DeleteResponseData
import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.data.courses.lessons.model.LessonData
import com.learnitevekri.data.courses.lessons.model.LessonProgressData
import com.learnitevekri.data.courses.lessons.model.UserAnswersData
import com.learnitevekri.data.courses.notifications.TokenData
import com.learnitevekri.data.courses.quiz.model.BaseQuestionData
import com.learnitevekri.data.courses.quiz.model.QuizResponseData
import com.learnitevekri.data.courses.quiz.model.QuizResultData
import com.learnitevekri.data.user.login.model.Data
import com.learnitevekri.data.user.login.model.EditProfileData
import com.learnitevekri.data.user.login.model.EditProfileResponse
import com.learnitevekri.data.user.login.model.ForgotPasswordData
import com.learnitevekri.data.user.login.model.LoggedUserData
import com.learnitevekri.data.user.login.model.LoginData
import com.learnitevekri.data.user.login.model.ResetCodeData
import com.learnitevekri.data.user.login.model.ResetPasswordResponseData
import com.learnitevekri.data.user.login.model.ResponseData
import com.learnitevekri.data.user.logout.LogoutResponseData
import com.learnitevekri.data.user.register.model.RegistrationData
import com.learnitevekri.data.user.register.model.RegistrationResponseData
import com.learnitevekri.data.user.teacher.model.Message
import com.learnitevekri.data.user.teacher.model.TeacherRequestData
import com.learnitevekri.data.user.teacher.model.TeacherRequestInfo
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("/users")
    suspend fun getUsers(): Response<List<LoggedUserData>>

    @POST("/login")
    suspend fun authorizeLogin(@Body loginForm: LoginData): Response<ResponseData<Data>>

    @POST("/logout")
    suspend fun logOut(): Response<LogoutResponseData>

    @GET("/profile/user")
    suspend fun getLoggedInUser(): Response<ResponseData<LoggedUserData>>

    @POST("/register")
    suspend fun registerUser(@Body registerForm: RegistrationData): Response<RegistrationResponseData>

    @GET("/courses")
    suspend fun getCourses(): Response<List<CourseData>>

    @GET("/chapters")
    suspend fun getChapters(): Response<List<ChapterData>>

    @GET("/course/{id}/chapters")
    suspend fun getChaptersByCourseId(@Path("id") courseId: Int): Response<List<ChapterWithLessonsData>>

    @GET("/chapters/{id}/lessons")
    suspend fun getLessonsByChapterId(@Path("id") id: Int): Response<List<LessonData>>

    @POST("/respond")
    suspend fun sendResponse(@Body quizResponseData: QuizResponseData): Response<QuizResultData>

    @GET("/course/{courseId}/chapters/{chapterId}/lesson/{lessonId}/questionsAnswers")
    suspend fun getQuestionsAnswersByCourseIdChapterIdLessonId(
        @Path("courseId") courseId: Int,
        @Path("chapterId") chapterId: Int,
        @Path("lessonId") lessonId: Int
    ): Response<List<BaseQuestionData>>

    @GET("/myCourses")
    suspend fun getMyCourses(): Response<List<CourseData>>

    @GET("/lesson/{lessonId}/contents")
    suspend fun getLessonContentByLessonId(
        @Path("lessonId") lessonId: Int
    ): Response<List<LessonContentData>>

    @GET("/lessonResult")
    suspend fun getLessonProgress(): Response<List<LessonProgressData>>

    @GET("/deleteResponses/{lessonId}")
    suspend fun deleteResponses(@Path("lessonId") lessonId: Int): Response<DeleteResponseData>

    @GET("/lesson/{lessonId}")
    suspend fun getLessonById(@Path("lessonId") lessonId: Int): Response<LessonData>

    @GET("/course/{courseId}/chapter/{chapterId}/score")
    suspend fun getChapterResult(
        @Path("courseId") courseId: Int,
        @Path("chapterId") chapterId: Int
    ): Response<ChapterResultData>

    @GET("/userResultsWithValidation/{lessonId}/lesson")
    suspend fun getLessonResultWithValidation(@Path("lessonId") lessonId: Int): Response<List<UserAnswersData>>

    @POST("/FCM_token")
    suspend fun sendFCMToken(@Body token: TokenData): Response<Void>

    @POST("requestResetCode")
    suspend fun requestResetCode(@Body resetRequest: ForgotPasswordData): Response<ResetPasswordResponseData>

    @POST("/changePassword")
    suspend fun changePasswordWithResetCode(@Body resetCodeData: ResetCodeData): Response<ResetPasswordResponseData>

    @PUT("/editUser")
    suspend fun editUser(@Body userData: EditProfileData): Response<EditProfileResponse>

    @GET("/teacherRequests")
    suspend fun getTeacherRequests(): Response<List<TeacherRequestData>>

    @POST("/validateTeacher")
    suspend fun acceptTeacherRequest(@Body teacherRequestInfo: TeacherRequestInfo): Response<Message>

    @POST("/declineRequest")
    suspend fun declineTeacherRequest(@Body teacherRequestInfo: TeacherRequestInfo): Response<Message>
}