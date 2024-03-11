package com.example.attendancemanagementsystem

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.attendancemanagementsystem.databinding.ActivityAttendanceScreenBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.Calendar

class AttendanceScreen : AppCompatActivity() {
    private val binding: ActivityAttendanceScreenBinding by lazy {
        ActivityAttendanceScreenBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        val db = Firebase.firestore
        // database = Firebase.database.reference

//        binding.button.setOnClickListener {
//            //val pre = binding.present.text.toString()
//             val p = "present"
//            val pre = present(p)
//           // db.collection("present").document(currentDate).set(pre).addOnSuccessListener {
//                Toast.makeText(this, "present successful", Toast.LENGTH_SHORT).show()
//                Log.d("mag", "successful")
//            }.addOnFailureListener {
//                Toast.makeText(this, "Fail !!", Toast.LENGTH_SHORT).show()
//            }
//        }
    }
//    val currentDateTime : Calendar = Calendar.getInstance()
//
//    val currentDate :String = "${currentDateTime.get(Calendar.YEAR)}-${currentDateTime.get(Calendar.MONTH)+1}-${currentDateTime.get(Calendar.DAY_OF_MONTH)}"

}

//data class present(val present:String? = null)
