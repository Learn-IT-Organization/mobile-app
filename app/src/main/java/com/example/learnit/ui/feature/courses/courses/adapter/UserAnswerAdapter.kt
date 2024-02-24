package com.example.learnit.ui.feature.courses.courses.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.learnit.R
import com.example.learnit.data.courses.lessons.model.UserAnswersData
import com.example.learnit.databinding.AnswerListItemBinding

class UserAnswerAdapter(private val userAnswersList: List<UserAnswersData>) :
    RecyclerView.Adapter<UserAnswerAdapter.UserAnswerViewHolder>() {

    inner class UserAnswerViewHolder(private val binding: AnswerListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        @SuppressLint("SetTextI18n")
        fun bind(userAnswer: UserAnswersData) {
            binding.iconQuestionType.setImageResource(
                when (userAnswer.questionType) {
                    "multiple_choice" -> {
                        R.drawable.ic_multiple_choice
                    }

                    "true_false" -> {
                        R.drawable.ic_true_false
                    }

                    "sorting" -> {
                        R.drawable.ic_sorting
                    }

                    else -> {
                        android.R.drawable.ic_menu_help
                    }
                }
            )

            binding.questionText.text = userAnswer.questionText
            
            binding.userAnswer.text = "Your answer: ${userAnswer.userAnswer}"
            if (userAnswer.correctness.correct) {
                binding.correctness.text = "Correct!!"
                binding.correctness.setTextColor(binding.root.context.resources.getColor(R.color.green))
            } else {
                binding.correctness.text = userAnswer.correctness.responseText
                binding.correctness.setTextColor(binding.root.context.resources.getColor(R.color.red))
            }
            binding.score.text = "Score: ${userAnswer.score.toInt()}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAnswerViewHolder {
        val binding =
            AnswerListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserAnswerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserAnswerViewHolder, position: Int) {
        val userAnswer = userAnswersList[position]
        holder.bind(userAnswer)
    }

    override fun getItemCount(): Int {
        return userAnswersList.size
    }

}