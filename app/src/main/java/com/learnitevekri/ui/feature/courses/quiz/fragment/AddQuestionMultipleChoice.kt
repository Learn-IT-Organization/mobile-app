package com.learnitevekri.ui.feature.courses.quiz.fragment

import android.os.Build
import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.learnitevekri.R
import com.learnitevekri.databinding.FragmentAddQuestionMultipleChoiceBinding

class AddQuestionMultipleChoice : Fragment() {
    private lateinit var binding: FragmentAddQuestionMultipleChoiceBinding
    private val answerViews = mutableListOf<View>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddQuestionMultipleChoiceBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addAnswerField()
        addAnswerField()

        binding.addAnswerButton.setOnClickListener {
            if (answerViews.size < 6) {
                addAnswerField()
            } else {
                Toast.makeText(
                    requireContext(),
                    "You can add a maximum of 6 answers",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun addAnswerField() {
        val answerLayout = LinearLayout(requireContext()).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(0, 8, 0, 8)
            }
        }

        val answerEditText = EditText(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            )
            hint = getString(R.string.enter_the_answer)
            inputType = InputType.TYPE_CLASS_TEXT
        }

        val isCorrectCheckBox = CheckBox(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = getString(R.string.correct)
            
        }

        answerLayout.addView(answerEditText)
        answerLayout.addView(isCorrectCheckBox)
        binding.answersLinearLayout.addView(answerLayout)
        answerViews.add(answerLayout)
    }

}