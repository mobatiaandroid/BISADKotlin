package com.mobatia.bisad.fragment.calendar.model

import com.google.gson.annotations.SerializedName

class CalendarListResponse (
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("color") val color: String)