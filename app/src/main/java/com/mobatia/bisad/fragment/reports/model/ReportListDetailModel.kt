package com.mobatia.bisad.fragment.reports.model

import com.google.gson.annotations.SerializedName

class ReportListDetailModel(

    @SerializedName("report_cycle") val report_cycle: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("file") val file: String,
    @SerializedName("viewreport") val viewreport: String
)