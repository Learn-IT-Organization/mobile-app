package com.learnitevekri.ui

import android.app.Activity
import com.learnitevekri.data.courses.quiz.repository.QuizResultRepositoryImpl
import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.courses.chapters.repository.ChaptersRepositoryImpl
import com.learnitevekri.data.courses.course.repository.CourseRepositoryImpl
import com.learnitevekri.data.courses.lessons.repository.LessonRepositoryImpl
import com.learnitevekri.data.courses.quiz.repository.QuestionsAnswersRepositoryImpl
import com.learnitevekri.data.user.login.repository.LoginRepositoryImpl
import com.learnitevekri.data.user.login.repository.UserRepositoryImpl
import com.learnitevekri.data.user.logout.LogOutRepositoryImpl
import com.learnitevekri.data.user.register.repository.RegisterRepositoryImpl
import com.learnitevekri.domain.course.ChaptersRepository
import com.learnitevekri.domain.course.CourseRepository
import com.learnitevekri.domain.course.LessonRepository
import com.learnitevekri.domain.login.LoginRepository
import com.learnitevekri.domain.logout.LogOutRepository
import com.learnitevekri.domain.quiz.QuestionsAnswersRepository
import com.learnitevekri.domain.quiz.QuizResultRepository
import com.learnitevekri.domain.register.RegisterRepository
import com.learnitevekri.domain.user.UserRepository

class App : Application() {
    private var currentActivity: Activity? = null

    companion object {
        lateinit var instance: App
            private set

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        val isDarkModeEnabled = SharedPreferences.getDarkModeStatus(this)
        if (isDarkModeEnabled) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }
    fun setCurrentActivity(activity: Activity?) {
        currentActivity = activity
    }
    fun getCurrentActivity(): Activity? {
        return currentActivity
    }

    fun getLoginRepository(): LoginRepository = LoginRepositoryImpl
    fun getUserRepository(): UserRepository = UserRepositoryImpl
    fun getRegisterRepository(): RegisterRepository = RegisterRepositoryImpl
    fun getCourseRepository(): CourseRepository = CourseRepositoryImpl
    fun getChaptersRepository(): ChaptersRepository = ChaptersRepositoryImpl
    fun getLessonRepository(): LessonRepository = LessonRepositoryImpl
    fun getQuestionsAnswersRepository(): QuestionsAnswersRepository = QuestionsAnswersRepositoryImpl
    fun getQuizResultRepository(): QuizResultRepository = QuizResultRepositoryImpl
    fun getLogoutRepository(): LogOutRepository = LogOutRepositoryImpl
}