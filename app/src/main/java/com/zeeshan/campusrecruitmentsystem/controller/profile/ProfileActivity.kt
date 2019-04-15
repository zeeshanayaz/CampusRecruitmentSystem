package com.zeeshan.campusrecruitmentsystem.controller.profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.zeeshan.campusrecruitmentsystem.R
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        setSupportActionBar(profileToolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setTitle(R.string.temp_name)


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
}
