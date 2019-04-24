package com.zeeshan.campusrecruitmentsystem.controller.dashboard.company


import android.app.ProgressDialog
import android.content.ContentValues
import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.SpinnerAdapter
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.model.Job
import com.zeeshan.campusrecruitmentsystem.model.User
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import kotlinx.android.synthetic.main.fragment_post_job.*

class PostJobFragment : Fragment() {

    private lateinit var careerSpinner: Spinner
    private lateinit var appExpSpinner: Spinner
    private lateinit var jobShiftSpinner: Spinner
    private lateinit var jobTypeSpinner: Spinner

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

        careerSpinner = view.findViewById(R.id.jobCareerLevelSpinner)
        appExpSpinner = view.findViewById(R.id.jobApplicantExperienceSpinner)
        jobShiftSpinner = view.findViewById(R.id.jobShiftSpinner)
        jobTypeSpinner = view.findViewById(R.id.jobJobTypeSpinner)

        careerSpinner.adapter = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.career_level)
        ) as SpinnerAdapter?

        appExpSpinner.adapter = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.applicant_experience)
        ) as SpinnerAdapter?

        jobShiftSpinner.adapter = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.job_shift)
        ) as SpinnerAdapter?

        jobTypeSpinner.adapter = ArrayAdapter(
            activity!!,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.job_type)
        ) as SpinnerAdapter?


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
                        careerSpinner.selectedItem.toString(),
                        appExpSpinner.selectedItem.toString(),
                        jobPositionText.text.trim().toString(),
                        jobLocationText.text.trim().toString(),
                        jobEstSalaryText.text.trim().toString(),
                        jobTypeSpinner.selectedItem.toString(),
                        jobShiftSpinner.selectedItem.toString()
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
        type: String,
        shift: String
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
            System.currentTimeMillis(),
            "Active",
            arrayListOf("")
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
        careerSpinner.setSelection(0)
        appExpSpinner.setSelection(0)
        jobPositionText.setText("")
        jobLocationText.setText("")
        jobEstSalaryText.setText("")
        jobTypeSpinner.setSelection(0)
        jobShiftSpinner.setSelection(0)

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
                careerSpinner.selectedItemPosition != 0 &&
                appExpSpinner.selectedItemPosition != 0 &&
                jobPositionText.text.trim().toString() != "" &&
                jobLocationText.text.trim().toString() != "" &&
                jobEstSalaryText.text.trim().toString() != "" &&
                jobTypeSpinner.selectedItemPosition != 0 &&
                jobShiftSpinner.selectedItemPosition != 0
    }
}
