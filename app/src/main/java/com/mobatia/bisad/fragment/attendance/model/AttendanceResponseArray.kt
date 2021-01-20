package com.mobatia.bisad.fragment.attendance.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.calendar.model.Cal
import com.mobatia.bisad.fragment.messages.model.MessageListDetailModel

class AttendanceResponseArray (
    @SerializedName("present") val present: Int,
    @SerializedName("late") val late: Int,
    @SerializedName("total_days") val total_days: Int,
     @SerializedName("attandance_details") val attandance_details: List<AttendanceListDetailModel>
//    @SerializedName("attandance_details") val attandance_details: AttendanceListDetailModel

)

