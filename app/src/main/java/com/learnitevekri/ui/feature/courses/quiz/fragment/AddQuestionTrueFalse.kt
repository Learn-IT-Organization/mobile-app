package com.learnitevekri.ui.feature.courses.quiz.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.snackbar.Snackbar
import com.learnitevekri.R
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.data.courses.quiz.model.AddMultipleChoiceAnswer
import com.learnitevekri.data.courses.quiz.model.AddTrueFalseQuestionData
import com.learnitevekri.databinding.FragmentAddQuestionTrueFalseBinding
import com.learnitevekri.ui.feature.courses.quiz.viewModel.SharedQuizViewModel

class AddQuestionTrueFalse : Fragment() {
    private lateinit var binding: FragmentAddQuestionTrueFalseBinding
    private val viewModel: SharedQuizViewModel by activityViewModels()

    private val TAG = AddQuestionTrueFalse::class.java.simpleName

    private var lessonId: Int = -1
    private var chapterId: Int = -1
    private var courseId: Int = -1
    private var userId = 1

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAddQuestionTrueFalseBinding.inflate(inflater, container, false)
        lessonId = arguments?.getInt("lesson_id", -1) ?: -1
        chapterId = arguments?.getInt("chapter_id", -1) ?: -1
        courseId = arguments?.getInt("course_id", -1) ?: -1
        Log.d(
            TAG, "Course ID, Chapter ID, Lesson ID: $courseId, $chapterId, $lessonId"
        )
        userId = SharedPreferences.getUserId().toInt()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.saveButton.setOnClickListener {
            saveTrueFalseQuestion()
        }
    }

    private fun saveTrueFalseQuestion() {
        val questionText = binding.questionEditText.text.toString().trim()

        if (questionText.isEmpty()) {
            Snackbar.make(
                binding.root,
                "Please fill in the question field",
                Snackbar.LENGTH_SHORT
            ).show()
            return
        }

        val userSelection = when (binding.trueFalseRadioGroup.checkedRadioButtonId) {
            R.id.trueRadioButton -> true
            R.id.falseRadioButton -> false
            else -> {
                Snackbar.make(
                    binding.root,
                    "Please select an answer",
                    Snackbar.LENGTH_SHORT
                ).show()
                return
            }
        }

        val trueFalseQuestionData = AddTrueFalseQuestionData(
            qaLessonId = lessonId,
            questionId = 0,
            questionText = questionText,
            questionType = "true_false",
            answers = listOf(
                AddMultipleChoiceAnswer(isCorrect = userSelection, optionText = "True"),
                AddMultipleChoiceAnswer(isCorrect = !userSelection, optionText = "False")
            ),
            qaChapterId = chapterId,
            qaCourseId = courseId,
            userId
        )

        viewModel.addQuestionAnswerTrueFalse(trueFalseQuestionData)
    }
}