package com.learnitevekri.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.learnitevekri.databinding.FragmentAddQuestionTrueFalseBinding

class AddQuestionTrueFalse : Fragment() {
    private lateinit var binding: FragmentAddQuestionTrueFalseBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddQuestionTrueFalseBinding.inflate(inflater, container, false)
        return binding.root
    }
}