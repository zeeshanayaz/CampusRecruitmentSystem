package com.zeeshan.campusrecruitmentsystem.utilities

import android.content.Context
import com.google.gson.Gson
import com.zeeshan.campusrecruitmentsystem.model.Company
import com.zeeshan.campusrecruitmentsystem.model.Student
import com.zeeshan.campusrecruitmentsystem.model.User

class AppPref(var context: Context) {
    //      User Data
    fun getUser(): User? {
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val userString = sharedPreferences.getString("user", null)
        if (userString != null) {
            val user = Gson().fromJson<User>(userString, User::class.java)
            return user
        } else {
            return null
        }
    }

    fun setUser(user: User?) {
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val edit = sharedPreferences.edit()
        edit.putString("user", Gson().toJson(user))
        edit.apply()
    }


    //    Company Data
    fun getCompany(): Company? {
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val userString = sharedPreferences.getString("company", null)
        if (userString != null) {
            val company = Gson().fromJson<Company>(userString, Company::class.java)
            return company
        } else {
            return null
        }
    }

    fun setCompany(company: Company?) {
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val edit = sharedPreferences.edit()
        edit.putString("company", Gson().toJson(company))
        edit.apply()
    }
    fun deleteCompany(){
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val edit = sharedPreferences.edit()
        edit.putString("company", null)
        edit.apply()
    }



    //    Student Data
    fun getStudent(): Student? {
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val userString = sharedPreferences.getString("student", null)
        if (userString != null) {
            val student = Gson().fromJson<Student>(userString, Student::class.java)
            return student
        } else {
            return null
        }
    }

    fun setStudent(student: Student) {
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val edit = sharedPreferences.edit()
        edit.putString("student", Gson().toJson(student))
        edit.apply()
    }
    fun deleteStudent(){
        val sharedPreferences = context.getSharedPreferences("App", 0)
        val edit = sharedPreferences.edit()
        edit.putString("student", null)
        edit.apply()
    }
}