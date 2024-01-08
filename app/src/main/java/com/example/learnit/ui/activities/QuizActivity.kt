package com.example.learnit.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.learnit.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuizBinding.inflate(layoutInflater)

        return setContentView(binding.root)
    }
}