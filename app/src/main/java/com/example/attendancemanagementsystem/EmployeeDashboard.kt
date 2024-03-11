package com.example.attendancemanagementsystem

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.attendancemanagementsystem.databinding.ActivityEmployeeDashboardBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.shashank.sony.fancytoastlib.FancyToast
import java.lang.NullPointerException
import java.text.SimpleDateFormat
import java.util.Calendar
import kotlin.math.abs
import android.net.ConnectivityManager
import androidx.appcompat.app.AlertDialog


class EmployeeDashboard : AppCompatActivity() {
    private val binding: ActivityEmployeeDashboardBinding by lazy {
        ActivityEmployeeDashboardBinding.inflate(layoutInflater)
    }

    // Create Getter Or Setter..........
    private var time: String? = null
    private fun getTime(time: String) {
        this.time = time
        Log.e("TAG", "getTime mai ${this.time}")
    }

    private fun setTime(): String? {
        Log.e("TAG", "setTime mai ${this.time}")
        return time

    }


    //        Current Date And Time................
    val currentDateTime: Calendar = Calendar.getInstance()
    val currentDate: String = "${currentDateTime.get(Calendar.DAY_OF_MONTH)}-${
        currentDateTime.get(
            Calendar.MONTH
        ) + 1
    }-${currentDateTime.get(Calendar.YEAR)}"

    val currentTime: String =
        "${currentDateTime.get(Calendar.HOUR_OF_DAY)}:${currentDateTime.get(Calendar.MINUTE)}"

    //    Variable Initialize....................
    private lateinit var db: FirebaseFirestore
    private lateinit var loginTime: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var employeeName: String? = null
    private lateinit var startTime: Calendar
    private lateinit var endTime: Calendar
    private lateinit var time1:Calendar
    private lateinit var time2:Calendar
    // setPerformance initialised..........
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        db = Firebase.firestore
        loginTime = Firebase.firestore
        auth = FirebaseAuth.getInstance()

// Details Show On Employee Dashboard****************************
        val user: FirebaseUser = auth.currentUser!!

        val ref = db.collection("Employee Accounts").document(user.uid)
        ref.get()
            .addOnSuccessListener {
                if (it != null) {

                    val name: String = it["name"].toString()
                    val post: String = it["workProfile"].toString()
                    val salary: String = it["salary"].toString()
                    val email: String = it["email"].toString()
                    val profile: String = it["profile"].toString()
                    binding.progressBar.isVisible = false
                    binding.showname.text = name
                    binding.showpost.text = post
                    binding.showsalary.text = salary
                    binding.showemail.text = email
                    Glide.with(this).load(Uri.parse(profile)).into(binding.showprofile)
                    employeeName = name

                } else {
                    Log.d("else msg", "No such document")
                }
            }
