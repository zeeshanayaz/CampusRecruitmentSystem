package com.zeeshan.campusrecruitmentsystem.model

class Student(
    var studentId : String = "",
    var firstName : String = "",
    var lastName : String = "",
    var emailAddress : String = "",
    var contactNumber : Long = 0,
    var address : String = "",
    var country : String = "",
    var city : String = "",
    var summarry : String = "",
    var experience : ArrayList<String>? = null,
    var projectsDone : ArrayList<String>? = null,
    var profileImageUrl: String? = null
) {
}