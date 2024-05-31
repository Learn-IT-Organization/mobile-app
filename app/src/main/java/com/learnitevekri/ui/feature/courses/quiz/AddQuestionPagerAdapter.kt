package com.learnitevekri.ui.feature.courses.quiz

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.learnitevekri.ui.feature.courses.quiz.fragment.AddQuestionMatching
import com.learnitevekri.ui.feature.courses.quiz.fragment.AddQuestionMultipleChoice
import com.learnitevekri.ui.feature.courses.quiz.fragment.AddQuestionSorting
import com.learnitevekri.ui.feature.courses.quiz.fragment.AddQuestionTrueFalse

class AddQuestionPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val selectedPosition: Int
) : FragmentStateAdapter(fragmentActivity) {

    override fun getItemCount(): Int = 1

    override fun createFragment(position: Int): Fragment {
        return when (selectedPosition) {
            0 -> AddQuestionMultipleChoice()
            1 -> AddQuestionTrueFalse()
            2 -> AddQuestionSorting()
            3 -> AddQuestionMatching()
            else -> throw IllegalArgumentException("Unknown question type")
        }
    }
}