package com.learnitevekri.ui.feature.notifications.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.learnitevekri.databinding.FragmentNotificationsBinding
import com.learnitevekri.ui.feature.notifications.adapter.NotificationsAdapter

class NotificationsFragment : Fragment() {

    private lateinit var binding: FragmentNotificationsBinding
    private lateinit var adapter: NotificationsAdapter
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        binding.imageViewBack.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.imageViewBack.setColorFilter(ContextCompat.getColor(requireContext(), android.R.color.white))

        adapter = NotificationsAdapter(requireContext())

        val linearLayoutManager = LinearLayoutManager(requireContext())
        binding.notificationsRecyclerView.layoutManager = linearLayoutManager
        binding.notificationsRecyclerView.adapter = adapter

        val sharedPreferences =
            requireContext().getSharedPreferences("notification_pref", Context.MODE_PRIVATE)
        val notificationsSet = sharedPreferences.getStringSet("notifications", mutableSetOf())

        val notificationsList = notificationsSet?.toList() ?: emptyList()
        if (notificationsList.isEmpty())
        {
            binding.textViewNoNotifications.visibility = View.VISIBLE
        }
        else {
            binding.textViewNoNotifications.visibility = View.GONE
            adapter.setNotifications(notificationsList)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val sharedPreferences =
            requireContext().getSharedPreferences("notification_pref", Context.MODE_PRIVATE)
        val notificationsSet = sharedPreferences.getStringSet("notifications", mutableSetOf())
        val notificationsList = notificationsSet?.toList() ?: emptyList()
        Log.d("NotificationsFragment", "Notifications: $notificationsList")
    }

    override fun onPause() {
        super.onPause()
        com.learnitevekri.data.MyFirebaseMessagingService.notificationIsActive()
    }
}