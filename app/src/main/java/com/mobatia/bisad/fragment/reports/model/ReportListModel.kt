package com.mobatia.bisad.fragment.reports.model

import com.google.gson.annotations.SerializedName

class ReportListModel(

    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val data: List<ReportResponseArray>
)