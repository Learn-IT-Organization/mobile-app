package com.example.learnit.ui.feature.courses.quiz

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.learnit.data.ApiConstants.ARG_CHAPTER_ID
import com.example.learnit.data.ApiConstants.ARG_COURSE_ID
import com.example.learnit.data.ApiConstants.ARG_LESSON_ID
import com.example.learnit.data.courses.quiz.model.BaseQuestionData
import com.example.learnit.data.courses.quiz.model.MultipleChoiceQuestionData
import com.example.learnit.data.courses.quiz.model.SortingQuestionData
import com.example.learnit.data.courses.quiz.model.TrueFalseQuestionData
import com.example.learnit.ui.feature.courses.quiz.fragment.MultipleChoiceQuizFragment
import com.example.learnit.ui.feature.courses.quiz.fragment.SortingQuizFragment
import com.example.learnit.ui.feature.courses.quiz.fragment.TrueFalseQuizFragment

class QuizPagerAdapter(
    fragmentActivity: FragmentActivity,
    private var questionList: List<BaseQuestionData>,
    private val courseId: Int,
    private val chapterId: Int,
    private val lessonId: Int
) : FragmentStateAdapter(fragmentActivity) {

    private val usedQuestions: HashSet<Int> = HashSet()

    companion object {
        val TAG: String = QuizPagerAdapter::class.java.simpleName
    }

    init {
        shuffleQuestions()
    }

    private fun shuffleQuestions() {
        questionList = shuffleQuestionsByType(questionList)
    }

    private fun shuffleQuestionsByType(questions: List<BaseQuestionData>): List<BaseQuestionData> {
        return questions.shuffled()
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun getItemId(position: Int): Long {
        return questionList[position].questionId.toLong()
    }

    override fun createFragment(position: Int): Fragment {
        val shuffledQuestion = questionList[position]
        usedQuestions.add(shuffledQuestion.questionId)

        val fragment: Fragment = when (shuffledQuestion) {
            is SortingQuestionData -> {
                val sortingFragment = SortingQuizFragment().apply {
                    arguments = createBundle(shuffledQuestion)
                }
                sortingFragment
            }

            is MultipleChoiceQuestionData -> {
                val mcFragment = MultipleChoiceQuizFragment().apply {
                    arguments = createBundle(shuffledQuestion)
                }
                mcFragment
            }

            is TrueFalseQuestionData -> {
                val tfFragment = TrueFalseQuizFragment().apply {
                    arguments = createBundle(shuffledQuestion)
                }
                tfFragment
            }

            else -> throw IllegalStateException("Invalid question type: ${shuffledQuestion::class.java.simpleName}")
        }

        return fragment
    }

    private fun createBundle(question: BaseQuestionData): Bundle {
        return Bundle().apply {
            putInt(ARG_COURSE_ID, courseId)
            putInt(ARG_CHAPTER_ID, chapterId)
            putInt(ARG_LESSON_ID, lessonId)
            putSerializable("question", question)
        }
    }
}