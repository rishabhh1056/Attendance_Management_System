package com.example.attendancemanagementsystem

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.attendancemanagementsystem.databinding.ActivitySignUpScreenBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.storage
import com.shashank.sony.fancytoastlib.FancyToast
import com.squareup.picasso.Picasso

class AddEmployees : AppCompatActivity() {
    private val binding: ActivitySignUpScreenBinding by lazy {
        ActivitySignUpScreenBinding.inflate(layoutInflater)
    }
    private var profile: String? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var databaseReference: DatabaseReference
    private lateinit var firestore: FirebaseFirestore
    private lateinit var firebaseUser: FirebaseUser

    fun getProfile(img: String) {
        this.profile = img
    }

    fun setProfile(): String {
        return profile!!
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        auth = FirebaseAuth.getInstance()
        databaseReference = FirebaseDatabase.getInstance().reference
        firestore = FirebaseFirestore.getInstance()

        binding.profilebtn.setOnClickListener {
            var intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.type = "image/*"
            imageLauncher.launch(intent)

        }

        binding.signup2.setOnClickListener {
            val name: String = binding.name.text.toString()
            val workProfile: String = binding.post.text.toString()
            val salary: String = binding.salary.text.toString()
            val joiningDAte: String = binding.joining.text.toString()
            val email: String = binding.emailEdit.text.toString()
            val password: String = binding.passwordEdit.text.toString()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || workProfile.isEmpty() || salary.isEmpty() || joiningDAte.isEmpty()) {
                FancyToast.makeText(
                    this,
                    "Please Fill All Given Details",
                    FancyToast.LENGTH_SHORT,
                    FancyToast.INFO,
                    true
                ).show()
            } else {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->

                        if (task.isSuccessful) {
                            firebaseUser = auth.currentUser!!
                            val id = firebaseUser.uid
                            var img: String
                            if (profile != null) {
                                img = setProfile()
                            } else {
                                img =
                                    "https://firebasestorage.googleapis.com/v0/b/attendance-management-sy-73401.appspot.com/o/ProfilePhotos%2F1708700574261.png?alt=media&token=0a35d842-9f8d-4879-a94d-fc31cd0c4e76"
                            }
                            //val img = setProfile()
                            val detailItem = AddData(
                                img,
                                name,
                                workProfile,
                                salary,
                                joiningDAte,
                                email,
                                password,
                                id
                            )
                            firestore.collection("Employee Accounts").document(firebaseUser.uid)
                                .set(detailItem)
                            FancyToast.makeText(
                                this,
                                "Employee Add Successfully",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.SUCCESS,
                                true
                            ).show()
                            startActivity(Intent(this, AdminDashBoard::class.java))
                            finish()
                        } else {
                            FancyToast.makeText(
                                this,
                                "Registration Failed: ${task.exception?.message}",
                                FancyToast.LENGTH_LONG,
                                FancyToast.CONFUSING,
                                true
                            ).show()
                        }
                    }
            }

        }
    }

    val imageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.data != null) {
                val ref = Firebase.storage.reference.child(
                    "ProfilePhotos/" + System.currentTimeMillis() + "." + getFileType(it.data!!.data!!)
                )
                ref.putFile(it.data!!.data!!).addOnSuccessListener {
                    ref.downloadUrl.addOnSuccessListener {
                        getProfile(it.toString())
                        Picasso.get().load(it.toString()).into(binding.profilebtn)
                    }
                }
            } else {
                Toast.makeText(this, "you are not pick a photo ", Toast.LENGTH_SHORT).show()
            }

        }

    private fun getFileType(data: Uri): String? {
        val r = contentResolver
        val mimeType = MimeTypeMap.getSingleton()
        return mimeType.getExtensionFromMimeType(r.getType(data))
    }
}
