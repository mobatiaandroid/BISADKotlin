package com.mobatia.bisad.fragment.apps.model

import com.google.gson.annotations.SerializedName

class AppsApiModel (

    @SerializedName("student_id") val student_id: String,
    @SerializedName("start") val start: Int,
    @SerializedName("limit") val limit: Int
)