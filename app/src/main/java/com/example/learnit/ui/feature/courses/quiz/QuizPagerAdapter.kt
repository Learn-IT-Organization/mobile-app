package com.example.learnit.ui.feature.courses.quiz

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.learnit.data.courses.quiz.model.BaseQuestionData
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
        Log.d(TAG, "shuffledQuestionList: $questionList")
    }

    private fun shuffleQuestionsByType(questions: List<BaseQuestionData>): List<BaseQuestionData> {
        val shuffledList = questions.shuffled()
        val multipleChoiceList = shuffledList.filter { it.questionType == "multiple_choice" }
        val trueFalseList = shuffledList.filter { it.questionType == "true_false" }
        Log.d(TAG, "multipleChoiceList: $multipleChoiceList")
        return multipleChoiceList + trueFalseList
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun getItemId(position: Int): Long {
        return questionList[position].questionId.toLong()
    }

    override fun createFragment(position: Int): Fragment {
        val shuffledQuestion = questionList[position]
        Log.d(TAG, "shuffledQuestion: $shuffledQuestion")
        usedQuestions.add(shuffledQuestion.questionId)

        val fragment: Fragment = when (shuffledQuestion.questionType) {
            "multiple_choice" -> {
                val mcFragment = MultipleChoiceQuizFragment().apply {
                    arguments = createBundle(shuffledQuestion)
                }
                mcFragment
            }

            "true_false" -> {
                val tfFragment = TrueFalseQuizFragment().apply {
                    arguments = createBundle(shuffledQuestion)
                }
                tfFragment
            }

            "sorting" -> {
                val sortingFragment = SortingQuizFragment().apply {
                    arguments = createBundle(shuffledQuestion)
                }
                sortingFragment
            }

            else -> throw IllegalStateException("Invalid question type: ${shuffledQuestion.questionType}")
        }

        return fragment
    }

    private fun createBundle(question: BaseQuestionData): Bundle {
        return Bundle().apply {
            putInt("courseId", courseId)
            putInt("chapterId", chapterId)
            putInt("lessonId", lessonId)
            putSerializable("question", question)
        }
    }

}