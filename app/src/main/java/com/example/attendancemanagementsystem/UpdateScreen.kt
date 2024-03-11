package com.example.attendancemanagementsystem

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.MimeTypeMap
import androidx.activity.result.contract.ActivityResultContracts
import com.example.attendancemanagementsystem.databinding.ActivityEmployeesDetailsBinding
import com.example.attendancemanagementsystem.databinding.ActivityUpdateScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.storage
import com.squareup.picasso.Picasso

class UpdateScreen : AppCompatActivity() {
    private val binding : ActivityUpdateScreenBinding by lazy {
        ActivityUpdateScreenBinding.inflate(layoutInflater)
    }
    private var profile:String? = null
    fun getProfile(img: String) {
        this.profile = img
    }

    fun setProfile(): String {
        return profile!!
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val db = Firebase.firestore

         binding.updatename.setText(intent.getStringExtra("Name"))
         binding.updatepost.setText(intent.getStringExtra("Post"))
         binding.updatesalary.setText(intent.getStringExtra("Salary"))
         binding.updatejoiningdate.setText(intent.getStringExtra("Joining Date"))

        binding.updateprofile.setOnClickListener{
            var intent = Intent()
                intent.action = Intent.ACTION_PICK
                intent.type = "image/*"
                imageLauncher.launch(intent)
            //Picasso.get().load(intent.getStringExtra("profile")).into(binding.updateprofile)
        }

        binding.updatebtn.setOnClickListener {
            var updateProfile:String
                 if(profile != null)
                 {
                      updateProfile = setProfile()
                 }
                 else{
                     updateProfile = intent.getStringExtra("profile")!!
                 }
            var details = hashMapOf(
                "name" to binding.updatename.text.toString(),
                "workProfile" to binding.updatepost.text.toString(),
                "salary" to binding.updatesalary.text.toString(),
                "joiningDate" to binding.updatejoiningdate.text.toString(),
                "email" to intent.getStringExtra("Email"),
                "password" to intent.getStringExtra("Password"),
                "user" to intent.getStringExtra("id"),
                "profile" to updateProfile

            )
            db.collection("Employee Accounts").document(intent.getStringExtra("id")!!).set(details)
            finish()

        }
        
        
    }
    val imageLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        if (it.data != null)
        {
            val ref = Firebase.storage.reference.child("updated photo/"+ System.currentTimeMillis()+ "."+ getFileType(it.data!!.data!!))

            ref.putFile(it.data!!.data!!).addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    getProfile(it.toString())
                    Picasso.get().load(it.toString()).into(binding.updateprofile)
                }
            }
        }
//        else
//        {
//            getProfile(intent.getStringExtra("profile")!!)
//        }
    }

    private fun getFileType(data: Uri): String? {
        val r = contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getExtensionFromMimeType(r.getType(data))
    }
}