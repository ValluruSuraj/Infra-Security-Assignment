package com.suraj.employee

import java.io.Serializable

data class Employee(
    var id: Long = -1,
    var name: String,
    var dob: String,
    var designation: String,
    var yoe: Int,
    var address: String
) : Serializable
