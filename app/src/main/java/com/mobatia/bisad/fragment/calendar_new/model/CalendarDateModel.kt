package com.mobatia.bisad.fragment.calendar_new.model

import com.google.gson.annotations.SerializedName

class CalendarDateModel {
    @SerializedName("startDate") var startDate: String=""
    @SerializedName("endDate") var endDate: String=""
    @SerializedName("detailList") var detailList= ArrayList<CalendarDetailModel>()
}