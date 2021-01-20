package com.mobatia.bisad.fragment.calendar.model

import com.google.gson.annotations.SerializedName

data class CalendarListDetailModel (
    @SerializedName("todo_count") val id: Int,
    @SerializedName("event_count") val title: Int,
    @SerializedName("cal") val cal: Cal
//    @SerializedName("cal") val cal: List<Cal>


)
