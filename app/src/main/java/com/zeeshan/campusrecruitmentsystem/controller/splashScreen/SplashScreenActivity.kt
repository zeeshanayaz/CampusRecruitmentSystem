package com.zeeshan.campusrecruitmentsystem.controller.splashScreen

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.controller.dashboard.DashboardActivity
import com.zeeshan.campusrecruitmentsystem.controller.registration.RegistrationActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        val auth: FirebaseAuth = FirebaseAuth.getInstance()
        if (auth.currentUser != null) {
            val intent = Intent(this@SplashScreenActivity, DashboardActivity::class.java).apply {
                addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            }
            startActivity(intent)

        }

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_splash_screen)
        } else if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_splash_screen_landscape)
        }

        splashLetStartBtn.setOnClickListener {
            startActivity(Intent(this@SplashScreenActivity, RegistrationActivity::class.java))
        }
    }
}
