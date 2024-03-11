package com.example.attendancemanagementsystem

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.attendancemanagementsystem.databinding.EmployeDetailsItemBinding
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore



class RvAdapter(val data: ArrayList<RvModel>, val context: Context) :
    RecyclerView.Adapter<RvAdapter.viewHolder>() {

    class viewHolder(val binding: EmployeDetailsItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(data: RvModel, context: Context) {
            binding.nameitem.text = data.name
            binding.postitem.text = data.workProfile
            binding.salaryitem.text = data.salary
            binding.joindateitem.text = data.joiningDate
            binding.emailitem.text = data.email
            binding.passitem.text = data.password
            Glide.with(context).load(data.profile).into(binding.profileitem)

            binding.delete.setOnClickListener {
                val db = Firebase.firestore
//                val user = FirebaseAuth.getInstance().currentUser
//                user?.delete()
                db.collection("Employee Accounts").document(data.user).delete()
                    .addOnSuccessListener {
                        Toast.makeText(context, "Details Deleted!!", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Details Not Deleted??", Toast.LENGTH_SHORT).show()
                    }

            }

            binding.update.setOnClickListener {
                var intent = Intent(context, UpdateScreen::class.java)
                intent.putExtra("Name", data.name )
                intent.putExtra("Post", data.workProfile )
                intent.putExtra("Salary", data.salary )
                intent.putExtra("Joining Date", data.joiningDate )
                intent.putExtra("Email", data.email )
                intent.putExtra("Password", data.password )
                intent.putExtra("id", data.user)
                intent.putExtra("profile", data.profile)
                context.startActivity(intent)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val binding: EmployeDetailsItemBinding =
            EmployeDetailsItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewHolder(binding)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        val data: RvModel = data[position]
        holder.bind(data, context)
    }
}