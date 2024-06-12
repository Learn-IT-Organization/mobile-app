package com.learnitevekri.data

import ChapterResultData
import com.learnitevekri.data.courses.chapters.model.AddNewChapterData
import com.learnitevekri.data.courses.chapters.model.AddNewChapterResponseData
import com.learnitevekri.data.courses.chapters.model.ChapterData
import com.learnitevekri.data.courses.chapters.model.ChapterWithLessonsData
import com.learnitevekri.data.courses.chapters.model.EditChapterData
import com.learnitevekri.data.courses.course.model.AddNewCourseData
import com.learnitevekri.data.courses.course.model.AddNewCourseResponseData
import com.learnitevekri.data.courses.course.model.CourseData
import com.learnitevekri.data.courses.course.model.EditCourseData
import com.learnitevekri.data.courses.lessons.model.AddLessonContentResponseData
import com.learnitevekri.data.courses.lessons.model.AddNewLessonData
import com.learnitevekri.data.courses.lessons.model.AddNewLessonResponseData
import com.learnitevekri.data.courses.lessons.model.DeleteContentResponse
import com.learnitevekri.data.courses.lessons.model.DeleteResponseData
import com.learnitevekri.data.courses.lessons.model.EditLessonContentData
import com.learnitevekri.data.courses.lessons.model.EditLessonData
import com.learnitevekri.data.courses.lessons.model.LessonContentData
import com.learnitevekri.data.courses.lessons.model.LessonData
import com.learnitevekri.data.courses.lessons.model.LessonProgressData
import com.learnitevekri.data.courses.lessons.model.UserAnswersData
import com.learnitevekri.data.courses.notifications.TokenData
import com.learnitevekri.data.courses.quiz.model.AddMatchingQuestionData
import com.learnitevekri.data.courses.quiz.model.AddMultipleChoiceQuestionData
import com.learnitevekri.data.courses.quiz.model.AddQuestionAnswerResponseData
import com.learnitevekri.data.courses.quiz.model.AddSortingQuestionData
import com.learnitevekri.data.courses.quiz.model.AddTrueFalseQuestionData
import com.learnitevekri.data.courses.quiz.model.BaseQuestionData
import com.learnitevekri.data.courses.quiz.model.EditQuestionAnswerData
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
import com.learnitevekri.data.user.teacher.model.TeacherRequestDataFull
import com.learnitevekri.data.user.teacher.model.TeacherRequestInfo
import com.learnitevekri.data.user.teacher.model.TeacherRequestResponseData
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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
        @Path("lessonId") lessonId: Int,
    ): Response<List<BaseQuestionData>>

    @GET("/myCourses")
    suspend fun getMyCourses(): Response<List<CourseData>>

    @GET("/lesson/{lessonId}/contents")
    suspend fun getLessonContentByLessonId(
        @Path("lessonId") lessonId: Int,
    ): Response<List<LessonContentData>>

    @GET("/lessonResult")
    suspend fun getLessonProgress(): Response<List<LessonProgressData>>

    @GET("/deleteResponses/{lessonId}")
    suspend fun deleteResponses(@Path("lessonId") lessonId: Int): Response<DeleteResponseData>

    @GET("/lesson/{lessonId}")
    suspend fun getLessonById(@Path("lessonId") lessonId: Int): Response<LessonData>

    @GET("/course/{courseId}/chapter/{chapterId}/score")
    suspend fun getChapterResult(
        @Path("courseId") courseId: Int, @Path("chapterId") chapterId: Int,
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

    @POST("/createRequest")
    suspend fun sendTeacherRequest(@Body teacherRequestData: TeacherRequestData): Response<TeacherRequestResponseData>

    @GET("/teacherRequests")
    suspend fun getTeacherRequests(): Response<List<TeacherRequestDataFull>>

    @POST("/validateTeacher")
    suspend fun acceptTeacherRequest(@Body teacherRequestInfo: TeacherRequestInfo): Response<Message>

    @POST("/declineRequest")
    suspend fun declineTeacherRequest(@Body teacherRequestInfo: TeacherRequestInfo): Response<Message>

    @GET("/userRequests")
    suspend fun getUserRequests(): Response<TeacherRequestDataFull>

    @POST("/courses")
    suspend fun addNewCourse(@Body addNewCourseData: AddNewCourseData): Response<AddNewCourseResponseData>

    @POST("/chapters")
    suspend fun addNewChapter(@Body lessonContents: AddNewChapterData): Response<AddNewChapterResponseData>

    @POST("/lessons")
    suspend fun addNewLesson(@Body addNewLessonData: AddNewLessonData): Response<AddNewLessonResponseData>

    @PUT("/editCourse/{courseId}")
    suspend fun editCourse(
        @Path("courseId") courseId: Int, @Body editCourseData: EditCourseData,
    ): Response<AddNewCourseResponseData>

    @PUT("/editChapter/{chapterId}")
    suspend fun editChapter(
        @Path("chapterId") chapterId: Int, @Body editChapterData: EditChapterData,
    ): Response<AddNewChapterResponseData>

    @PUT("/editLesson/{lessonId}")
    suspend fun editLesson(
        @Path("lessonId") lessonId: Int, @Body editLessonData: EditLessonData,
    ): Response<AddNewLessonResponseData>

    @POST("/lessonContents")
    suspend fun createLessonContent(@Body lessonContent: LessonContentData): Response<AddLessonContentResponseData>

    @POST("/questionsAnswers")
    suspend fun createQuestionAnswerMatching(@Body questionData: AddMatchingQuestionData): Response<AddQuestionAnswerResponseData>

    @POST("/questionsAnswers")
    suspend fun createTrueFalseQuestionAnswer(@Body questionData: AddTrueFalseQuestionData): Response<AddQuestionAnswerResponseData>

    @POST("/questionsAnswers")
    suspend fun createMultipleChoiceQuestionAnswer(@Body questionData: AddMultipleChoiceQuestionData): Response<AddQuestionAnswerResponseData>

    @POST("/questionsAnswers")
    suspend fun createSortingQuestionAnswer(@Body questionData: AddSortingQuestionData): Response<AddQuestionAnswerResponseData>

    @PUT("/editLessonContent/{id}")
    suspend fun editLessonContent(
        @Path("id") id: Int, @Body editLessonContentData: EditLessonContentData,
    ): Response<AddLessonContentResponseData>

    @PUT("/editQuestionsAnswers/{id}")
    suspend fun <T> editQuestionAnswer(
        @Path("id") id: Int, @Body editQuestionAnswerData: EditQuestionAnswerData<T>,
    ): Response<AddQuestionAnswerResponseData>

    @GET("/course/{courseId}")
    suspend fun getCourseById(@Path("courseId") courseId: Int): Response<CourseData>

    @GET("/chapter/{chapterId}")
    suspend fun getChapterById(@Path("chapterId") chapterId: Int): Response<ChapterData>

    @DELETE("/course/{courseId}")
    suspend fun deleteCourse(@Path("courseId") courseId: Int): Response<DeleteResponseData>

    @DELETE("/chapter/{chapterId}")
    suspend fun deleteChapter(@Path("chapterId") chapterId: Int): Response<DeleteResponseData>

    @DELETE("/lesson/{lessonId}")
    suspend fun deleteLesson(@Path("lessonId") lessonId: Int): Response<DeleteResponseData>

    @DELETE("/lessonContent/{contentId}")
    suspend fun deleteLessonContent(@Path("contentId") contentId: Int): Response<DeleteContentResponse>


}