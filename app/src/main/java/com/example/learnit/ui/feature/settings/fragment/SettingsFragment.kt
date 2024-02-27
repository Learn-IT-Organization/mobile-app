package com.example.learnit.ui.feature.settings.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.learnit.data.SharedPreferences
import com.example.learnit.databinding.FragmentSettingsBinding
import com.example.learnit.ui.activities.StartActivity
import com.example.learnit.ui.feature.settings.viewModel.SettingsFragmentViewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var viewModel: SettingsFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this)[SettingsFragmentViewModel::class.java]

        binding.logoutButton.setOnClickListener {
            SharedPreferences.storeExpires(0)
            SharedPreferences.clearUserData()
            val intent = Intent(context, StartActivity::class.java)
            startActivity(intent)
        }

        val isDarkModeEnabled = SharedPreferences.getDarkModeStatus(requireContext())
        binding.themeSwitch.isChecked = isDarkModeEnabled

        if (isDarkModeEnabled) {
            enableDarkMode()
        }

        binding.themeSwitch.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                enableDarkMode()
            } else {
                disableDarkMode()
            }
            SharedPreferences.setDarkModeStatus(requireContext(), isChecked)
        }

        return binding.root
    }

    private fun enableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    private fun disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}