package com.mobatia.bisad.fragment.student_information.model

import com.google.gson.annotations.SerializedName

data class StudentInfoDetail (
    @SerializedName("title") val title: String,
    @SerializedName("value") val value: String
)





//StudentInfoDetail