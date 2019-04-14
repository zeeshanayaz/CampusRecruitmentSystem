package com.zeeshan.campusrecruitmentsystem.controller.registration


import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.controller.dashboard.DashboardActivity
import com.zeeshan.campusrecruitmentsystem.controller.splashScreen.SplashScreenActivity
import com.zeeshan.campusrecruitmentsystem.model.User
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import kotlinx.android.synthetic.main.fragment_log_out.*

class LogOutFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: FirebaseFirestore
    private lateinit var progress: ProgressDialog
    private lateinit var accountTypeStatus: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_out, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

//        Initializing Values
        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseFirestore.getInstance()
        progress = ProgressDialog(activity!!)


        logoutLogoutButton.setOnClickListener {
            if (!TextUtils.isEmpty(logoutTextEmailAddress.text.trim()) && !TextUtils.isEmpty(logoutTextPassword.text.trim())) {
                if (logoutAccountTypeRadioGroup.checkedRadioButtonId == -1) {
//                    No Radio Button are checked
                    Toast.makeText(activity!!, "Please Select Account Type...", Toast.LENGTH_SHORT).show()

                } else {

                    when (logoutAccountTypeRadioGroup.checkedRadioButtonId) {
                        logoutRadioAdmin.id -> accountTypeStatus = "Admin"
                        logoutRadioStudent.id -> accountTypeStatus = "Student"
                        logoutRadioCompany.id -> accountTypeStatus = "Company"
                    }

                    Snackbar.make(view, "Connecting to Server", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
                    progress.setTitle("Creating New Account")
                    progress.setMessage("Please wait, while we are creating new account for you...")
                    progress.setCancelable(false)
                    progress.show()

                    registerUser(
                        logoutTextEmailAddress.text.trim().toString(),
                        logoutTextPassword.text.trim().toString(),
                        accountTypeStatus
                    )
                }
            } else {
                logoutTextEmailAddress.setError("Error")
                logoutTextPassword.setError("Error")
                Snackbar.make(view, "All field are required.", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
            }

        }


        logoutLoginBtn.setOnClickListener {
            activity!!.supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.registrationContainer,
                    LogInFragment()
                )
                .commit()
        }
    }

//        Creating User in Firebase Authentication
    private fun registerUser(email: String, password: String, accountTypeStatus: String) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener {
            if (it.isSuccessful) {
                val authResult = it.result

                val userData = User("${authResult!!.user.uid}", "$email", "$accountTypeStatus")
                saveUsertoFirestore(userData)
                AppPref(activity!!).setUser(userData)

                progress.dismiss()
                DashboardActivity.accountType = accountTypeStatus
                navigateToMain()

            }
            else{
                progress.dismiss()
                Toast.makeText(activity, "Error: ${it.exception.toString()}", Toast.LENGTH_LONG).show()
            }
        }
    }

//    Navigate TO Mian Dashboard
    private fun navigateToMain() {
        Toast.makeText(activity, "Welcome to CRS", Toast.LENGTH_LONG).show()
        val intent = Intent(activity!!, DashboardActivity::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        startActivity(intent)
        RegistrationActivity().finish()
        SplashScreenActivity().finish()
    }

    private fun saveUsertoFirestore(userData: User) {
        dbReference.collection("Users").document(userData.userId).set(userData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "User successfully written!")
            }
            .addOnFailureListener {
                Log.w(ContentValues.TAG, "Error writing document", it)
            }
    }

}
