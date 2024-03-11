package com.example.attendancemanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.example.attendancemanagementsystem.databinding.ActivityAdminLoginScreenBinding
import com.example.attendancemanagementsystem.databinding.ActivityEmployeLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.shashank.sony.fancytoastlib.FancyToast

class EmployeLoginScreen : AppCompatActivity() {
    private val binding: ActivityEmployeLoginScreenBinding by lazy{
        ActivityEmployeLoginScreenBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var documentReference: DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.login.setOnClickListener {
            val username:String = binding.empusername.text.toString()
            val password:String = binding.employepassword.text.toString()

            if (username.isEmpty()||password.isEmpty())
            {
                FancyToast.makeText(this,"Please Fill All Given Details",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.INFO,true).show()

            }
            else{
                auth.signInWithEmailAndPassword(username,password)
                    .addOnCompleteListener { task->
                        if (task.isSuccessful)
                        {
                            Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this, EmployeeDashboard::class.java))
                            finish()
                        }
                        else
                        {
                            FancyToast.makeText(this,"Login Failed: ${task.exception?.message}",
                                FancyToast.LENGTH_LONG,
                                FancyToast.INFO,true).show()
                        }
                    }
            }
        }
    }

}
