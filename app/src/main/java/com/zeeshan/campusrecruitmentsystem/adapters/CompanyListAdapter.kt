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
import com.zeeshan.campusrecruitmentsystem.model.Company
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import com.zeeshan.campusrecruitmentsystem.utilities.DummyCompanyImageUrl
import java.util.*

class CompanyListAdapter(
    var context: Context,
    var dataList: ArrayList<Company>,
    var dbReference: FirebaseFirestore,
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
        val companyBio = view.findViewById<TextView>(R.id.cardCompanyBio)
        private lateinit var imageUrl: String
        val companyImage = view.findViewById<ImageView>(R.id.cardCompanyImage)
        val dialogBuilder = AlertDialog.Builder(context)


        //        val jobType = view.findViewById<TextView>(R.id.cardJobType)
//        val jobShift = view.findViewById<TextView>(R.id.cardJobShift)
//        val jobSalary = view.findViewById<TextView>(R.id.cardJobSalary)
//        val jobDescription = view.findViewById<TextView>(R.id.cardJobDescription)
//        val jobLocation = view.findViewById<TextView>(R.id.cardJobLocation)
//        val jobDate = view.findViewById<TextView>(R.id.cardJobDate)
        var deleteBtn = view.findViewById<Button>(R.id.cardDeleteCompanyBtn)
//        var withdrawBtn = view.findViewById<Button>(R.id.cardJobWithdrawBtn)
//        var applyBtn = view.findViewById<Button>(R.id.cardJobApplyBtn)

        fun bindJob(company: Company) {

            companyName.text = company.companyName
            companyLocation.text = "${company.companyCity}, ${company.companyCountry}"

            if (company.companyDescription != "") companyBio.text = company.companyDescription
            else companyBio.text = "No Description"


            if (!company.companyLogoUrl.isNullOrEmpty()) {
                imageUrl = company.companyLogoUrl!!
            } else {
                imageUrl = DummyCompanyImageUrl

            }

            Glide.with(context).applyDefaultRequestOptions(RequestOptions().apply() {
                placeholder(CircularProgressDrawable(context).apply {
                    strokeWidth = 2f
                    centerRadius = 50f
                    start()
                })
            }).load(imageUrl).into(companyImage)

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

            deleteBtn.setOnClickListener {
                val create: AlertDialog = dialogBuilder.create()
                dialogBuilder.setCancelable(false)
                dialogBuilder.setTitle("Delete Company!")
                dialogBuilder.setMessage("Do you want to delete ${company.companyName}")

                dialogBuilder.setPositiveButton("Delete", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        dbReference.collection("Company").document(company.companyId).delete()
                            .addOnSuccessListener { Log.d("Company", "DocumentSnapshot successfully deleted!") }
                            .addOnFailureListener { e -> Log.d("STUDENT", "Error deleting document", e) }
                        Toast.makeText(context, "${company.companyName} Deleted", Toast.LENGTH_SHORT).show()
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
                itemClick(company)
            }

            view.setOnLongClickListener {
                itemLongClick(company)
                true
            }
        }
    }
}