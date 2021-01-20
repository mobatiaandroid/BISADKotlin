package com.mobatia.bisad.fragment.report_absence.model

import com.google.gson.annotations.SerializedName

data class AbsenceRequestListDetailModel (

    @SerializedName("id") val id: String,
    @SerializedName("from_date") val from_date: String,
    @SerializedName("to_date") val to_date: String,
    @SerializedName("reason") val reason: String

)
