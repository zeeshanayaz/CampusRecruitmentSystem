package com.zeeshan.campusrecruitmentsystem.controller.PMB

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.model.Student
import kotlinx.android.synthetic.main.activity_pmb.*

class PMBActivity : AppCompatActivity() {
    companion object {
        var user: Student? = null
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pmb)
        setSupportActionBar(pmbToolbar)
        getSupportActionBar()!!.setDisplayHomeAsUpEnabled(true)
        getSupportActionBar()!!.setDisplayShowHomeEnabled(true)
        supportActionBar!!.setTitle("PMB")
    }
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
