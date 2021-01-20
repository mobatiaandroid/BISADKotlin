package com.mobatia.bisad.fragment.calendar.model

import com.google.gson.annotations.SerializedName

data class Cal (
    @SerializedName("VCALENDAR") val VCALENDAR: VCALENDAR,
//    @SerializedName("VCALENDAR") val VCALENDAR: List<VCALENDAR>,
    @SerializedName("VEVENT") val VEVENT: List<VEVENT>

)
