package com.zeeshan.campusrecruitmentsystem.adapters

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.model.Company
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import java.util.*

class CompanyListAdapter(
    var context: Context,
    var dataList: ArrayList<Company>,
    var itemClick: (company: Company) -> Unit,
    var itemLongClick: (company: Company) -> Unit

) :
    RecyclerView.Adapter<CompanyListAdapter.MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, position: Int): MyViewHolder {
        val view: View = LayoutInflater.from(context).inflate(R.layout.card_company_list, null, false)
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

        //        val title = view.findViewById<TextView>(R.id.cardJobTitle)
        val companyName = view.findViewById<TextView>(R.id.cardCompanyName)
        val companyLocation = view.findViewById<TextView>(R.id.cardCompanyLocation)

        //        val jobType = view.findViewById<TextView>(R.id.cardJobType)
//        val jobShift = view.findViewById<TextView>(R.id.cardJobShift)
//        val jobSalary = view.findViewById<TextView>(R.id.cardJobSalary)
//        val jobDescription = view.findViewById<TextView>(R.id.cardJobDescription)
//        val jobLocation = view.findViewById<TextView>(R.id.cardJobLocation)
//        val jobDate = view.findViewById<TextView>(R.id.cardJobDate)
        var deleteBtn = view.findViewById<ImageButton>(R.id.cardDeleteCompanyBtn)
//        var withdrawBtn = view.findViewById<Button>(R.id.cardJobWithdrawBtn)
//        var applyBtn = view.findViewById<Button>(R.id.cardJobApplyBtn)

        fun bindJob(company: Company) {

            companyName.text = company.companyName
            companyLocation.text = "${company.companyCity}, ${company.companyCountry}"
//            val date = Date(job.jobCreatedDate)
//            val format = SimpleDateFormat("MMM d, yyyy h:mm a")   //"yyyy.MM.dd HH:mm"
//            val jobDateString = format.format(date)
//
//            jobDate.text = jobDateString.toString()

            when (user?.userAccountType) {
                "Student" -> {
                    deleteBtn.visibility = View.GONE
                }
                "Company" -> {
                    deleteBtn.visibility = View.GONE
                }
                "Admin" -> {
                    deleteBtn.visibility = View.VISIBLE
                }
            }

            view.setOnClickListener {
                itemClick(company)
            }

            view.setOnLongClickListener {
                itemLongClick(company)
                true
            }
        }
    }
}