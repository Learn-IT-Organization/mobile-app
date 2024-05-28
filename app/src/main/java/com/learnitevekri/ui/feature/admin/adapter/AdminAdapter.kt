package com.learnitevekri.ui.feature.admin.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnitevekri.R
import com.learnitevekri.data.user.teacher.model.TeacherRequestDataFull
import com.learnitevekri.databinding.ItemTeacherRequestBinding

class AdminAdapter(
    private var teacherRequests: List<TeacherRequestDataFull>,
    private val onAcceptClicked: (TeacherRequestDataFull) -> Unit,
    private val onDeclineClicked: (TeacherRequestDataFull) -> Unit
) : RecyclerView.Adapter<AdminAdapter.TeacherRequestViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherRequestViewHolder {
        val binding =
            ItemTeacherRequestBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeacherRequestViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeacherRequestViewHolder, position: Int) {
        holder.bind(teacherRequests[position])
    }

    override fun getItemCount(): Int {
        return teacherRequests.size
    }

    inner class TeacherRequestViewHolder(private val binding: ItemTeacherRequestBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(teacherRequest: TeacherRequestDataFull) {
            when (teacherRequest.isApproved) {
                "accepted" -> {
                    binding.statusIcon.visibility = View.VISIBLE
                    binding.statusIcon.setImageResource(R.drawable.ic_completed)
                    binding.buttonAccept.visibility = View.GONE
                    binding.buttonDecline.visibility = View.GONE
                }

                "declined" -> {
                    binding.statusIcon.visibility = View.VISIBLE
                    binding.statusIcon.setImageResource(R.drawable.decline_icon)
                    binding.buttonAccept.visibility = View.GONE
                    binding.buttonDecline.visibility = View.GONE
                }

                else -> {
                    binding.buttonAccept.visibility = View.VISIBLE
                    binding.buttonDecline.visibility = View.VISIBLE
                }
            }

            binding.teacherNameTextView.text = teacherRequest.fullName
            binding.emailTextView.text = teacherRequest.email

            binding.buttonAccept.setOnClickListener { onAcceptClicked(teacherRequest) }
            binding.buttonDecline.setOnClickListener { onDeclineClicked(teacherRequest) }
        }
    }
}