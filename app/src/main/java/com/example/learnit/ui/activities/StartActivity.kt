package com.example.learnit.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnit.databinding.ActivityStartBinding

class StartActivity : AppCompatActivity() {

    private lateinit var binding: ActivityStartBinding

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        binding = ActivityStartBinding.inflate(layoutInflater)

        return setContentView(binding.root)
    }
}