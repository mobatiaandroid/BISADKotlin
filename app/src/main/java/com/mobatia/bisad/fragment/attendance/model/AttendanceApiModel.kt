package com.mobatia.bisad.fragment.attendance.model

import com.google.gson.annotations.SerializedName

data class AttendanceApiModel (

    @SerializedName("student_id") val student_id: String)