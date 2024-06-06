package com.learnitevekri.ui.feature.courses.quiz.adapter

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.learnitevekri.ui.feature.courses.quiz.fragment.AddQuestionMatching
import com.learnitevekri.ui.feature.courses.quiz.fragment.AddQuestionMultipleChoice
import com.learnitevekri.ui.feature.courses.quiz.fragment.AddQuestionSorting
import com.learnitevekri.ui.feature.courses.quiz.fragment.AddQuestionTrueFalse

class AddQuestionPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val selectedPosition: Int,
    private val lessonId: Int,
    private val chapterId: Int,
    private val courseId: Int,
) : FragmentStateAdapter(fragmentActivity) {

    private val TAG = AddQuestionPagerAdapter::class.java.simpleName

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        val fragment: Fragment = when (selectedPosition) {
            0 -> AddQuestionMultipleChoice()
            1 -> AddQuestionTrueFalse()
            2 -> AddQuestionSorting()
            3 -> AddQuestionMatching()
            else -> throw IllegalArgumentException("Unknown question type")
        }
        fragment.arguments = Bundle().apply {
            putInt("lesson_id", lessonId)
            putInt("chapter_id", chapterId)
            putInt("course_id", courseId)
            Log.d(TAG, "Lesson ID, Chapter ID, Course ID: $lessonId, $chapterId, $courseId")
        }
        return fragment
    }
}