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
import com.zeeshan.campusrecruitmentsystem.controller.profile.ProfileActivity
import com.zeeshan.campusrecruitmentsystem.controller.splashScreen.SplashScreenActivity
import com.zeeshan.campusrecruitmentsystem.model.User
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import kotlinx.android.synthetic.main.activity_dashboard.*
import kotlinx.android.synthetic.main.app_bar_dashboard.*

class DashboardActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener {


    private lateinit var appPrefUser: User      //User from App Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        setSupportActionBar(dashboardToolbar)

        val toggle = ActionBarDrawerToggle(
            this, drawer_layout, dashboardToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )
        drawer_layout.addDrawerListener(toggle)
        toggle.syncState()
        nav_view.setNavigationItemSelectedListener(this)


        appPrefUser = AppPref(this).getUser()!!
        Toast.makeText(this@DashboardActivity, "${appPrefUser.userEmail}", Toast.LENGTH_SHORT).show()
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

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        // Handle navigation view item clicks here.
        when (item.itemId) {
            R.id.nav_pmb -> {
                Toast.makeText(this, "Open PMB Nav", Toast.LENGTH_SHORT).show()
            }

            R.id.nav_share -> {
                Toast.makeText(this, "Share", Toast.LENGTH_SHORT).show()
                shareApp()
            }
        }

        drawer_layout.closeDrawer(GravityCompat.START)
        return true
    }

//    Share App Intent
    private fun shareApp() {
        val shareIntent: Intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, R.string.app_name)
            type = "text/plain"
        }
        startActivity(Intent.createChooser(shareIntent, "Feel free to share this app with your friends."))
//               var sharingIntent : Intent = Intent().apply {
//                   action = Intent.ACTION_SEND
//               }
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
