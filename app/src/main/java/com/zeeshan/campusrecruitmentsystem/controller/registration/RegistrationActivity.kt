package com.zeeshan.campusrecruitmentsystem.controller.registration

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zeeshan.campusrecruitmentsystem.R

class RegistrationActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        startFragment()
    }

    private fun startFragment() {
        supportFragmentManager.beginTransaction().add(R.id.registrationContainer,
            StepsGuideFragment()
        ).commit()
    }
}
