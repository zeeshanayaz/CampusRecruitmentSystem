package com.zeeshan.campusrecruitmentsystem.controller.dashboard

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.ContentValues
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.view.*
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.controller.dashboard.company.PostJobFragment
import com.zeeshan.campusrecruitmentsystem.controller.profile.ProfileActivity
import com.zeeshan.campusrecruitmentsystem.controller.splashScreen.SplashScreenActivity
import com.zeeshan.campusrecruitmentsystem.model.Company
import com.zeeshan.campusrecruitmentsystem.model.Student
import com.zeeshan.campusrecruitmentsystem.model.User
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.app_bar_dashboard.*
import kotlinx.android.synthetic.main.create_company_profile_dialog.view.*
import kotlinx.android.synthetic.main.create_student_profile_dialog.view.*


class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appPrefUser: User      //User from App Preference
    private var appPrefCompany: Company? = null      //Company from App Preference
    private var appPrefStudent: Student? = null      //Company from App Preference
    private lateinit var dbReference: FirebaseFirestore
    private lateinit var progress: ProgressDialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(dashboardToolbar)

        appPrefUser = AppPref(this).getUser()!!
        dbReference = FirebaseFirestore.getInstance()
        progress = ProgressDialog(this@DashboardActivity)

        when (appPrefUser.userAccountType) {
            "Student" -> {
                appPrefStudent = AppPref(this).getStudent()
                if (appPrefStudent == null) {
                    Toast.makeText(this, "Student Not Created", Toast.LENGTH_SHORT).show()
//                    startFragment()
                    showCreateStudentProfilePopup()
                } else {
                    startFragment()
                    Toast.makeText(
                        this,
                        "Wellcome ${appPrefStudent!!.firstName} ${appPrefStudent!!.lastName}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            "Company" -> {
                appPrefCompany = AppPref(this).getCompany()
                if (appPrefCompany == null) {
                    Toast.makeText(this, "Company Not Created", Toast.LENGTH_SHORT).show()
                    showCreateCompanyProfilePopup()

                } else {
                    startFragment()
                    Toast.makeText(this, "Wellcome ${appPrefCompany!!.companyName}", Toast.LENGTH_LONG).show()
                }

            }
            "Admin" -> {
                startFragment()
                Toast.makeText(this, "Wellcome Mr.${appPrefUser.userAccountType}", Toast.LENGTH_LONG).show()
            }
        }

        if (appPrefUser.userAccountType.equals("Student")) {
            nav_view.getMenu().setGroupVisible(R.id.student_nav_menu, true);
        } else if (appPrefUser.userAccountType.equals("Company")) {
            nav_view.getMenu().setGroupVisible(R.id.company_nav_menu, true);
        } else {
            nav_view.getMenu().setGroupVisible(R.id.admin_nav_menu, true);
        }

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, dashboardToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()

        nav_view.setNavigationItemSelectedListener(this)
    }

    //      Create Student Profile for the first time
    private fun showCreateStudentProfilePopup() {


        val studentProfileCreateDialog =
            LayoutInflater.from(this@DashboardActivity).inflate(R.layout.create_student_profile_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this@DashboardActivity)
            .setCancelable(false)
            .setView(studentProfileCreateDialog)
            .show()

        studentProfileCreateDialog.dialogStdCountry.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.country)
        ) as SpinnerAdapter?

        studentProfileCreateDialog.dialogStdCity.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.cities_pakistan)
        ) as SpinnerAdapter?


        studentProfileCreateDialog.submitStudentProfile.setOnClickListener {
            if (checkStudentProfileDialogEmpty(studentProfileCreateDialog)) {
                var summarry: String = ""
                if (studentProfileCreateDialog.dialogStdSummarry.text.trim().toString() != "")
                    summarry = studentProfileCreateDialog.dialogStdSummarry.text.trim().toString()

                showProgress("PLease wait while we are creating your profile....")
                createStudentProfile(
                    appPrefUser,
                    dbReference,
                    studentProfileCreateDialog.dialogStdFirstName.text.trim().toString(),
                    studentProfileCreateDialog.dialogStdLastName.text.trim().toString(),
                    studentProfileCreateDialog.dialogStdContactNumber.text.trim().toString(),
                    studentProfileCreateDialog.dialogStdCountry.selectedItem.toString(),
                    studentProfileCreateDialog.dialogStdCity.selectedItem.toString(),
                    summarry,
                    dialogBuilder
                )
            } else {
                Toast.makeText(this, "All input fields are required.", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun createStudentProfile(
        user: User,
        dbReference: FirebaseFirestore,
        firstName: String,
        lastName: String,
        contactNumber: String,
        country: String,
        city: String,
        summarry: String,
        dialogBuilder: AlertDialog
    ) {
        val studentData = Student(
            user.userId,
            firstName,
            lastName,
            user.userEmail,
            contactNumber.toLong(),
            "",
            country,
            city,
            summarry,
            null,
            null,
            null
        )

        dbReference.collection(user.userAccountType).document(user.userId).set(studentData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "${user.userAccountType} successfully written!")
                AppPref(this@DashboardActivity).setStudent(studentData)

                appPrefStudent = AppPref(this).getStudent()
                dialogBuilder.dismiss()

                startFragment()
                Toast.makeText(
                    this,
                    "Wellcome Mr.${appPrefStudent!!.firstName} ${appPrefStudent!!.lastName}",
                    Toast.LENGTH_LONG
                ).show()
                progress.dismiss()
            }
            .addOnFailureListener {
                Log.w(ContentValues.TAG, "Error writing document", it)
                progress.dismiss()
            }

    }

    private fun checkStudentProfileDialogEmpty(studentProfile: View): Boolean {
        return studentProfile.dialogStdFirstName.text.trim().toString() != "" &&
                studentProfile.dialogStdLastName.text.trim().toString() != "" &&
                studentProfile.dialogStdContactNumber.text.trim().toString() != "" &&
                studentProfile.dialogStdCountry.selectedItemPosition != 0 &&
                studentProfile.dialogStdCity.selectedItemPosition != 0
    }

    //      Create Profile for the first time
    private fun showCreateCompanyProfilePopup() {

        val profileCreateDialog =
            LayoutInflater.from(this@DashboardActivity).inflate(R.layout.create_company_profile_dialog, null)
        val dialogBuilder = AlertDialog.Builder(this@DashboardActivity)
            .setCancelable(false)
            .setView(profileCreateDialog)
            .show()
        profileCreateDialog.dialogContinueToDashboardButton.setOnClickListener {

            if (
                dialogBuilderTextEmptyCheck(profileCreateDialog)
            ) {
                if (profileCreateDialog.dialogCompanyDescription.length() < 100) {
                    profileCreateDialog.dialogCompanyDescription.setError("Please provide company Description of atleast 100 letters.....")
                } else {
                    showProgress("PLease wait while we are creating your profile....")

                    createCompanyProfile(
                        appPrefUser,
                        dbReference,
                        profileCreateDialog.dialogCompanyName.text.trim().toString(),
                        profileCreateDialog.dialogCompanyHeadName.text.trim().toString(),
                        profileCreateDialog.dialogCompanyIndustryCategory.text.trim().toString(),
                        profileCreateDialog.dialogCompanyCountry.text.trim().toString(),
                        profileCreateDialog.dialogCompanyDescription.text.trim().toString(),
                        dialogBuilder
                    )
                }
            } else {
                Toast.makeText(this, "All input fields are required.", Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun showProgress(message: String) {
        progress.setMessage(message)
        progress.setCancelable(false)
        progress.show()
    }

    private fun createCompanyProfile(
        user: User,
        dbReference: FirebaseFirestore,
        name: String,
        headName: String,
        industryName: String,
        country: String,
        description: String,
        dialogBuilder: AlertDialog
    ) {
        val companyData = Company(
            "${user.userId}",
            "$name",
            "$headName",
            "$industryName",
            "",
            "",
            "$country",
            "",
            "$description",
            "${user.userEmail}",
            "",
            "",
            ""
        )
        dbReference.collection("${user.userAccountType}").document(user.userId).set(companyData)
            .addOnSuccessListener {
                Log.d(ContentValues.TAG, "${user.userAccountType} successfully written!")
                AppPref(this@DashboardActivity).setCompany(companyData)

                appPrefCompany = AppPref(this).getCompany()
                dialogBuilder.dismiss()

                startFragment()
                Toast.makeText(this, "Wellcome ${appPrefCompany!!.companyName}", Toast.LENGTH_LONG).show()
                progress.dismiss()
            }
            .addOnFailureListener {
                Log.w(ContentValues.TAG, "Error writing document", it)
                progress.dismiss()
            }
    }

    private fun dialogBuilderTextEmptyCheck(profileCreateDialog: View): Boolean {
        return profileCreateDialog.dialogCompanyName.text.trim().toString() != "" &&
                profileCreateDialog.dialogCompanyHeadName.text.trim().toString() != "" &&
                profileCreateDialog.dialogCompanyIndustryCategory.text.trim().toString() != "" &&
                profileCreateDialog.dialogCompanyCountry.text.trim().toString() != "" &&
                profileCreateDialog.dialogCompanyDescription.text.trim().toString() != ""
    }

    private fun startFragment() {
        supportActionBar!!.setTitle("Job List - CRS")
        supportFragmentManager.beginTransaction().add(
            R.id.dashboardContainer,
            JobListFragment()
        ).commit()
    }

    override fun onBackPressed() {
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.dashboard_menu, menu)
        if (appPrefUser.userAccountType.equals("Admin")) {
            menu!!.findItem(R.id.menu_profile).setVisible(false)
        }
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.menu_sign_out -> {
                showSignOutPopUp()
                return true
            }
            R.id.menu_profile -> {
                val intent = Intent(this, ProfileActivity::class.java).apply {
                    //                                    putExtra(EXTRA_MESSAGE, "LoginFragment()")
                }
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
//            Communicate Navigation
            R.id.nav_help -> {
                supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
                    // the fragment exists
                    if (it is HelpFragment) {
                        // The presented fragment is FooFragment type
                    } else {
                        supportActionBar!!.setTitle(R.string.help_amp_feedback)
                        changeDashboardFragment(HelpFragment())
                    }
                }
            }
            R.id.nav_share -> {

                shareApp()
            }
//            Students Navigation
//            R.id.nav_pmb_student -> {
//                CurrentStatus = "Student"
//                supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
//                    // the fragment exists
//                    if (it is PMBFragment) {
//                        Toast.makeText(this, "Transaction not Completed", Toast.LENGTH_SHORT).show()
//                        // The presented fragment is FooFragment type
//                    } else {
//                        supportActionBar!!.setTitle("PMB - CRS")
//                        changeDashboardFragment(PMBFragment())
//                    }
//                }
//            }
            R.id.nav_job_list_student -> {
                supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
                    // the fragment exists
                    if (it is JobListFragment) {
                        // The presented fragment is FooFragment type
                    } else {
                        supportActionBar!!.setTitle("Job List - CRS")
                        changeDashboardFragment(JobListFragment())
                    }
                }
            }
            R.id.nav_applied_job_student -> {
                supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
                    // the fragment exists
                    if (it is AppliedJobFragment) {
                        // The presented fragment is FooFragment type
                    } else {
                        supportActionBar!!.setTitle("Job Applied List - CRS")
                        changeDashboardFragment(AppliedJobFragment())
                    }
                }
            }
            R.id.nav_company_list_student -> {
                supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
                    // the fragment exists
                    if (it is CompanyListFragment) {
                        // The presented fragment is FooFragment type
                    } else {
                        supportActionBar!!.setTitle("Registered Companies - CRS")
                        changeDashboardFragment(CompanyListFragment())
                    }
                }
            }
//            Company Navigation
//            R.id.nav_pmb_company -> {
//                CurrentStatus = "Company"
//                supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
//                    // the fragment exists
//                    if (it is PMBFragment) {
//                        Toast.makeText(this, "Transaction not Completed", Toast.LENGTH_SHORT).show()
//                        // The presented fragment is FooFragment type
//                    } else {
//                        supportActionBar!!.setTitle("PMB - CRS")
//                        changeDashboardFragment(PMBFragment())
//                    }
//                }
//
//            }
            R.id.nav_post_job_company -> {
                supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
                    // the fragment exists
                    if (it is PostJobFragment) {
                        // The presented fragment is FooFragment type
                    } else {
                        supportActionBar!!.setTitle(R.string.post_job)
                        changeDashboardFragment(PostJobFragment())
                    }
                }
            }
            R.id.nav_job_list_company -> {
                supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
                    // the fragment exists
                    if (it is JobListFragment) {
                        // The presented fragment is FooFragment type
                    } else {
                        supportActionBar!!.setTitle("Job List - CRS")
                        changeDashboardFragment(JobListFragment())
                    }
                }
            }
            R.id.nav_student_list_company -> {
                supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
                    // the fragment exists
                    if (it is StudentListFragment) {
                        // The presented fragment is FooFragment type
                    } else {
                        supportActionBar!!.setTitle("Student List - CRS")
                        changeDashboardFragment(StudentListFragment())
                    }
                }
            }
