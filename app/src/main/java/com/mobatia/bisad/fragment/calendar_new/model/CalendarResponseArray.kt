package com.mobatia.bisad.fragment.calendar_new.model

import com.google.gson.annotations.SerializedName

class CalendarResponseArray  (
    @SerializedName("title") val title: String,
    @SerializedName("color") val color: String,
    @SerializedName("details") val calendarDetail: CalendarCalModel

)