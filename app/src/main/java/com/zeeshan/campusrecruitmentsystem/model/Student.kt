package com.zeeshan.campusrecruitmentsystem.model

class Student(
    var studentId : String = "",
    var firstName : String = "",
    var lastName : String = "",
    var emailAddress : String = "",
    var contactNumber : Int = 0,
    var summarry : String = "",
    var experience : ArrayList<String>,
    var projectsDone : ArrayList<String>
) {
}