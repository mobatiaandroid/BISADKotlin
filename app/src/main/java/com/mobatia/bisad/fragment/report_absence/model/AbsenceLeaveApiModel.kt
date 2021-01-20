package com.mobatia.bisad.fragment.report_absence.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.student_information.model.StudentInfoDetail

data class AbsenceLeaveApiModel (
    @SerializedName("student_id") val student_id: String,
    @SerializedName("start") val start: Int,
    @SerializedName("limit") val limit: Int
)

