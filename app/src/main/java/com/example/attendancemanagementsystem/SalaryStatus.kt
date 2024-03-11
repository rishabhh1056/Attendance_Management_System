package com.example.attendancemanagementsystem

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.attendancemanagementsystem.databinding.ActivitySalaryStatusBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class SalaryStatus : AppCompatActivity() {
    private val binding:ActivitySalaryStatusBinding by lazy {
        ActivitySalaryStatusBinding.inflate(layoutInflater)
    }

    val currentDateTime: Calendar = Calendar.getInstance()
    val currentDate: String = "${currentDateTime.get(Calendar.DAY_OF_MONTH)}-${
        currentDateTime.get(
            Calendar.MONTH
        ) + 1
    }-${currentDateTime.get(Calendar.YEAR)}"

    val currentTime: String =
        "${currentDateTime.get(Calendar.HOUR_OF_DAY)}:${currentDateTime.get(Calendar.MINUTE)}"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        val db = Firebase.firestore
        val day = db.collection("soniya rajput").document("logoutDate: $currentDate")
        day.get()
            .addOnSuccessListener {
                if (it !=null)
                {
                    val workingDays = it.get("totalWorkingHrs").toString()
                    binding.days.text =workingDays
                    Log.e("TAG" , "workinDay m msg $workingDays")
                    Toast.makeText(this, "check $workingDays", Toast.LENGTH_SHORT).show()
                }
            }

    }
}