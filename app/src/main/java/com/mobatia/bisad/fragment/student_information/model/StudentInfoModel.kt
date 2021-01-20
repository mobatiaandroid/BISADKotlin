package com.mobatia.bisad.fragment.student_information.model

import com.google.gson.annotations.SerializedName

data class StudentInfoModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArray: StudentInfoResponseArray)