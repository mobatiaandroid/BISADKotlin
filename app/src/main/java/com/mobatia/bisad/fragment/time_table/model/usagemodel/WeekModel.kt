package com.mobatia.bisad.fragment.time_table.model.usagemodel

import com.google.gson.annotations.SerializedName

data class WeekModel (
    @SerializedName("weekName") val weekName: String,
    @SerializedName("positionSelected") var positionSelected: Int
)