package com.zeeshan.campusrecruitmentsystem.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.support.v4.widget.CircularProgressDrawable
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.model.Student
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import com.zeeshan.campusrecruitmentsystem.utilities.DummyStudentImageUrl
import java.util.*

class StudentListAdapter(
    var context: Context,
    var dataList: ArrayList<Student>,
    var dbReference: FirebaseFirestore,
    var itemClick: (student: Student) -> Unit,
    var itemLongClick: (student: Student) -> Unit
) : RecyclerView.Adapter<StudentListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.card_student_list, null, false)
        return MyViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(viewHolder: MyViewHolder, position: Int) {
        viewHolder.bindJob(dataList[position])
    }

    inner class MyViewHolder(var view: View) : RecyclerView.ViewHolder(view) {

        val user = AppPref(context).getUser()

        val studentName = view.findViewById<TextView>(R.id.cardStudentName)
        val studentbio = view.findViewById<TextView>(R.id.cardStudentBio)
        private lateinit var imageUrl: String
        val studentImage = view.findViewById<ImageView>(R.id.cardStudentImage)
        val studentCity = view.findViewById<TextView>(R.id.cardStudentCity)
        val deleteBtn = view.findViewById<Button>(R.id.cardDeleteStudentBtn)

        val dialogBuilder = AlertDialog.Builder(context)


        //        val dbReference = FirebaseFirestore.getInstance()
        fun bindJob(student: Student) {


            studentName.text = student.firstName + " " + student.lastName
            if (student.summarry != "") studentbio.text = student.summarry
            else studentbio.text = "No Summarry"

            if (!student.profileImageUrl.isNullOrEmpty()) {
                imageUrl = student.profileImageUrl!!
            } else {
                imageUrl = DummyStudentImageUrl
            }
            studentCity.text = student.city + ", " + student.country

            Glide.with(context).applyDefaultRequestOptions(RequestOptions().apply() {
                placeholder(CircularProgressDrawable(context).apply {
                    strokeWidth = 2f
                    centerRadius = 50f
                    start()
                })
            }).load(imageUrl).into(studentImage)


            when (user!!.userAccountType) {
                "Admin" -> {
                    deleteBtn.visibility = View.VISIBLE
                }
                "Student", "Company" -> {
                    deleteBtn.visibility = View.GONE
                }
            }


            deleteBtn.setOnClickListener {

                val create: AlertDialog = dialogBuilder.create()
                dialogBuilder.setCancelable(false)
                dialogBuilder.setTitle("Delete Student!")
                dialogBuilder.setMessage("Do you want to delete ${student.firstName} ${student.lastName}")

                dialogBuilder.setPositiveButton("Delete", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dbReference.collection("Student").document(student.studentId).delete()
                            .addOnSuccessListener {
                                Log.d("STUDENT", "DocumentSnapshot successfully deleted!")
                                deleteStdFromUser(dbReference, student)
                            }
                            .addOnFailureListener { e -> Log.d("STUDENT", "Error deleting document", e) }


                        Toast.makeText(context, "${student.firstName} ${student.lastName} Deleted", Toast.LENGTH_SHORT)
                            .show()
                    }
                })
                dialogBuilder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        create.dismiss()
                    }
                })
                dialogBuilder.create()
                dialogBuilder.show()
            }


            view.setOnClickListener {
                itemClick(student)
            }

            view.setOnLongClickListener {
                itemLongClick(student)
                true
            }
        }

        private fun deleteStdFromUser(dbReference: FirebaseFirestore, student: Student) {
            dbReference.collection("Users").document(student.studentId).delete()
                .addOnSuccessListener {
                    //                    FirebaseAuth.getInstance().deleteUser(student.studentId)
                    Log.d("STUDENT", "DocumentSnapshot successfully deleted from User Table!")
                }
                .addOnFailureListener { Log.d("STUDENT", "DocumentSnapshot failed to deleted!") }
        }

    }
}