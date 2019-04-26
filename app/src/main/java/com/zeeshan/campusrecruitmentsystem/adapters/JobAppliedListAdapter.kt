package com.zeeshan.campusrecruitmentsystem.adapters

import android.app.AlertDialog
import android.content.Context
import android.graphics.Color
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.model.Job
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import java.text.SimpleDateFormat
import java.util.*

class JobAppliedListAdapter(
    var context: Context,
    var dataList: ArrayList<Job>,
    var dbReference: FirebaseFirestore,
    var itemClick: (job: Job) -> Unit,
    var itemLongClick: (job: Job) -> Unit
) : RecyclerView.Adapter<JobAppliedListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.card_job_list, null, false)
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

        val title = view.findViewById<TextView>(R.id.cardJobTitle)
        val companyName = view.findViewById<TextView>(R.id.cardJobCompanyName)
        val jobType = view.findViewById<TextView>(R.id.cardJobType)
        val jobShift = view.findViewById<TextView>(R.id.cardJobShift)
        val jobSalary = view.findViewById<TextView>(R.id.cardJobSalary)
        val jobDescription = view.findViewById<TextView>(R.id.cardJobDescription)
        val jobLocation = view.findViewById<TextView>(R.id.cardJobLocation)
        val jobDate = view.findViewById<TextView>(R.id.cardJobDate)
                var deleteBtn = view.findViewById<Button>(R.id.cardDeleteJobBtn)
        var withdrawBtn = view.findViewById<Button>(R.id.cardJobWithdrawBtn)
        var applyBtn = view.findViewById<Button>(R.id.cardJobApplyBtn)
        var checkJob = view.findViewById<TextView>(R.id.cardActiveCheck)
        var activeteJobBtn = view.findViewById<Button>(R.id.cardJobActiveBtn)
        var applicantJobBtn = view.findViewById<Button>(R.id.cardViewApplicantsJobBtn)

        val dialogBuilder = AlertDialog.Builder(context)


        //        val dbReference = FirebaseFirestore.getInstance()
        fun bindJob(job: Job) {
            title.text = job.jobTitle
            companyName.text = "XYZ Company"
            jobType.text = job.jobType
            jobShift.text = job.jobShift
            jobSalary.text = job.jobEstimatedSalary.toString()
            jobDescription.text = job.jobDescription
            jobLocation.text = job.jobLocation
            checkJob.text = job.jobStatus

            val date = Date(job.jobCreatedDate)
            val format = SimpleDateFormat("MMM d, yyyy h:mm a")   //"yyyy.MM.dd HH:mm"
            val jobDateString = format.format(date)

            jobDate.text = jobDateString.toString()

            when (user?.userAccountType) {
                "Student" -> {
                    deleteBtn.visibility = View.GONE
                    withdrawBtn.visibility = View.VISIBLE
                    applyBtn.visibility = View.GONE
                    activeteJobBtn.visibility = View.GONE
                    applicantJobBtn.visibility = View.GONE
//                    if (job.jobApplicant!!.contains(user.userId)){
//                        applyBtn.setText("Applied")
//                    }
                }
//                "Company", "Admin" -> {
//
//                    deleteBtn.visibility = View.VISIBLE
//                    applicantJobBtn.visibility = View.VISIBLE
//                    if (job.jobStatus == "Active") {
//                        withdrawBtn.visibility = View.VISIBLE
//                        activeteJobBtn.visibility = View.GONE
//                    } else {
//                        withdrawBtn.visibility = View.GONE
//                        activeteJobBtn.visibility = View.VISIBLE
//                    }
//                    applyBtn.visibility = View.GONE
//                }
////                "Admin" -> {
////                    deleteBtn.visibility = View.VISIBLE
////                    withdrawBtn.visibility = View.GONE
////                    activeteJobBtn.visibility = View.GONE
////                    applyBtn.visibility = View.GONE
////                }
            }
            if (job.jobStatus == "Inactive") {
                checkJob.setTextColor(Color.parseColor("#FF0000"))
            } else if (job.jobStatus == "Active") {
                checkJob.setTextColor(Color.parseColor("#4CAF50"))
            }


//            applyBtn.setOnClickListener {
//                //                Toast.makeText(context, "Apply Btn Clicked", Toast.LENGTH_SHORT).show()
//                var applicantList = job.jobApplicant
//
//                if (applicantList?.size == 1 && applicantList[0] == "") {
//                    applicantList[0] = user!!.userId
//                } else if (!applicantList!!.contains(user!!.userId)) {
//                    applicantList.add(user.userId)
//                } else{
//                    Toast.makeText(context, "You have already applied for the job..", Toast.LENGTH_SHORT).show()
//                }
//                dbReference.collection("Job-Post").document(job.jobId).update("jobApplicant", applicantList)
//                    .addOnSuccessListener {
//                        Log.d(
//                            "JOBSTATUS",
//                            "DocumentSnapshot successfully updated Applicant List"
//                        )
//                        applyBtn.setText("Applied")
//                    }
//                    .addOnFailureListener { e -> Log.d("JOBSTATUS", "Error updating document", e) }
//            }

            withdrawBtn.setOnClickListener {
                val applicantList = job.jobApplicant
                val index = applicantList!!.indexOf(user?.userId)
                applicantList.removeAt(index)
                dbReference.collection("Job-Post").document(job.jobId).update("jobApplicant", applicantList)
                    .addOnSuccessListener { Log.d("JOBSTATUS", "DocumentSnapshot successfully updated!") }
                    .addOnFailureListener { e -> Log.d("JOBSTATUS", "Error updating document", e) }

//                Toast.makeText(context, "Withdraw Btn Clicked", Toast.LENGTH_SHORT).show()

            }

//            deleteBtn.setOnClickListener {
//
//                val create: AlertDialog = dialogBuilder.create()
//                dialogBuilder.setCancelable(false)
//                dialogBuilder.setTitle("Delete Job!")
//                dialogBuilder.setMessage("Do you want to delete ${job.jobTitle}")
//
//                dialogBuilder.setPositiveButton("Delete", object : DialogInterface.OnClickListener {
//                    override fun onClick(dialog: DialogInterface?, which: Int) {
//                        dbReference.collection("Job-Post").document(job.jobId).delete()
//                            .addOnSuccessListener { Log.d("JOBSTATUS", "DocumentSnapshot successfully deleted!") }
//                            .addOnFailureListener { e -> Log.d("JOBSTATUS", "Error deleting document", e) }
//                        Toast.makeText(context, "${job.jobTitle} Deleted", Toast.LENGTH_SHORT).show()
//                    }
//                })
//                dialogBuilder.setNegativeButton("Cancel", object : DialogInterface.OnClickListener {
//                    override fun onClick(dialog: DialogInterface?, which: Int) {
//                        create.dismiss()
//                    }
//                })
//                dialogBuilder.create()
//                dialogBuilder.show()
//            }

//            activeteJobBtn.setOnClickListener {
//                Toast.makeText(context, "Activate Btn Clicked", Toast.LENGTH_SHORT).show()
//                dbReference.collection("Job-Post").document(job.jobId).update("jobStatus", "Active")
//                    .addOnSuccessListener { Log.d("JOBSTATUS", "DocumentSnapshot successfully updated!") }
//                    .addOnFailureListener { e -> Log.d("JOBSTATUS", "Error updating document", e) }
//            }

//            applicantJobBtn.setOnClickListener {
//                Toast.makeText(context, "Applicants Btn Clicked", Toast.LENGTH_SHORT).show()
//
//            }

            view.setOnClickListener {
                itemClick(job)
            }

            view.setOnLongClickListener {
                itemLongClick(job)
                true
            }
        }
    }
}