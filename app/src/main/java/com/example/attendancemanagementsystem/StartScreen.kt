package com.example.attendancemanagementsystem

import android.content.Intent
import android.os.Binder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import com.example.attendancemanagementsystem.databinding.ActivityEmployeesDetailsBinding
import com.example.attendancemanagementsystem.databinding.ActivityStartScreenBinding

class StartScreen : AppCompatActivity() {
    private val binding: ActivityStartScreenBinding by lazy {
        ActivityStartScreenBinding.inflate(layoutInflater )
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val animation = AnimationUtils.loadAnimation(this, R.anim.start_anim)
        binding.textView2.startAnimation(animation)
        Handler().postDelayed({
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        },4000)
    }
}