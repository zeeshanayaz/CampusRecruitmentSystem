package com.zeeshan.campusrecruitmentsystem.splashScreen

import android.content.Intent
import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.registration.RegistrationActivity
import kotlinx.android.synthetic.main.activity_splash_screen.*

class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            setContentView(R.layout.activity_splash_screen)
        }
        else if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            setContentView(R.layout.activity_splash_screen_landscape)
        }

        splashLetStartBtn.setOnClickListener {
            startActivity(Intent(this@SplashScreenActivity, RegistrationActivity::class.java))
        }
    }
}
