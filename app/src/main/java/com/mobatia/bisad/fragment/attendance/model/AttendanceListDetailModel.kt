package com.mobatia.bisad.fragment.attendance.model

import com.google.gson.annotations.SerializedName

class AttendanceListDetailModel (
    @SerializedName("id") val id: Int,
    @SerializedName("date") val date: String,
    @SerializedName("present") val present: String,
    @SerializedName("late") val late: Int


    )



