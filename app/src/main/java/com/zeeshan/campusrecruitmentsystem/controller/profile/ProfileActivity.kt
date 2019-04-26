package com.zeeshan.campusrecruitmentsystem.controller.profile

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.controller.profile.company.CreateCompanyProfileFragment
import com.zeeshan.campusrecruitmentsystem.model.Company
import com.zeeshan.campusrecruitmentsystem.model.User
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {
    private lateinit var appPrefUser: User      //User from App Preference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(profileToolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setTitle(R.string.temp_name)

        appPrefUser = AppPref(this).getUser()!!

        supportFragmentManager
            .beginTransaction()
            .add(R.id.containerProfile, MyProfileFragment())
            .commit()
    }

    // don't forget click listener for back button
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

            menuInflater.inflate(R.menu.profile_menu, menu)
            if (appPrefUser.userAccountType.equals("Student")) {
                menu!!.findItem(R.id.menu_download_cv).setVisible(true)
            }

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.menu_profile_update -> {
                Toast.makeText(this, "Update Profile", Toast.LENGTH_SHORT).show()
                return true
            }
            R.id.menu_download_cv -> {
                Toast.makeText(this, "Download CV", Toast.LENGTH_SHORT).show()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        if (currentFocus != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
        return super.dispatchTouchEvent(ev)
    }
}
