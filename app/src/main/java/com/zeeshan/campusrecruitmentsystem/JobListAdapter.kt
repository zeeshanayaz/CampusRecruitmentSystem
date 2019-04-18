package com.zeeshan.campusrecruitmentsystem

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.zeeshan.campusrecruitmentsystem.controller.dashboard.DashboardActivity
import com.zeeshan.campusrecruitmentsystem.model.Job
import java.text.SimpleDateFormat
import java.util.*

class JobListAdapter(
    var context: Context,
    var dataList: ArrayList<Job>,
    var itemClick: (job: Job) -> Unit,
    var itemLongClick: (job: Job) -> Unit

) :
    RecyclerView.Adapter<JobListAdapter.MyViewHolder>() {
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

        val title = view.findViewById<TextView>(R.id.cardJobTitle)
        val companyName = view.findViewById<TextView>(R.id.cardJobCompanyName)
        val jobType = view.findViewById<TextView>(R.id.cardJobType)
        val jobShift = view.findViewById<TextView>(R.id.cardJobShift)
        val jobSalary = view.findViewById<TextView>(R.id.cardJobSalary)
        val jobDescription = view.findViewById<TextView>(R.id.cardJobDescription)
        val jobLocation = view.findViewById<TextView>(R.id.cardJobLocation)
        val jobDate = view.findViewById<TextView>(R.id.cardJobDate)
        var deleteBtn = view.findViewById(R.id.cardDeleteJobBtn) as View
        var withdrawBtn = view.findViewById<Button>(R.id.cardJobWithdrawBtn)
        var applyBtn = view.findViewById<Button>(R.id.cardJobApplyBtn)

        fun bindJob(job: Job) {

            title.text = job.jobTitle
            companyName.text = "XYZ Company"
            jobType.text = job.jobType
            jobShift.text = job.jobShift
            jobSalary.text = job.jobEstimatedSalary.toString()
            jobDescription.text = job.jobDescription
            jobLocation.text = job.jobLocation

            val date = Date(job.jobCreatedDate)
            val format = SimpleDateFormat("MMM d, yyyy h:mm a")   //"yyyy.MM.dd HH:mm"
            val jobDateString = format.format(date)

            jobDate.text = jobDateString.toString()

            when (DashboardActivity.userType) {
                "Student" -> {
                    deleteBtn.visibility = View.GONE
                    withdrawBtn.visibility = View.GONE
                    applyBtn.visibility = View.VISIBLE
                }
                "Company" -> {
                    deleteBtn.visibility = View.VISIBLE
                    withdrawBtn.visibility = View.VISIBLE
                    applyBtn.visibility = View.GONE
                }
                "Admin" -> {
                    deleteBtn.visibility = View.VISIBLE
                    withdrawBtn.visibility = View.GONE
                    applyBtn.visibility = View.GONE
                }
            }

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