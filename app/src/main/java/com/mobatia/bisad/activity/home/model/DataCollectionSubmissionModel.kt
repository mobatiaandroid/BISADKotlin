package com.mobatia.bisad.activity.home.model

import com.google.gson.annotations.SerializedName

class DataCollectionSubmissionModel (
    @SerializedName("overall_status") val overall_status: Int,
    @SerializedName("data") val data: String,
    @SerializedName("trigger_type") val trigger_type: Int,
    @SerializedName("device_type") val device_type: String,
    @SerializedName("device_name") val device_name: String,
    @SerializedName("app_version") val app_version: String
)