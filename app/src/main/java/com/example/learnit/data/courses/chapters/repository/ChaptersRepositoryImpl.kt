package com.example.learnit.data.courses.chapters.repository

import android.util.Log
import com.example.learnit.data.RetrofitAdapter
import com.example.learnit.data.courses.chapters.mapper.mapToChapterList
import com.example.learnit.domain.course.repository.ChaptersRepository
import com.example.learnit.ui.feature.courses.chapters.model.ChapterModel

//Csinaljatok egy TAG constanst ami tartalmazza az osztaly nevet ami szerint kiloggoltok dolgokat
//A Loggolas TAG-ja minden osztalyban egyszeges kell legyen hogy konnyebb legyen a kereses utana
//Szoval ilyet hogy harom Log-bann 3 fele Tag van nem akarok latni :)

//!!Kod formazas itt is es minden osztalyban!! CTR+ALT+L es importok formazasa/torlese stb: CTrl+ALT+O
object ChaptersRepositoryImpl : ChaptersRepository {
    private val apiService = RetrofitAdapter.provideApiService()
    override suspend fun getChapters(): List<ChapterModel> {
        val response = apiService.getChapters()
        Log.d("ChapterResponse1", response.raw().toString())

        if (response.isSuccessful) {
            val responseData = response.body()
            Log.d("ChapterResponse2", response.raw().toString())
            return (responseData ?: emptyList()).mapToChapterList()
        }
        Log.d("ChapterResponse3", "EmptyList")
        return emptyList()
    }

    override suspend fun getChaptersByCourseId(courseId: Int): List<ChapterModel> {
        val response = apiService.getChaptersByCourseId(courseId)
        Log.d("ChapterResponse1", response.raw().toString())

        if (response.isSuccessful) {
            val responseData = response.body()
            Log.d("ChapterResponse2", response.raw().toString())
            return (responseData ?: emptyList()).mapToChapterList()
        }
        Log.d("ChapterResponse3", "EmptyList")
        return emptyList()
    }
}