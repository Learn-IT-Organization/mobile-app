package com.example.learnit.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.learnit.data.courses.quiz.model.BaseQuestionData

abstract class BaseQuizFragment<T : BaseQuestionData> : Fragment() {
    abstract val viewModel: ViewModel
    abstract val binding: ViewBinding
    abstract val TAG: String

    protected var currentQuestion: T? = null

    protected var courseId: Int = -1
    protected var chapterId: Int = -1
    protected var lessonId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        arguments?.let {
            courseId = it.getInt("courseId", -1)
            chapterId = it.getInt("chapterId", -1)
            lessonId = it.getInt("lessonId", -1)
            currentQuestion = it.getSerializable("question") as T?
            Log.d(TAG, "question: ${currentQuestion?.questionText}")
            updateUI()
        }
        return binding.root
    }

    protected abstract fun updateUI()

}