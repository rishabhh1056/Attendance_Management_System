package com.example.attendancemanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.attendancemanagementsystem.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val binding : ActivityMainBinding by lazy {
          ActivityMainBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.cardView1.setOnClickListener {
            startActivity(Intent(this, AdminLoginScreen::class.java))
        }
        binding.cardView2.setOnClickListener {
            startActivity(Intent(this, EmployeLoginScreen::class.java))
        }
        binding.cardView3.setOnClickListener {
            startActivity(Intent(this, ClientScreen::class.java))
        }
    }
}