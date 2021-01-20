package com.mobatia.bisad.activity.communication.newsletter.model

import com.google.gson.annotations.SerializedName

data class NewLetterListDetailModel (
    @SerializedName("id") val id: String,
    @SerializedName("create_time") val create_time: String,
    @SerializedName("status") val status: String,
    @SerializedName("emails_sent") val emails_sent: String,
    @SerializedName("subject_line") val subject_line: String,
    @SerializedName("title") val title: String,
    @SerializedName("from_name") val from_name: String

)
