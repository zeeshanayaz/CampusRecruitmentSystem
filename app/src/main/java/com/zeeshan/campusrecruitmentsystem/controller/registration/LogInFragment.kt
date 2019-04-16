package com.zeeshan.campusrecruitmentsystem.controller.registration


import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.app.Fragment
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.controller.dashboard.DashboardActivity
import com.zeeshan.campusrecruitmentsystem.model.User
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var dbReference: FirebaseFirestore
    private lateinit var progress: ProgressDialog
    private lateinit var accountTypeStatus: String


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log_in, container, false)



        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //        Initializing Values
        auth = FirebaseAuth.getInstance()
        dbReference = FirebaseFirestore.getInstance()
        progress = ProgressDialog(activity!!)





        forgetPasswordLink.setOnClickListener {
            Snackbar.make(it, "Beta Version. Forget password not implemented...", Snackbar.LENGTH_SHORT).show()
        }

        loginLoginButton.setOnClickListener {
            if (!TextUtils.isEmpty(loginTextEmailAddress.text.trim()) && !TextUtils.isEmpty(loginTextPassword.text.trim())) {
                if (loginAccountTypeRadioGroup.checkedRadioButtonId == -1) {
//                    No Radio Button are checked
                    Toast.makeText(activity!!, "Please Select Account Type...", Toast.LENGTH_SHORT).show()

                } else {
                    when (loginAccountTypeRadioGroup.checkedRadioButtonId) {
                        loginRadioAdmin.id -> accountTypeStatus = "Admin"
                        loginRadioStudent.id -> accountTypeStatus = "Student"
                        loginRadioCompany.id -> accountTypeStatus = "Company"
                    }

                    Snackbar.make(view, "Connecting to Server", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
                    progress.setTitle("Creating New Account")
                    progress.setMessage("Please wait, while we are verifying your details...")
                    progress.setCancelable(false)
                    progress.show()

                    authenticateUser(
                        loginTextEmailAddress.text.trim().toString(),
                        loginTextPassword.text.trim().toString(),
                        accountTypeStatus
                    )
                }
            } else {
                loginTextEmailAddress.setError("Error")
                loginTextPassword.setError("Error")
                Snackbar.make(view, "All field are required.", Snackbar.LENGTH_SHORT).setAction("Action", null).show()
            }
        }

////        Facebook Login Button
//        facebookLoginButton.setOnClickListener {
//            Snackbar.make(it,"Beta Version. Facebook Login is not supported right now...",Snackbar.LENGTH_SHORT).show()
//        }

        loginCreateUserBtn.setOnClickListener {
            activity!!.supportFragmentManager
                .beginTransaction()
                .replace(
                    R.id.registrationContainer,
                    LogOutFragment()
                )
                .commit()
        }
    }

    //    Authenticate User in Firebase Authentication
    private fun authenticateUser(email: String, password: String, accountTypeStatus: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnSuccessListener {
                retrieveUserData(it.user.uid, dbReference, accountTypeStatus)
            }
            .addOnFailureListener {
                progress.dismiss()
                Toast.makeText(activity, "Error: ${it.toString()}", Toast.LENGTH_LONG).show()
            }
    }

    private fun retrieveUserData(uid: String, dbReference: FirebaseFirestore, accountTypeStatus: String) {
        dbReference.collection("Users").document(uid).get()
            .addOnSuccessListener {
                if (it.exists()) {
                    val userData: User = it.toObject(User::class.java)!!
                    if (userData.userAccountType == accountTypeStatus) {
                        AppPref(activity!!).setUser(userData)
                        progress.dismiss()
                        navigateToMain()
                    } else {
                        progress.dismiss()
                        auth.signOut()
                    }

                }
            }
    }

    //    Navigate TO Mian Dashboard
    private fun navigateToMain() {


        val registrationActivity = activity!! as RegistrationActivity

        Toast.makeText(activity, "Welcome to CRS", Toast.LENGTH_LONG).show()
        val intent = Intent(activity!!, DashboardActivity::class.java).apply {
            //            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        }
        registrationActivity.finish()
        startActivity(intent)
    }

}
