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
import com.zeeshan.campusrecruitmentsystem.adapters.JobListAdapter
import com.zeeshan.campusrecruitmentsystem.model.Job
import com.zeeshan.campusrecruitmentsystem.model.User
import com.zeeshan.campusrecruitmentsystem.utilities.AppPref
import kotlinx.android.synthetic.main.fragment_job_list.*

class JobListFragment : Fragment() {

    private lateinit var appPrefUser: User      //User from App Preference
    private lateinit var dbReference: FirebaseFirestore
    private var jobList = ArrayList<Job>()
    private lateinit var jobListAdapter: JobListAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_job_list, container, false)

        appPrefUser = AppPref(activity!!).getUser()!!
        dbReference = FirebaseFirestore.getInstance()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


//        changeButtonVisibility(appPrefUser, view)


        val recyclerView = view.findViewById<RecyclerView>(R.id.jobListRecycler)
        recyclerView.layoutManager = LinearLayoutManager(activity!!)

        jobListAdapter = JobListAdapter(activity!!, jobList, dbReference, {
            //            OnClick
            Toast.makeText(activity!!, "Clicked ${it.jobTitle}", Toast.LENGTH_SHORT).show()
        }, {
            //            On Long Click
            Toast.makeText(activity!!, "Long Clicked ${it.jobTitle}", Toast.LENGTH_SHORT).show()
        })

        recyclerView.adapter = jobListAdapter

        if (appPrefUser.userAccountType == "Admin") retrieveAllJobList()
        if (appPrefUser.userAccountType == "Student") retrieveJobListForStudent()
        if (appPrefUser.userAccountType == "Company") retrieveJobListForCompany()
    }


    private fun retrieveJobListForStudent() {
        dbReference.collection("Job-Post").whereEqualTo("jobStatus", "Active")
            .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@EventListener
                }

                for (dc in querySnapshot!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            jobList.add(dc.document.toObject(Job::class.java))
                            println(jobList)
                            Log.d("UserListFragment", jobList.toString())

                            jobListAdapter.notifyDataSetChanged()
                            if (!jobList.isEmpty())
                                emptyListCheckText.visibility = View.INVISIBLE
                            else
                                emptyListCheckText.visibility = View.VISIBLE
                        }

                        DocumentChange.Type.MODIFIED -> {
                            if (dc.document != null) {
                                val updatedJob = dc.document.toObject(Job::class.java)

                                jobList.forEachIndexed { position, jobObj ->
                                    if (jobObj.equals(updatedJob)) {
                                        println("MATCHED")
                                        jobList[position] = updatedJob
                                        jobListAdapter.notifyDataSetChanged()
                                    } else {
                                        println("NOT MATCHED")
                                    }
                                }
                            }
                        }
                        DocumentChange.Type.REMOVED -> {
                            if (dc.document != null) {
                                val removedJob = dc.document.toObject(Job::class.java)
                                var index: Int = -1

                                jobList.forEachIndexed { position, jobObj ->
                                    if (jobObj.equals(removedJob)) {
                                        println("MATCHED")
                                        index = position
                                    } else {
                                        println("NOT MATCHED")
                                    }
                                }
                                jobList.removeAt(index)
                                jobListAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            })
    }


    private fun retrieveJobListForCompany() {
        dbReference.collection("Job-Post").whereEqualTo("companyId", "${appPrefUser.userId}")
            .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@EventListener
                }

                for (dc in querySnapshot!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            jobList.add(dc.document.toObject(Job::class.java))
                            println(jobList)
                            Log.d("UserListFragment", jobList.toString())

                            jobListAdapter.notifyDataSetChanged()
                            if (!jobList.isEmpty())
                                emptyListCheckText.visibility = View.INVISIBLE
                            else
                                emptyListCheckText.visibility = View.VISIBLE
                        }

                        DocumentChange.Type.MODIFIED -> {
                            if (dc.document != null) {
                                val updatedJob = dc.document.toObject(Job::class.java)

                                jobList.forEachIndexed { position, jobObj ->
                                    if (jobObj.equals(updatedJob)) {
                                        println("MATCHED")
                                        jobList[position] = updatedJob
                                        jobListAdapter.notifyDataSetChanged()
                                    } else {
                                        println("NOT MATCHED")
                                    }
                                }
                            }
                        }
                        DocumentChange.Type.REMOVED -> {
                            if (dc.document != null) {
                                val removedJob = dc.document.toObject(Job::class.java)
                                var index: Int = -1

                                jobList.forEachIndexed { position, jobObj ->
                                    if (jobObj.equals(removedJob)) {
                                        println("MATCHED")
                                        index = position
                                    } else {
                                        println("NOT MATCHED")
                                    }
                                }
                                jobList.removeAt(index)
                                jobListAdapter.notifyDataSetChanged()
                            }
                        }
                    }
                }
            })
    }

    private fun retrieveAllJobList() {
        dbReference.collection("Job-Post")
            .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@EventListener
                }

                for (dc in querySnapshot!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            jobList.add(dc.document.toObject(Job::class.java))
                            println(jobList)
                            Log.d("UserListFragment", jobList.toString())

                            jobListAdapter.notifyDataSetChanged()
                            if (!jobList.isEmpty())
                                emptyListCheckText.visibility = View.INVISIBLE
                            else
                                emptyListCheckText.visibility = View.VISIBLE
                        }

                        DocumentChange.Type.MODIFIED -> {
                            if (dc.document != null) {
                                val updatedJob = dc.document.toObject(Job::class.java)

                                jobList.forEachIndexed { position, jobObj ->
                                    if (jobObj.equals(updatedJob)) {
                                        println("MATCHED")
                                        jobList[position] = updatedJob
                                        jobListAdapter.notifyDataSetChanged()
                                    } else {
                                        println("NOT MATCHED")
                                    }
                                }
                            }

                        }
                        DocumentChange.Type.REMOVED -> {
                            if (dc.document != null) {
                                val removedJob = dc.document.toObject(Job::class.java)
                                var index: Int = -1

                                jobList.forEachIndexed { position, jobObj ->
                                    if (jobObj.equals(removedJob)) {
                                        println("MATCHED")
                                        index = position
                                    } else {
                                        println("NOT MATCHED")
                                    }
                                }
                                jobList.removeAt(index)
                                jobListAdapter.notifyDataSetChanged()
                            }

                        }
                    }
                }
            })
    }
}
