package com.mobatia.bisad.fragment.calendar_new.model

import com.google.gson.annotations.SerializedName

class CalendarDetailModel {
    @SerializedName("DTSTART") var DTSTART:String=""
    @SerializedName("DTEND") var DTEND:String=""
    @SerializedName("SUMMARY") var SUMMARY:String=""
    @SerializedName("DESCRIPTION") var DESCRIPTION:String=""
    @SerializedName("LOCATION") var LOCATION:String=""
    @SerializedName("type") var type:Int=0
    @SerializedName("color") var color:String=""
    @SerializedName("startTime") var startTime:String=""
    @SerializedName("endTime") var endTime:String=""
}