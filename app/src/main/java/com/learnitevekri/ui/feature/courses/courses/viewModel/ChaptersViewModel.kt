package com.learnitevekri.ui.feature.courses.courses.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.learnitevekri.data.courses.chapters.model.AddNewChapterData
import com.learnitevekri.data.courses.chapters.model.AddNewChapterResponseData
import com.learnitevekri.data.courses.chapters.model.ChapterData
import com.learnitevekri.data.courses.chapters.model.ChapterWithLessonsData
import com.learnitevekri.data.courses.chapters.model.EditChapterData
import com.learnitevekri.data.courses.course.model.AddNewCourseResponseData
import com.learnitevekri.data.courses.course.model.CourseData
import com.learnitevekri.domain.course.ChaptersRepository
import com.learnitevekri.ui.App
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChaptersViewModel : ViewModel() {
    private val repository: ChaptersRepository = App.instance.getChaptersRepository()

    private val mutableState = MutableStateFlow<ChaptersScreenState>(ChaptersScreenState.Loading)
    val state: StateFlow<ChaptersScreenState> = mutableState

    private val mutableState2 = MutableStateFlow<EditChapterState>(EditChapterState.Loading)
    val state2: StateFlow<EditChapterState> = mutableState2

    companion object {
        val TAG: String = ChaptersViewModel::class.java.simpleName
    }

    sealed class ChaptersScreenState {
        data object Loading : ChaptersScreenState()
        data class Success(val chaptersData: List<ChapterWithLessonsData>) : ChaptersScreenState()
        data class Failure(val throwable: Throwable) : ChaptersScreenState()
    }

    sealed class EditChapterState {
        object Loading : EditChapterState()
        data class Success(val chapterData: ChapterData) : EditChapterState()
        data class Updated(val response: AddNewChapterResponseData) : EditChapterState()
        data class Failure(val throwable: Throwable) : EditChapterState()
    }

    private val errorHandler = CoroutineExceptionHandler { _, exception ->
        mutableState.value = ChaptersScreenState.Failure(exception)
    }

    fun loadChapters(courseId: Int) {
        viewModelScope.launch(Dispatchers.IO + errorHandler) {
            try {
                val loadedChapters = repository.getChaptersByCourseId(courseId)
                loadedChapters.forEach {
                    it.chapterResultData =
                        repository.getChapterResult(courseId, it.chapter.chapterId)
                }
                Log.d(TAG, "Chapters: $loadedChapters")
                mutableState.value = ChaptersScreenState.Success(loadedChapters)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching chapters: ${e.message}")
                mutableState.value = ChaptersScreenState.Failure(e)
            }
        }
    }

    fun loadChapterById(chapterId: Int) {
        viewModelScope.launch {
            try {
                val chapter = repository.getChapterById(chapterId)
                mutableState2.value = EditChapterState.Success(chapter)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching chapter by id: ${e.message}")
                mutableState2.value = EditChapterState.Failure(e)
            }
        }
    }

    suspend fun addNewChapter(addNewChapterData: AddNewChapterData): Int? {
        return withContext(Dispatchers.IO) {
            try {
                repository.addNewChapter(addNewChapterData)
            } catch (e: Exception) {
                Log.e(CoursesViewModel.TAG, "Error adding new chapter: ${e.message}")
                null
            }
        }
    }

    suspend fun editChapter(
        chapterId: Int, editChapterData: EditChapterData
    ): AddNewChapterResponseData {
        return withContext(Dispatchers.IO) {
            try {
                val chaptersScreenState = mutableState.value
                if (chaptersScreenState is ChaptersScreenState.Success) {
                    val chapterExists = chaptersScreenState.chaptersData.any { it.chapter.chapterId == chapterId }
                    if (!chapterExists) {
                        throw IllegalArgumentException("Chapter not found")
                    }
                }
                repository.editChapter(chapterId, editChapterData).also {
                    Log.d(TAG, "Chapter updated successfully: $chapterId")
                }
            } catch (e: Exception) {
                Log.e(TAG, "Error updating chapter: ${e.message}")
                throw e
            }
        }
    }

}