//            Admin Navigation
            R.id.nav_student_list_admin -> {
                supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
                    // the fragment exists
                    if (it is StudentListFragment) {
                        // The presented fragment is FooFragment type
                    } else {
                        supportActionBar!!.setTitle("Student List - CRS")
                        changeDashboardFragment(StudentListFragment())
                    }
                }
            }
            R.id.nav_company_list_admin -> {
                supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
                    // the fragment exists
                    if (it is CompanyListFragment) {
                        // The presented fragment is FooFragment type
                    } else {
                        supportActionBar!!.setTitle("Registered Companies - CRS")
                        changeDashboardFragment(CompanyListFragment())
                    }
                }
            }
            R.id.nav_job_list_admin -> {
                supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
                    // the fragment exists
                    if (it is JobListFragment) {
                        // The presented fragment is FooFragment type
                    } else {
                        supportActionBar!!.setTitle("Job List - CRS")
                        changeDashboardFragment(JobListFragment())
                    }
                }
            }


        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

    private fun changeDashboardFragment(fragment: android.support.v4.app.Fragment) {
        supportFragmentManager.beginTransaction().replace(
            R.id.dashboardContainer,
            fragment
        ).commit()
    }


    //    Share App Intent
    private fun shareApp() {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, "Campus Recruitment App")
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Feel free to share this app with your friends."))
    }

    //    Signing Out PopUp
    private fun showSignOutPopUp() {
        val dialogBuilder = AlertDialog.Builder(this@DashboardActivity)
        val create: AlertDialog = dialogBuilder.create()

        dialogBuilder.setCancelable(false)

        dialogBuilder.setTitle("Signing Out!")
        dialogBuilder.setMessage("Do you want to Sign out!")

        dialogBuilder.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                val curUser = FirebaseAuth.getInstance()
                curUser.signOut()
                val user = User("", "", "")
//                val company: Company? = null
//                val company = Company("","","","","","","","","","","","","")
                AppPref(this@DashboardActivity).setUser(user)
                AppPref(this@DashboardActivity).deleteCompany()
                AppPref(this@DashboardActivity).deleteStudent()
                val intent = Intent(this@DashboardActivity, SplashScreenActivity::class.java)
                startActivity(intent)
                finish()
            }
        })
        dialogBuilder.setNegativeButton("No", object : DialogInterface.OnClickListener {
            override fun onClick(dialog: DialogInterface?, which: Int) {
                create.dismiss()
            }
        })
        dialogBuilder.create()
        dialogBuilder.show()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }

}
