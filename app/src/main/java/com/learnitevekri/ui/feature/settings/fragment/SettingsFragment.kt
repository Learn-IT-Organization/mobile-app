package com.learnitevekri.ui.feature.settings.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.databinding.FragmentSettingsBinding
import com.learnitevekri.ui.activities.StartActivity
import com.learnitevekri.ui.feature.login.viewModel.LoginViewModel
import com.learnitevekri.ui.feature.settings.viewModel.SettingsViewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private val viewModel: SettingsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

        binding.logoutButton.setOnClickListener {
            viewModel.logOut()
            SharedPreferences.storeExpires(0)
            SharedPreferences.clearUserData()
            val intent =
                requireActivity().packageManager.getLaunchIntentForPackage(requireActivity().packageName)
            intent?.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent!!)
            requireActivity().finish()
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