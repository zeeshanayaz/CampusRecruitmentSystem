package com.zeeshan.campusrecruitmentsystem.registration


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.zeeshan.campusrecruitmentsystem.R
import kotlinx.android.synthetic.main.fragment_log_in.*

class LogInFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_log_in, container, false)



        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loginCreateUserBtn.setOnClickListener {
            activity!!.supportFragmentManager
                .beginTransaction()
                .replace(R.id.registrationContainer, LogOutFragment())
                .commit()
        }
    }
}
