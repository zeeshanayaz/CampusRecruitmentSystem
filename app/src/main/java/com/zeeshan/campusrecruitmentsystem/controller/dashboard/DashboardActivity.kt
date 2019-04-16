package com.zeeshan.campusrecruitmentsystem.controller.dashboard

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.support.design.widget.NavigationView
import android.support.v4.view.GravityCompat
import android.support.v7.app.ActionBarDrawerToggle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.controller.dashboard.company.PostJobFragment
import com.zeeshan.campusrecruitmentsystem.controller.profile.ProfileActivity
import com.zeeshan.campusrecruitmentsystem.controller.splashScreen.SplashScreenActivity
import com.zeeshan.campusrecruitmentsystem.model.Company
import com.zeeshan.campusrecruitmentsystem.model.User
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.app_bar_dashboard.*

class DashboardActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appPrefUser: User      //User from App Preference
    private lateinit var appPrefCompany: Company      //User from App Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(dashboardToolbar)

        appPrefUser = AppPref(this).getUser()!!
        appPrefCompany = AppPref(this).getCompany()!!

        startFragment()


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

    private fun startFragment() {
        supportFragmentManager.beginTransaction().add(
            R.id.dashboardContainer,
            HelpFragment()
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
                Toast.makeText(this, "Profile", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, ProfileActivity::class.java).apply {
                    //                                    putExtra(EXTRA_MESSAGE, "LoginFragment()")
                }
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

//    fun Fragment.setVisibilityChangeListener(clazz: Class<out Fragment>, listener: (Boolean) -> Unit) {
//        fragmentManager?.run {
//            addOnBackStackChangedListener {
//                val topFragmentTag = getBackStackEntryAt(backStackEntryCount - 1).name
//                val topFragment = findFragmentByTag(topFragmentTag)
//                listener(topFragment != null && topFragment::class.java == clazz)
//            }
//        }
//    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
//            Communicate Navigation
            R.id.nav_help -> {
                supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
                    // the fragment exists
                    if (it is HelpFragment) {
                        Toast.makeText(this, "Transaction not Completed", Toast.LENGTH_SHORT).show()
                        // The presented fragment is FooFragment type
                    } else {
                        changeDashboardFragment(HelpFragment())
                    }
                }
            }
            R.id.nav_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
                shareApp()
            }
//            Students Navigation
            R.id.nav_pmb_student -> {
                Toast.makeText(this, "PMB Students", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_job_list_student -> {
                Toast.makeText(this, "nav_job_list_student", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_applied_job_student -> {
                Toast.makeText(this, "nav_applied_job_student", Toast.LENGTH_SHORT).show()
            }
//            Company Navigation
            R.id.nav_pmb_company -> {
                Toast.makeText(this, "nav_pmb_company", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_post_job_company -> {
                supportFragmentManager.findFragmentById(R.id.dashboardContainer)?.let {
                    // the fragment exists
                    if (it is PostJobFragment) {
                        Toast.makeText(this, "Transaction not Completed", Toast.LENGTH_SHORT).show()
                        // The presented fragment is FooFragment type
                    } else {
                        changeDashboardFragment(PostJobFragment())
                    }
                }
            }
            R.id.nav_job_list_company -> {
                Toast.makeText(this, "nav_job_list_company", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_student_list_company -> {
                Toast.makeText(this, "nav_student_list_company", Toast.LENGTH_SHORT).show()
            }
//            Admin Navigation
            R.id.nav_student_list_admin -> {
                Toast.makeText(this, "nav_student_list_admin", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_company_list_admin -> {
                Toast.makeText(this, "nav_company_list_admin", Toast.LENGTH_SHORT).show()
            }
            R.id.nav_job_list_admin -> {
                Toast.makeText(this, "nav_job_list_admin", Toast.LENGTH_SHORT).show()
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
        Toast.makeText(this, "$fragment", Toast.LENGTH_SHORT).show()
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
                AppPref(this@DashboardActivity).setUser(user)
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
}
