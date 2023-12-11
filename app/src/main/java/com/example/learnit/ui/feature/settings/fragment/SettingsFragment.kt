package com.example.learnit.ui.feature.settings.fragment

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.learnit.R
import com.example.learnit.data.SharedPreferences
import com.example.learnit.databinding.DialogFontSizeBinding
import com.example.learnit.databinding.FragmentSettingsBinding
import com.example.learnit.databinding.FragmentSplashBinding
import com.example.learnit.ui.activities.StartActivity
import com.example.learnit.ui.feature.settings.viewModel.SettingsFragmentViewModel

class SettingsFragment : Fragment() {
    private lateinit var binding: FragmentSettingsBinding
    private lateinit var dialogFontSizeBinding: DialogFontSizeBinding

    private lateinit var viewModel: SettingsFragmentViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        viewModel = ViewModelProvider(this).get(SettingsFragmentViewModel::class.java)

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

        binding.fontSizeLayout.setOnClickListener {
            showFontSizeDialog()
        }

        return binding.root
    }


    fun enableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
    }

    fun disableDarkMode() {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }

    private fun getSavedFontSize(): Int {
        val sharedPreferences = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        return sharedPreferences.getInt("fontSize", DEFAULT_FONT_SIZE)
    }

    private companion object {
        const val DEFAULT_FONT_SIZE = 16
    }

    private fun showFontSizeDialog() {
        dialogFontSizeBinding = DialogFontSizeBinding.inflate(LayoutInflater.from(requireContext()))

        val seekBar = dialogFontSizeBinding.fontSizeSeekBar
        val applyButton = dialogFontSizeBinding.applyButton

        // Az elmentett betűméret visszaállítása
        seekBar.progress = getSavedFontSize()

        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                dialogFontSizeBinding.selectedFontSizeText.text = "Selected Font Size: $progress sp"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {}

            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        val dialog = AlertDialog.Builder(requireContext())
            .setView(dialogFontSizeBinding.root)
            .create()

        applyButton.setOnClickListener {
            val selectedFontSize = seekBar.progress
            Log.d("FontSize", "Selected Font Size: $selectedFontSize sp")
            SharedPreferences.saveFontSize(requireContext(), selectedFontSize)
            dialog.dismiss()
        }

        dialog.show()
    }
}