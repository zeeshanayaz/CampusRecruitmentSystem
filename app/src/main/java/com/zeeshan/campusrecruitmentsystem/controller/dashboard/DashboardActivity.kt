package com.zeeshan.campusrecruitmentsystem.controller.dashboard

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.zeeshan.campusrecruitmentsystem.R

class DashboardActivity : AppCompatActivity() {

    companion object {
        var accountType : String = ""
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        Toast.makeText(this@DashboardActivity, "${DashboardActivity.accountType}", Toast.LENGTH_SHORT).show()
    }
}
