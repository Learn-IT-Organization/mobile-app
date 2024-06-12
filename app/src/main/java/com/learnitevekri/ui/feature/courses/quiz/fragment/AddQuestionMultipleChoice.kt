package com.learnitevekri.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.courses.quiz.model.AddMultipleChoiceAnswer
import com.learnitevekri.data.courses.quiz.model.AddMultipleChoiceQuestionData
import com.learnitevekri.databinding.FragmentAddQuestionMultipleChoiceBinding
import com.learnitevekri.ui.feature.courses.quiz.viewModel.SharedQuizViewModel

class AddQuestionMultipleChoice : Fragment() {
    private var lessonId: Int = -1
    private var chapterId: Int = -1
    private var courseId: Int = -1
    private lateinit var binding: FragmentAddQuestionMultipleChoiceBinding
    private val viewModel: SharedQuizViewModel by activityViewModels()
    private var userId = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddQuestionMultipleChoiceBinding.inflate(inflater, container, false)
        lessonId = arguments?.getInt("lesson_id", -1) ?: -1
        chapterId = arguments?.getInt("chapter_id", -1) ?: -1
        courseId = arguments?.getInt("course_id", -1) ?: -1
        userId = SharedPreferences.getUserId().toInt()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            if (areFieldsValid()) {
                saveQuestion()
            } else {
                Snackbar.make(
                    binding.root,
                    "Please fill in all fields and select at least one correct answer",
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun areFieldsValid(): Boolean {
        val answerEditTexts = listOf(
            binding.answer1EditText,
            binding.answer2EditText,
            binding.answer3EditText,
            binding.answer4EditText
        )

        val isCorrectCheckBoxes = listOf(
            binding.isCorrectCheckBox1,
            binding.isCorrectCheckBox2,
            binding.isCorrectCheckBox3,
            binding.isCorrectCheckBox4
        )

        for (i in answerEditTexts.indices) {
            val answerText = answerEditTexts[i].text.toString().trim()
            val isCorrect = isCorrectCheckBoxes[i].isChecked

            if (answerText.isEmpty() && isCorrect) {
                return false
            }

            if (i == 0 && answerText.isEmpty()) {
                return false
            }
        }

        return true
    }

    private fun saveQuestion() {
        val answers = mutableListOf<AddMultipleChoiceAnswer>()

        val answerEditTexts = listOf(
            binding.answer1EditText,
            binding.answer2EditText,
            binding.answer3EditText,
            binding.answer4EditText
        )

        val isCorrectCheckBoxes = listOf(
            binding.isCorrectCheckBox1,
            binding.isCorrectCheckBox2,
            binding.isCorrectCheckBox3,
            binding.isCorrectCheckBox4
        )

        var isAnyChecked = false

        for (i in answerEditTexts.indices) {
            val answerText = answerEditTexts[i].text.toString().trim()
            val isCorrect = isCorrectCheckBoxes[i].isChecked

            if (answerText.isNotEmpty()) {
                answers.add(AddMultipleChoiceAnswer(isCorrect, answerText))
            }

            if (isCorrect) {
                isAnyChecked = true
            }
        }

        if (!isAnyChecked) {
            Snackbar.make(
                binding.root,
                "Please select at least one correct answer",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        val questionText = binding.questionEditText.text.toString().trim()

        val questionData = AddMultipleChoiceQuestionData(
            qaLessonId = lessonId,
            questionText = questionText,
            questionType = "multiple_choice",
            answers = answers,
            qaChapterId = chapterId,
            qaCourseId = courseId,
            userId
        )

        viewModel.addQuestionAnswerMultipleChoice(questionData)
    }


}