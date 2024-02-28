package com.example.learnit.ui.feature.notifications.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.learnit.R
import com.example.learnit.data.ApiConstants
import com.example.learnit.databinding.NotificationsListItemBinding

class NotificationsAdapter : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    private var notifications: List<String> = emptyList()

    fun setNotifications(notifications: List<String>) {
        this.notifications = notifications
        notifyDataSetChanged()
    }

    inner class NotificationViewHolder(private val binding: NotificationsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: String) {
            binding.titleTextView.text = notification

//            binding.root.setOnClickListener {
//                itemView.findNavController().navigate(
//                    R.id.action_notificationsFragment_to_chaptersFragment,
//                    bundleOf(ApiConstants.COURSE_ID to course.course_id)
//                )
//            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = NotificationsListItemBinding.inflate(inflater, parent, false)
        return NotificationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        val notification = notifications[position]
        holder.bind(notification)
    }

    override fun getItemCount(): Int {
        return notifications.size
    }
}