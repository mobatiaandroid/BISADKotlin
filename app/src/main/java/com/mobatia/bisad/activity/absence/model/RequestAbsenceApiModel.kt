package com.mobatia.bisad.activity.absence.model

import com.google.gson.annotations.SerializedName

data class RequestAbsenceApiModel (

    @SerializedName("student_id") val student_id: String,
    @SerializedName("from_date") val from_date: String,
    @SerializedName("to_date") val to_date: String,
    @SerializedName("reason") val reason: String

)
