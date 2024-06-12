package com.learnitevekri.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.courses.quiz.model.AddSortingAnswer
import com.learnitevekri.data.courses.quiz.model.AddSortingQuestionData
import com.learnitevekri.databinding.FragmentAddQuestionSortingBinding
import com.learnitevekri.ui.feature.courses.quiz.adapter.SortingItemAdapter
import com.learnitevekri.ui.feature.courses.quiz.viewModel.SharedQuizViewModel

class AddQuestionSorting : Fragment() {
    private lateinit var binding: FragmentAddQuestionSortingBinding
    private val viewModel: SharedQuizViewModel by activityViewModels()

    private var lessonId: Int = -1
    private var chapterId: Int = -1
    private var courseId: Int = -1
    private var userId = 1

    private lateinit var group1ItemsAdapter: SortingItemAdapter
    private lateinit var group2ItemsAdapter: SortingItemAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddQuestionSortingBinding.inflate(inflater, container, false)
        lessonId = arguments?.getInt("lesson_id", -1) ?: -1
        chapterId = arguments?.getInt("chapter_id", -1) ?: -1
        courseId = arguments?.getInt("course_id", -1) ?: -1
        userId = SharedPreferences.getUserId().toInt()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        group1ItemsAdapter = SortingItemAdapter(mutableListOf())
        group2ItemsAdapter = SortingItemAdapter(mutableListOf())

        binding.rvGroup1Items.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGroup1Items.adapter = group1ItemsAdapter

        binding.rvGroup2Items.layoutManager = LinearLayoutManager(requireContext())
        binding.rvGroup2Items.adapter = group2ItemsAdapter

        binding.btnAddItemGroup1.setOnClickListener {
            val item = binding.etGroup1Item.text.toString()
            if (item.isNotEmpty()) {
                group1ItemsAdapter.addItem(item)
                binding.etGroup1Item.text.clear()
            }
        }

        binding.btnAddItemGroup2.setOnClickListener {
            val item = binding.etGroup2Item.text.toString()
            if (item.isNotEmpty()) {
                group2ItemsAdapter.addItem(item)
                binding.etGroup2Item.text.clear()
            }
        }

        binding.saveButton.setOnClickListener {
            saveSortingQuestion()
        }
    }

    private fun saveSortingQuestion() {
        val questionText = ""
        val group1Title = binding.etGroup1Title.text.toString().trim()
        val group2Title = binding.etGroup2Title.text.toString().trim()

        val group1Items = group1ItemsAdapter.getItems()
        val group2Items = group2ItemsAdapter.getItems()

        if (group1Items.isEmpty() || group2Items.isEmpty()) {
            binding.etGroup1Title.error = "Add at least one item"
            binding.etGroup2Title.error = "Add at least one item"
            return
        }

        val sortingAnswers = mutableListOf<AddSortingAnswer>()
        val upList = mutableListOf<String>()
        val downList = mutableListOf<String>()

        for (item in group1Items) {
            upList.add(item)
        }

        for (i in group2Items.indices) {
            downList.add(group2Items[i])
        }

        sortingAnswers.add(AddSortingAnswer(group1Title, group2Title, upList, downList))

        val sortingQuestionData = AddSortingQuestionData(
            qaLessonId = lessonId,
            questionId = 0,
            questionText = questionText,
            questionType = "sorting",
            answers = sortingAnswers,
            qaChapterId = chapterId,
            qaCourseId = courseId,
            userId
        )

        viewModel.addQuestionAnswerSorting(sortingQuestionData)
    }

}