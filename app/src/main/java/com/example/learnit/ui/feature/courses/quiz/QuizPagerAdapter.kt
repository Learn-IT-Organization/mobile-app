package com.example.learnit.ui.feature.courses.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.learnit.ui.feature.courses.quiz.fragment.MultipleChoiceQuizFragment
import com.example.learnit.ui.feature.courses.quiz.fragment.TrueFalseQuizFragment

class QuizPagerAdapter(
    fragmentActivity: FragmentActivity,
    private val numberOfQuestions: Int,
    //private var questionList: List<QuestionsAnswersModel>,
    private val courseId: Int,
    private val chapterId: Int,
    private val lessonId: Int,
) : FragmentStateAdapter(fragmentActivity) {

    private var currentFragment: Fragment? = null


    override fun getItemCount(): Int {
        return numberOfQuestions
        //return questionList.size
    }

//    override fun getItemId(position: Int): Long {
//        return questionList[position].questionId.toLong()
//    }

    override fun createFragment(position: Int): Fragment {
        val fragment = when (position % 2) {
            //itt adom at bundle-ban a kerdest
            //putseralizable
            0 -> MultipleChoiceQuizFragment().apply {
                arguments = Bundle().apply {
                    putInt("courseId", courseId)
                    putInt("chapterId", chapterId)
                    putInt("lessonId", lessonId)
                }
            }

            1 -> TrueFalseQuizFragment().apply {
                arguments = Bundle().apply {
                    putInt("courseId", courseId)
                    putInt("chapterId", chapterId)
                    putInt("lessonId", lessonId)
                }
            }

            else -> throw IllegalStateException("Invalid position: $position")
        }
        currentFragment = fragment
        return fragment
    }

    interface QuizButtonClickListener {
        fun onNextButtonClicked()
    }
}