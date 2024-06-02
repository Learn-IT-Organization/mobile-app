package com.learnitevekri.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Spinner
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.learnitevekri.databinding.FragmentAddQuestionBinding
import com.learnitevekri.ui.feature.courses.quiz.adapter.AddQuestionPagerAdapter
import com.learnitevekri.ui.feature.courses.quiz.viewModel.SharedQuizViewModel

class AddQuestionFragment : Fragment() {

    private lateinit var binding: FragmentAddQuestionBinding
    private val viewModel: SharedQuizViewModel by activityViewModels()
    private lateinit var viewPager: ViewPager2
    private lateinit var questionTypeSpinner: Spinner
    private lateinit var adapter: AddQuestionPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {

        binding = FragmentAddQuestionBinding.inflate(inflater, container, false)

        viewPager = binding.viewPager
        questionTypeSpinner = binding.questionTypeSpinner

        questionTypeSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                updateAdapter(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

            }
        }

        return binding.root
    }

    private fun updateAdapter(selectedPosition: Int) {
        adapter = AddQuestionPagerAdapter(requireActivity(), selectedPosition)
        viewPager.adapter = adapter
    }
}