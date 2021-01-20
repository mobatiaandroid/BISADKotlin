package com.mobatia.bisad.fragment.time_table.model.usagemodel

import com.google.gson.annotations.SerializedName

class DayModel
{
    @SerializedName("id") var id: Int=0
    @SerializedName("period_id") var period_id: Int=0
    @SerializedName("subject_name") var subject_name: String=""
    @SerializedName("staff") var staff: String=""
    @SerializedName("student_id") var student_id: Int=0
    @SerializedName("day") var day: String=""
    @SerializedName("sortname") var sortname: String=""
    @SerializedName("starttime") var starttime: String=""
    @SerializedName("endtime") var endtime: String=""


}
