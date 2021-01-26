package com.mobatia.bisad.fragment.calendar_new.model

import com.google.gson.annotations.SerializedName

class CalendarModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val calendarList: List<CalendarResponseArray>
)