//*************************************************************************************************


                // Start Code For Login Button Here................

                sharedPreferences = getSharedPreferences(
                    "ButtonClicked",
                    Context.MODE_PRIVATE
                )   // initialized preference
                val lastClickLoginDate = sharedPreferences.getString("lastClicked", "")

                if (lastClickLoginDate != currentDate) // check last clicked date or current-date in equal or not!
                {
                    binding.loginbtn.isEnabled = true
                } else {
                    binding.loginbtn.isEnabled = true
                }


                binding.loginbtn.setOnClickListener {

                    if (!isNetworkAvailable()) {
                        // Internet connection is available
                        showWarningDialog()
                    }

                    val p = "Present"
                    val data = present(p, currentTime)
                    getTime(currentTime)
                    db.collection("$employeeName").document("loginDate: $currentDate").set(data)
                        .addOnSuccessListener {
                            FancyToast.makeText(
                                this,
                                "Login Marked Successful",
                                FancyToast.LENGTH_SHORT,
                                FancyToast.SUCCESS,
                                true
                            ).show()
                            binding.loginbtn.isEnabled = false
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "check Internet ${it.message.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                    sharedPreferences.edit().putString("lastClicked", currentDate).apply()
                }
                // End Code For Login Button here.........................

                binding.to.setOnClickListener {

                    db.collection("$employeeName").document("loginDate: $currentDate").get()
                        .addOnSuccessListener {
                            if (it != null) {
                                var loginTime: String? = null
                                loginTime = it.data?.get("currentTime")?.toString()
                                    ?: "Please Login Marked First!"
                                getTime(loginTime!!)
                                Log.e("TAG", "msg in it block $it")
                                FancyToast.makeText(
                                    this,
                                    "Your Login Time: $loginTime",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.INFO,
                                    true
                                ).show()
                            } else {
                                Toast.makeText(
                                    this,
                                    "Please First Login Marked",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                        .addOnFailureListener {
                            Toast.makeText(
                                this,
                                "Read it!! ${it.message.toString()}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                }


                // Start Code for Logout Button here......................................

                val lastClickLogoutDate =
                    sharedPreferences.getString("lastLogoutBtnClickedDate", "")

                if (lastClickLogoutDate != currentDate) {
                    binding.logoutbtn.isEnabled = true
                } else {
                    binding.logoutbtn.isEnabled = true
                }

                binding.logoutbtn.setOnClickListener {

                    if (!isNetworkAvailable()) {     // <---- Check Internet connected or not
                        // Internet connection is available
                        showWarningDialog()
                    }


                    val out = "logout"
                    val format = SimpleDateFormat("HH:mm")
                    startTime = Calendar.getInstance()
                    endTime = Calendar.getInstance()


                    try {
                        val time: String? = setTime()

                        Log.e("tag", "msg in time $time")

                        startTime.time = time?.let { it1 -> format.parse(it1) }!!
                        endTime.time = format.parse(currentTime)!!                         // Edited///

                        val diff = abs(startTime.timeInMillis - endTime.timeInMillis)
                        // Log.e("TAG", "msg h $diff")
                        val hour = diff / (60 * 60 * 1000)
                        val minutes = diff % (60 * 60 * 1000) / (60 * 1000)
                        val totalHr = String.format("%02d:%02d", hour,minutes)
                        val logout = present(out, currentTime, totalHr)

                        db.collection("$employeeName").document("logoutDate: $currentDate").set(logout)
                            .addOnSuccessListener {
                                FancyToast.makeText(
                                    this,
                                    "Logout Marked Successfully",
                                    FancyToast.LENGTH_SHORT,
                                    FancyToast.SUCCESS,
                                    true
                                ).show()
                                binding.logoutbtn.isEnabled = false
                            }
                    } catch (e: NullPointerException) {
                        FancyToast.makeText(
                            this,
                            "Error!! Please press To button First for get Login Time ",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.WARNING,
                            true
                        ).show()
                    } catch (e: Exception) {
                        FancyToast.makeText(
                            this,
                            "Something went wrong ${e.message.toString()}",
                            FancyToast.LENGTH_SHORT,
                            FancyToast.ERROR,
                            true
                        ).show()
                    }

                    sharedPreferences.edit().putString("lastLogoutBtnClickedDate", currentDate)
                        .apply()


                }

                // Start code For Show Working Days...................................

        binding.CheckSalary.setOnClickListener {
            db.collection("$employeeName").document("logoutDate: $currentDate").get()
                .addOnSuccessListener {
                    if (it != null) {
                        var workingHr: String? = null
                        workingHr = it.data?.get("totalWorkingHrs")?.toString() ?: "Empty"

                        time1 = Calendar.getInstance()
                        time2 = Calendar.getInstance()
                        val time3 :Calendar = Calendar.getInstance()
                        val format = SimpleDateFormat("HH:mm")

                        time1.time = format.parse(workingHr)!!
                        time2.time = format.parse("07:30")!!
                        time3.time = format.parse("06:00")!!

                        if (time1.time >= time2.time)
                        {
                            binding.workingdays.text = workingHr
                        }
                        else if(time1.time<=time2.time || time1.time >= time3.time )
                        {
                            binding.halfdays.text = workingHr
                        }

                    }
                }
        }

            }

        private fun isNetworkAvailable(): Boolean {
            val connectivityManager =
                getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            val networkInfo = connectivityManager.activeNetworkInfo
            return networkInfo != null && networkInfo.isConnected
        }


        private fun showWarningDialog() {
            // Build the alert dialog
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Warning")
            builder.setMessage("PLEASE CONNECT WITH INTERNET")
            builder.setIcon(android.R.drawable.ic_dialog_alert)

            // Set a button for the user to dismiss the dialog
            builder.setPositiveButton("OK") { dialog, which ->
                // You can perform any action here when the user clicks OK
                // For example, you can close the activity or dismiss the dialog
                dialog.dismiss()
            }

            // Create and show the dialog
            val dialog = builder.create()
            dialog.show()
        }


    }

//Data Class
private data class present(
    val present: String? = null,
    val currentTime: String? = null,
    val totalWorkingHrs: String? = null
)





