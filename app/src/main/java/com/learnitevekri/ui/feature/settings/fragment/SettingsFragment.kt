package com.learnitevekri.ui.feature.settings.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.learnitevekri.data.SharedPreferences
import com.learnitevekri.databinding.FragmentSettingsBinding
import com.learnitevekri.ui.activities.StartActivity

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)

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