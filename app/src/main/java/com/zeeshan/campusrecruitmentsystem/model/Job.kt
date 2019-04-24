package com.zeeshan.campusrecruitmentsystem.model

data class Job(
    var companyId: String = "",
    var jobId: String = "",
    var jobTitle: String = "",
    var jobDescription: String = "",
    var jobSkills: String = "",
    var jobCareerLevel: String = "",
    var jobApplicantExperience: String = "",
    var jobPositions: Int = 0,
    var jobLocation: String = "",
    var jobEstimatedSalary: Int = 0,
    var jobType: String = "",
    var jobShift: String = "",
    var jobCreatedDate: Long = 0,
    var jobStatus: String = "",
    var jobApplicant: ArrayList<String>? = null
) {
    override fun toString(): String {
        return "Job Title = '$jobTitle'"
    }

    override fun equals(other: Any?): Boolean {

        if (this === other) return true

        if (javaClass != other?.javaClass) return false

        other as Job

        if (jobId != other.jobId) return false

        return true
    }
}