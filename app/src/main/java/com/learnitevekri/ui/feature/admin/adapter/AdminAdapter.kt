package com.learnitevekri.ui.feature.admin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.learnitevekri.data.user.teacher.model.TeacherRequestData
import com.learnitevekri.databinding.ItemTeacherRequestBinding

class AdminAdapter(private val teacherRequests: List<TeacherRequestData>) :
    RecyclerView.Adapter<AdminAdapter.TeacherRequestViewHolder>() {

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
        fun bind(teacherRequest: TeacherRequestData) {
            binding.teacherNameTextView.text = teacherRequest.fullName
            binding.emailTextView.text = teacherRequest.email
        }
    }
}