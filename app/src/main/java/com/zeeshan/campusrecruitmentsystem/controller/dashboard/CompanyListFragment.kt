package com.zeeshan.campusrecruitmentsystem.controller.dashboard


import android.content.ContentValues.TAG
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.zeeshan.campusrecruitmentsystem.R
import com.zeeshan.campusrecruitmentsystem.adapters.CompanyListAdapter
import com.zeeshan.campusrecruitmentsystem.model.Company
import com.zeeshan.campusrecruitmentsystem.model.User
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import kotlinx.android.synthetic.main.fragment_company_list.*

class CompanyListFragment : Fragment() {

    private lateinit var appPrefUser: User      //User from App Preference
    private lateinit var dbReference: FirebaseFirestore
    private var companyList = ArrayList<Company>()
    private lateinit var companyListAdapter: CompanyListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_company_list, container, false)

        appPrefUser = AppPref(activity!!).getUser()!!
        dbReference = FirebaseFirestore.getInstance()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.companyListRecycler)
        recyclerView.layoutManager = LinearLayoutManager(activity!!)

        companyListAdapter = CompanyListAdapter(activity!!, companyList, {
            //            OnClick
            Toast.makeText(activity!!, "Clicked ${it.companyName}", Toast.LENGTH_SHORT).show()
        }, {
            //            On Long Click
            Toast.makeText(activity!!, "Long Clicked ${it.companyName}", Toast.LENGTH_SHORT).show()
        })

        recyclerView.adapter = companyListAdapter

//        if (appPrefUser.userAccountType == "Student") retrieveAllCompaniesList()
//        if (appPrefUser.userAccountType == "Admin") retrieveAllCompaniesList()
        retrieveAllCompaniesList()
    }

    private fun retrieveAllCompaniesList() {
        dbReference.collection("Company")
            .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@EventListener
                }

                for (dc in querySnapshot!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            companyList.add(dc.document.toObject(Company::class.java))
                            println(companyList)
                            Log.d("CompanyListFragment", companyList.toString())

                            companyListAdapter.notifyDataSetChanged()
                            if (!companyList.isEmpty())
                                emptyListCheckText.visibility = View.INVISIBLE
                            else
                                emptyListCheckText.visibility = View.VISIBLE
                        }

                        DocumentChange.Type.MODIFIED -> {

                        }
                        DocumentChange.Type.REMOVED -> {

                        }
                    }
                }
            })
    }
}
