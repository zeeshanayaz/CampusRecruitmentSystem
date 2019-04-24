package com.zeeshan.campusrecruitmentsystem.controller.profile


import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.widget.CircularProgressDrawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.model.Company
import com.zeeshan.campusrecruitmentsystem.model.Student
import com.zeeshan.campusrecruitmentsystem.model.User
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import kotlinx.android.synthetic.main.fragment_my_profile.*

class MyProfileFragment : Fragment() {

    var selectedPhotoUri: Uri? = null
    private lateinit var appPrefUser: User                  //User from App Preference
    private lateinit var appPrefCompany: Company         //Company from App Preference
    private lateinit var appPrefStudent: Student        //Company from App Preference
    private lateinit var dbReference: FirebaseFirestore


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_my_profile, container, false)

        appPrefUser = AppPref(activity!!).getUser()!!
        dbReference = FirebaseFirestore.getInstance()


        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (appPrefUser != null) {
            when (appPrefUser.userAccountType) {
                "Student" -> {
                    displayStudentData()
                }
                "Company" -> {
                    displayCompanyData()
                }
            }
        }

        profileSeletPhotoBtn.setOnClickListener {
            Log.d("ProfileFragment", "Profile Select Photo Button Pressed")
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            selectedPhotoUri = data.data
            val inputStream = activity!!.contentResolver.openInputStream(selectedPhotoUri!!)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            profileImageImageView.setImageBitmap(bitmap)
            profileSeletPhotoBtn.alpha = 0f
            uploadImageToFirebase()
        }
    }

    private fun uploadImageToFirebase() {
        val fileName = appPrefUser.userId
        val ref = FirebaseStorage.getInstance().getReference("/images/profileImages/$fileName")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener { uri ->
                ref.downloadUrl.addOnSuccessListener {
                    dbReference.collection(appPrefUser.userAccountType).document(appPrefUser.userId)
                        .update("profileImageUrl", "$it")
                        .addOnSuccessListener { Log.d("MYPROFILE", "DocumentSnapshot successfully updated!") }
                        .addOnFailureListener { e -> Log.d("MYPROFILE", "Error updating document", e) }

                    updateAppPrefStudent()
                }

            }

    }

    private fun updateAppPrefStudent() {
        dbReference.collection(appPrefUser.userAccountType).document(appPrefUser.userId).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val retrieveData: Student = it.toObject(Student::class.java)!!
                    AppPref(activity!!).setStudent(retrieveData)
                }
            }
    }

    private fun displayStudentData() {
        appPrefStudent = AppPref(activity!!).getStudent()!!
        if (!appPrefStudent.firstName.isNullOrEmpty() && !appPrefStudent.lastName.isNullOrEmpty()) {
            profileUserNameText.text = "${appPrefStudent.firstName} ${appPrefStudent.lastName}"
            profileUserNameText.setTextColor(resources.getColor(R.color.colorPrimary))
        } else {
            profileUserNameText.setText(getString(R.string.user_name))
            profileUserNameText.setTextColor(resources.getColor(R.color.colorSecondaryText))
        }

        if (!appPrefStudent.profileImageUrl.isNullOrEmpty()) {
            profileSeletPhotoBtn.alpha = 0f
            Glide.with(activity!!).applyDefaultRequestOptions(RequestOptions().apply() {
                placeholder(CircularProgressDrawable(activity!!).apply {
                    strokeWidth = 2f
                    centerRadius = 50f
                    start()
                })
            }).load(appPrefStudent.profileImageUrl).into(profileImageImageView)
        } else {
            val profileDummyImageUrl =
                "https://firebasestorage.googleapis.com/v0/b/campusrecruitmentsystem-0.appspot.com/o/images%2FdummyProfileImage%2Fuserdefaultprofileimage.png?alt=media&token=a27bf46e-d6fe-417d-aaed-1a5e74b8d54a"
            profileSeletPhotoBtn.alpha = 0f
            Glide.with(activity!!).applyDefaultRequestOptions(RequestOptions().apply() {
                placeholder(CircularProgressDrawable(activity!!).apply {
                    strokeWidth = 2f
                    centerRadius = 50f
                    start()
                })
            }).load(profileDummyImageUrl).into(profileImageImageView)
        }
    }

    private fun displayCompanyData() {
        appPrefCompany = AppPref(activity!!).getCompany()!!
        if (!appPrefCompany.companyName.isNullOrEmpty()) {
            profileUserNameText.text = "${appPrefCompany.companyName}"
            profileUserNameText.setTextColor(resources.getColor(R.color.colorPrimary))
        } else {
            profileUserNameText.setText(getString(R.string.user_name))
            profileUserNameText.setTextColor(resources.getColor(R.color.colorSecondaryText))
        }

        if (!appPrefCompany.companyLogoUrl.isNullOrEmpty()){
            profileSeletPhotoBtn.alpha = 0f
            Glide.with(activity!!).applyDefaultRequestOptions(RequestOptions().apply() {
                placeholder(CircularProgressDrawable(activity!!).apply {
                    strokeWidth = 2f
                    centerRadius = 50f
                    start()
                })
            }).load(appPrefCompany.companyLogoUrl).into(profileImageImageView)
        } else{
            val profileDummyImageUrl = "https://firebasestorage.googleapis.com/v0/b/campusrecruitmentsystem-0.appspot.com/o/images%2FdummyProfileImage%2Fcompanydummyprofileicon.png?alt=media&token=43cf909d-7efe-4bb6-9e2a-fbabca75e683"
            profileSeletPhotoBtn.alpha = 0f
            Glide.with(activity!!).applyDefaultRequestOptions(RequestOptions().apply() {
                placeholder(CircularProgressDrawable(activity!!).apply {
                    strokeWidth = 2f
                    centerRadius = 50f
                    start()
                })
            }).load(profileDummyImageUrl).into(profileImageImageView)
        }
    }
}
