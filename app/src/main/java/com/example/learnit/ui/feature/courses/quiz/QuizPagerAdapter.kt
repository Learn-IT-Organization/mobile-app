package com.example.learnit.ui.feature.courses.quiz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.learnit.ui.feature.courses.quiz.fragment.MultipleChoiceQuizFragment
import com.example.learnit.ui.feature.courses.quiz.fragment.TrueFalseQuizFragment

class QuizPagerAdapter (
    fragmentActivity: FragmentActivity,
    private val numberOfQuestions: Int,
    private val courseId: Int,
    private val chapterId: Int,
    private val lessonId: Int
) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return numberOfQuestions
    }

    override fun createFragment(position: Int): Fragment {
        return when (position % 2) {
            0 -> MultipleChoiceQuizFragment().apply {
                Log.d("QuizPagerAdapter0", "$courseId $chapterId $lessonId")
                arguments = Bundle().apply {
                    putInt("courseId", courseId)
                    putInt("chapterId", chapterId)
                    putInt("lessonId", lessonId)
                }
            }
            1 -> TrueFalseQuizFragment().apply {
                Log.d("QuizPagerAdapter1", "$courseId $chapterId $lessonId")
                arguments = Bundle().apply {
                    putInt("courseId", courseId)
                    putInt("chapterId", chapterId)
                    putInt("lessonId", lessonId)
                }
            }
            else -> throw IllegalStateException("Invalid position: $position")
        }
    }
}