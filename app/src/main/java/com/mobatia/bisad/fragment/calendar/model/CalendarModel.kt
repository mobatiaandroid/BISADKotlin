package com.mobatia.bisad.fragment.calendar.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.apps.model.AppsListDetailModel

class CalendarModel (
    @SerializedName("status") val status: Int,
   @SerializedName("responseArray") val calendarList: List<CalendarListResponse>
)