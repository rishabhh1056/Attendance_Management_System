package com.example.attendancemanagementsystem

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.attendancemanagementsystem.databinding.ActivityAdminLoginScreenBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.shashank.sony.fancytoastlib.FancyToast

class AdminLoginScreen : AppCompatActivity() {
    private val binding: ActivityAdminLoginScreenBinding by lazy{
        ActivityAdminLoginScreenBinding.inflate(layoutInflater)
    }
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var documentReference: DocumentReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        binding.adminlogin.setOnClickListener {
            val username:String = binding.adminusername.text.toString()
            val password:String = binding.adminpassword.text.toString()

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
                            checkUserAccessLevel(task.result.user!!.uid)
                        }
                        else
                        {
                            FancyToast.makeText(this,"Login Failed: ${task.exception?.message}",FancyToast.LENGTH_LONG,FancyToast.INFO,true).show()
                        }
                    }
            }
        }
    }

    private fun checkUserAccessLevel(uid: String) {
        documentReference = firestore.collection("Employee Accounts").document(uid)

        documentReference.get().addOnSuccessListener{
//            Toast.makeText(this, "success ${it.data}", Toast.LENGTH_SHORT).show()
//            Log.d("TAG" , "msg ${it.data}")
            if(it.getString("admin") != null)
            {
//                Log.d("TAG" , "in if condition ${it.data}")
                FancyToast.makeText(this,"Login Successful",FancyToast.LENGTH_SHORT,FancyToast.SUCCESS,true).show()
                startActivity(Intent(this, AdminDashBoard::class.java))

            }
            else
            {
                FancyToast.makeText(this, "I think You have not Admin Id password! Please try to login as a User", FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }

    }
}