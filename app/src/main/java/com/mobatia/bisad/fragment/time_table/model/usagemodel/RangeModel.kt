package com.mobatia.bisad.fragment.time_table.model.usagemodel

import com.google.gson.annotations.SerializedName

class RangeModel (
    @SerializedName("timeTableDayModel") val timeTableDayModel: List<DayModel>,
    @SerializedName("timeTableDayThursdayModel") val timeTableDayThursdayModel: List<DayModel>,
    @SerializedName("timeTableModel") val timeTableModel: List<TimeTableModel>,
    @SerializedName("periodModel") val periodModel: List<PeriodModel>
)