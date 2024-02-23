package com.example.learnit.ui.feature.courses.courses.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.learnit.R
import com.example.learnit.data.ApiConstants
import com.example.learnit.data.courses.course.model.CourseData
import com.example.learnit.databinding.CourseListItemBinding

class UserAnswerAdapter(private val courses: List<CourseData>) :
    RecyclerView.Adapter<CoursesAdapter.CourseViewHolder>() {

    inner class UserAnswerViewHolder(private val binding: UserA) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(course: CourseData) {
            binding.nameTextView.text = course.course_name
            if (adapterPosition % 2 == 0) {
                binding.courseImageView.setImageResource(R.drawable.girl_with_code_snippet)
            } else {
                binding.courseImageView.setImageResource(R.drawable.boy_developer)
            }
            binding.root.setOnClickListener {
                itemView.findNavController().navigate(
                    R.id.action_CoursesFragment_to_ChaptersFragemnt,
                    bundleOf(ApiConstants.COURSE_ID to course.course_id)
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserAnswerViewHolder {
        val binding =
            CourseListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UserAnswerViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CoursesAdapter.CourseViewHolder, position: Int) {
        TODO("Not yet implemented")
    }



    override fun getItemCount(): Int {
        return courses.size
    }
}