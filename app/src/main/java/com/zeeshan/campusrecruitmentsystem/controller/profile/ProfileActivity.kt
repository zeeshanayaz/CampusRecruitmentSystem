package com.zeeshan.campusrecruitmentsystem.controller.profile

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zeeshan.campusrecruitmentsystem.R

class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        supportFragmentManager
            .beginTransaction()
            .add(R.id.containerProfile, MyProfileFragment())
            .commit()
    }
}
