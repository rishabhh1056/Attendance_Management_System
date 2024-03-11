package com.example.attendancemanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.attendancemanagementsystem.databinding.ActivityAdminDashBoardBinding
import com.example.attendancemanagementsystem.databinding.ActivityEmployeesDetailsBinding

class AdminDashBoard : AppCompatActivity() {
    private val binding : ActivityAdminDashBoardBinding by lazy{
        ActivityAdminDashBoardBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.addemp.setOnClickListener{
            startActivity(Intent(this ,AddEmployees::class.java ))
        }
        binding.empdetail.setOnClickListener {
            startActivity(Intent(this, EmployeesDetails::class.java))
        }
        binding.updateemp.setOnClickListener {
            startActivity(Intent(this, UpdateEmployees::class.java))
        }
        binding.salaryemp.setOnClickListener {
            startActivity(Intent(this, EmployeesDetails::class.java))
        }

    }
}