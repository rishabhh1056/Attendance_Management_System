package com.example.attendancemanagementsystem

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.attendancemanagementsystem.databinding.ActivityEmployeesDetailsBinding
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot

class EmployeesDetails : AppCompatActivity() {
    private val binding: ActivityEmployeesDetailsBinding by lazy {
        ActivityEmployeesDetailsBinding.inflate(layoutInflater)
    }
    private lateinit var db: FirebaseFirestore
    private lateinit var rvAdapter: RvAdapter
    private lateinit var dataList: ArrayList<RvModel>
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        recyclerView = binding.Rc
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        dataList = arrayListOf()

        rvAdapter = RvAdapter(dataList, this)

        recyclerView.adapter = rvAdapter

        EventChangeListner()


    }

    private fun EventChangeListner() {
        db = FirebaseFirestore.getInstance()
        db.collection("Employee Accounts")
            .addSnapshotListener(object : EventListener<QuerySnapshot> {
                override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                    if (error != null) {
                        Log.e("Firestore Error", error.message.toString())
                        Toast.makeText(
                            this@EmployeesDetails,
                            "Error ${error.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    dataList.clear()
                    for (dc: DocumentChange in value?.documentChanges!!) {
                        if (dc.type == DocumentChange.Type.ADDED) {
                            dataList.add(dc.document.toObject(RvModel::class.java))
                        }
                    }
                    rvAdapter.notifyDataSetChanged()
                }
            })
    }
}