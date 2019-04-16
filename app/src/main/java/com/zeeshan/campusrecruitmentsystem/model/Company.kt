package com.zeeshan.campusrecruitmentsystem.model

data class Company(
    var companyId: String = "",
    var companyName: String = "",
    var companyHeadName : String = "",
    var companyIndustryCategiry : String = "",
    var companyOwnershipType : String = "",
    var companyAddress : String = "",
    var companyCountry : String = "",
    var companyCity : String = "",
    var companyDescription : String = "",
    var companyContactEmail : String = "",
    var companyContactNumber : String = "",
    var companyWebsite : String = "",
    var companyLogoUrl : String = ""
) {
    override fun toString(): String {
        return "Company(companyName='$companyName')"
    }
}