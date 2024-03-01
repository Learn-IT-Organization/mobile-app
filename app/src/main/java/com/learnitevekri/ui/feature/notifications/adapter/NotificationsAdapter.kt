package com.learnitevekri.ui.feature.notifications.adapter

import android.content.Context
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.learnitevekri.R
import com.learnitevekri.databinding.NotificationsListItemBinding

class NotificationsAdapter(private val context: Context) : RecyclerView.Adapter<NotificationsAdapter.NotificationViewHolder>() {

    private var notifications: List<String> = emptyList()
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("notification_button_pressed", Context.MODE_PRIVATE)

    fun setNotifications(notifications: List<String>) {
        this.notifications = notifications
        notifyDataSetChanged()
    }

    inner class NotificationViewHolder(private val binding: NotificationsListItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(notification: String) {

            binding.titleTextView.text = notification
            val notificationKey = "notification_$adapterPosition"

            val wasClicked = sharedPreferences.getBoolean(notificationKey, false)

            val backgroundColor = if (wasClicked) {
                ContextCompat.getColor(context, android.R.color.white)
            } else {
                ContextCompat.getColor(context, R.color.md_theme_primaryContainer)
            }
            binding.notificationContainer.setBackgroundColor(backgroundColor)
            binding.root.setOnClickListener {
                sharedPreferences.edit().putBoolean(notificationKey, true).apply()

                itemView.findNavController().navigate(
                    R.id.action_notificationsFragment_to_coursesFragment
                )
            }
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