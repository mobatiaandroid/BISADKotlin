package com.mobatia.bisad.fragment.report_absence.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.student_information.model.StudentList

data class AbsenceResponseArray (
    @SerializedName("request") val requestList: List<AbsenceRequestListDetailModel>

)
