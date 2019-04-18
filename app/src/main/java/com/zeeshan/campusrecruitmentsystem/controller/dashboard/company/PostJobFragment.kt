package com.zeeshan.campusrecruitmentsystem.controller.dashboard.company


import android.app.ProgressDialog
import android.content.ContentValues
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.model.Job
import com.zeeshan.campusrecruitmentsystem.model.User
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import kotlinx.android.synthetic.main.fragment_post_job.*

class PostJobFragment : Fragment() {

    private lateinit var appPrefUser: User      //User from App Preference
    private lateinit var dbReference: FirebaseFirestore
    private lateinit var progress: ProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_post_job, container, false)

        appPrefUser = AppPref(activity!!).getUser()!!
        dbReference = FirebaseFirestore.getInstance()
        progress = ProgressDialog(activity)


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        submitJobPostBtn.setOnClickListener {
            if (checkInputFields()) {
                if (jobDescriptionText.length() < 100) {
                    jobDescriptionText.setError("Please provide company Description of atleast 100 letters.....")
                } else {
                    showProgress("PLease wait while we are creating your profile....")

                    postJob(
                        appPrefUser,
                        dbReference,
                        jobTitleText.text.trim().toString(),
                        jobDescriptionText.text.trim().toString(),
                        jobSkillsText.text.trim().toString(),
                        jobCareerLevelText.text.trim().toString(),
                        jobAppExpText.text.trim().toString(),
                        jobPositionText.text.trim().toString(),
                        jobLocationText.text.trim().toString(),
                        jobEstSalaryText.text.trim().toString(),
                        jobTypeText.text.trim().toString(),
                        jobShiftText.text.trim().toString()
                    )
                }
            } else {
                Toast.makeText(activity!!, "All input fields are required.", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun postJob(
        appPrefUser: User,
        dbReference: FirebaseFirestore,
        title: String,
        description: String,
        skills: String,
        careerLevel: String,
        experience: String,
        position: String,
        location: String,
        salary: String,
        type : String,
        shift : String
    ) {
        val dbRef = dbReference.collection("Job-Post").document()
        val jobPostData = Job(
            appPrefUser.userId,
            dbRef.id,
            title,
            description,
            skills,
            careerLevel,
            experience,
            position.toInt(),
            location,
            salary.toInt(),
            type,
            shift,
            System.currentTimeMillis()
            )

        dbRef.set(jobPostData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "${jobPostData.toString()} successfully written!")

                clearInputFields()

                progress.dismiss()
            }
            .addOnFailureListener {
                Log.w(ContentValues.TAG, "Error writing document", it)
                progress.dismiss()
            }
    }

    private fun clearInputFields() {
        jobTitleText.setText("")
        jobDescriptionText.setText("")
        jobSkillsText.setText("")
        jobCareerLevelText.setText("")
        jobAppExpText.setText("")
        jobPositionText.setText("")
        jobLocationText.setText("")
        jobEstSalaryText.setText("")
        jobTypeText.setText("")
        jobShiftText.setText("")

    }

    private fun showProgress(message: String) {
        progress.setMessage(message)
        progress.setCancelable(false)
        progress.show()
    }

    private fun checkInputFields(): Boolean {
        return jobTitleText.text.trim().toString() != "" &&
                jobDescriptionText.text.trim().toString() != "" &&
                jobSkillsText.text.trim().toString() != "" &&
                jobCareerLevelText.text.trim().toString() != "" &&
                jobAppExpText.text.trim().toString() != "" &&
                jobPositionText.text.trim().toString() != "" &&
                jobLocationText.text.trim().toString() != "" &&
                jobEstSalaryText.text.trim().toString() != "" &&
                jobTypeText.text.trim().toString() != "" &&
                jobShiftText.text.trim().toString() != ""
    }


}
