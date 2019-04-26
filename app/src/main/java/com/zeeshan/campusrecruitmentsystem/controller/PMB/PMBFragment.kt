package com.zeeshan.campusrecruitmentsystem.controller.PMB


import android.os.Bundle
import android.support.design.widget.BottomNavigationView
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.controller.dashboard.CompanyListFragment
import com.zeeshan.campusrecruitmentsystem.controller.dashboard.StudentListFragment
import com.zeeshan.campusrecruitmentsystem.model.User
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import com.zeeshan.campusrecruitmentsystem.utilities.CurrentStatus

class PMBFragment : Fragment() {

    private lateinit var appPrefUser: User                  //User from App Preference
    lateinit var bottomNavigation: BottomNavigationView
    override fun onCreateView(


        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_pmb, container, false)

        appPrefUser = AppPref(activity!!).getUser()!!
        bottomNavigation = view.findViewById(R.id.pmbNavigationView)
        bottomNavigation.inflateMenu(R.menu.pmb_navigation_menu)
        bottomNavigation.selectedItemId = R.id.navigation_pmb_chats
        bottomNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)


        startFragment()

        return view
    }

    private fun startFragment() {
        fragmentManager!!.beginTransaction().add(
            R.id.containerPMB,
            RecentChatFragment()
        ).commit()
    }

    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_pmb_chats -> {
                fragmentManager!!.findFragmentById(R.id.containerPMB)?.let {
                    // the fragment exists
                    if (it is RecentChatFragment) {
                        Toast.makeText(activity, "Transaction not Completed", Toast.LENGTH_SHORT).show()
                        // The presented fragment is FooFragment type
                    } else {
                        changePMBFragment(RecentChatFragment())
                    }
                }
//                Toast.makeText(activity!!, "Chats.", Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
//            R.id.navigation_pmb_company -> {
//                fragmentManager!!.findFragmentById(R.id.containerPMB)?.let {
//                    // the fragment exists
//                    if (it is CompanyListFragment) {
//                        Toast.makeText(activity, "Transaction not Completed", Toast.LENGTH_SHORT).show()
//                        // The presented fragment is FooFragment type
//                    } else {
//                        changePMBFragment(CompanyListFragment())
//                    }
//                }
////                Toast.makeText(activity!!, "Company", Toast.LENGTH_SHORT).show()
//                return@OnNavigationItemSelectedListener true
//            }
            R.id.navigation_pmb_student -> {
                fragmentManager!!.findFragmentById(R.id.containerPMB)?.let {
                    // the fragment exists
                    if (it is StudentListFragment) {
                        Toast.makeText(activity, "Transaction not Completed", Toast.LENGTH_SHORT).show()
                        // The presented fragment is FooFragment type
                    } else {
                        changePMBFragment(StudentListFragment())
                    }
                }
//                Toast.makeText(activity!!, "Student", Toast.LENGTH_SHORT).show()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    private fun changePMBFragment(fragment: Fragment) {
        fragmentManager!!.beginTransaction().replace(
            R.id.containerPMB,
            fragment
        ).commit()
        Toast.makeText(activity, "$fragment", Toast.LENGTH_SHORT).show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    override fun onStop() {
        super.onStop()
        CurrentStatus = ""
    }
}
