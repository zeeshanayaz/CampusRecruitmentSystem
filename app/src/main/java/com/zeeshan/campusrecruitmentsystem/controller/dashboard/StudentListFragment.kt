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
import com.zeeshan.campusrecruitmentsystem.adapters.StudentListAdapter
import com.zeeshan.campusrecruitmentsystem.model.Student

class StudentListFragment : Fragment() {

//    private lateinit var appPrefUser: User      //User from App Preference
    private lateinit var dbReference: FirebaseFirestore
    private var studentList = ArrayList<Student>()
    private lateinit var studentListAdapter: StudentListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_student_list, container, false)

//        appPrefUser = AppPref(activity!!).getUser()!!
        dbReference = FirebaseFirestore.getInstance()

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView = view.findViewById<RecyclerView>(R.id.studentListRecycler)
        recyclerView.layoutManager = LinearLayoutManager(activity!!)

        studentListAdapter = StudentListAdapter(activity!!, studentList, dbReference, {
            //            OnClick
            Toast.makeText(activity!!, "Clicked ${it.firstName} ${it.lastName}", Toast.LENGTH_SHORT).show()
        }, {
            //            On Long Click
            Toast.makeText(activity!!, "Long Clicked ${it.firstName} ${it.lastName}", Toast.LENGTH_SHORT).show()
        })

        recyclerView.adapter = studentListAdapter

        retrieveStudentList()

    }

    private fun retrieveStudentList() {
        dbReference.collection("Student")
            .addSnapshotListener(EventListener<QuerySnapshot> { querySnapshot, e ->
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e)
                    return@EventListener
                }

                for (dc in querySnapshot!!.documentChanges) {
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            studentList.add(dc.document.toObject(Student::class.java))
                            println(studentList)
                            Log.d("StudentListFragment", studentList.toString())

                            studentListAdapter.notifyDataSetChanged()
//                            checkEmptyList()
                        }

                        DocumentChange.Type.MODIFIED -> {
                            if (dc.document != null) {
                                val updatedStudent = dc.document.toObject(Student::class.java)

                                studentList.forEachIndexed { position, studentObj ->
                                    if (studentObj.equals(updatedStudent)) {
                                        println("MATCHED")
                                        studentList[position] = updatedStudent
                                        studentListAdapter.notifyDataSetChanged()
                                    } else {
                                        println("NOT MATCHED")
                                    }
                                }
                            }

                        }
                        DocumentChange.Type.REMOVED -> {
                            if (dc.document != null) {
                                val removedStudent = dc.document.toObject(Student::class.java)
                                var index: Int = -1

                                studentList.forEachIndexed { position, studentObj ->
                                    if (studentObj.equals(removedStudent)) {
                                        println("MATCHED")
                                        index = position
                                    } else {
                                        println("NOT MATCHED")
                                    }
                                }
                                studentList.removeAt(index)
                                studentListAdapter.notifyDataSetChanged()
//                                checkEmptyList()
                            }

                        }
                    }
                }
            })
    }

}
