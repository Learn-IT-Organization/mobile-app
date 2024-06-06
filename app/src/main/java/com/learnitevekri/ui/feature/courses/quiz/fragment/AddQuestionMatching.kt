package com.learnitevekri.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnitevekri.data.courses.quiz.model.AddMatchingAnswer
import com.learnitevekri.data.courses.quiz.model.AddMatchingQuestionData
import com.learnitevekri.databinding.FragmentAddQuestionMatchingBinding
import com.learnitevekri.ui.feature.courses.quiz.adapter.MatchingPairsAdapter
import com.learnitevekri.ui.feature.courses.quiz.viewModel.SharedQuizViewModel

class AddQuestionMatching : Fragment() {

    private val TAG: String = AddQuestionMatching::class.java.simpleName
    private lateinit var binding: FragmentAddQuestionMatchingBinding
    private lateinit var adapter: MatchingPairsAdapter
    private val pairsList = mutableListOf<Pair<String, String>>()
    private val viewModel: SharedQuizViewModel by activityViewModels()
    private val maxPairs = 4
    private var courseId: Int = -1
    private var chapterId: Int = -1
    private var lessonId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddQuestionMatchingBinding.inflate(inflater, container, false)

        lessonId = arguments?.getInt("lesson_id", -1) ?: -1
        chapterId = arguments?.getInt("chapter_id", -1) ?: -1
        courseId = arguments?.getInt("course_id", -1) ?: -1

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = MatchingPairsAdapter(pairsList)
        binding.rvMatchingPairs.layoutManager = LinearLayoutManager(requireContext())
        binding.rvMatchingPairs.adapter = adapter

        binding.btnAddPair.setOnClickListener {
            if (pairsList.size < maxPairs) {
                pairsList.add(Pair("", ""))
                adapter.notifyItemInserted(pairsList.size - 1)
            } else {
                Toast.makeText(context, "Maximum of $maxPairs pairs allowed", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        binding.saveButton.setOnClickListener {
            saveData()
        }
    }

    private fun saveData() {
        val answers = pairsList.map { AddMatchingAnswer(it.first, it.second) }
        val questionData = AddMatchingQuestionData(
            qaLessonId = lessonId,
            questionId = 0,
            questionText = "",
            questionType = "matching",
            answers = answers,
            chapterId,
            courseId
        )

        Log.d(TAG, "saveData: $questionData")
        viewModel.addQuestionAnswerMatching(questionData)
    }
}