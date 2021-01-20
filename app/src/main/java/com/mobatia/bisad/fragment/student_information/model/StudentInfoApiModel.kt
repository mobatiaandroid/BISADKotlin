package com.mobatia.bisad.fragment.student_information.model

import com.google.gson.annotations.SerializedName

data class StudentInfoApiModel (
    @SerializedName("student_id") val student_id: String)