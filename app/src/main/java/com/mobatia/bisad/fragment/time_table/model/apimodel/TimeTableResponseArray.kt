package com.mobatia.bisad.fragment.time_table.model.apimodel

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.termdates.model.TermDatesListDetailModel
import com.mobatia.bisad.fragment.time_table.model.apimodel.RangeApiModel

class TimeTableResponseArray (
    @SerializedName("range") val range: RangeApiModel,
    @SerializedName("field1") val field1List: List<FieldApiListModel>,
    @SerializedName("Timetable") val timeTableList: List<TimeTableApiListModel>
)