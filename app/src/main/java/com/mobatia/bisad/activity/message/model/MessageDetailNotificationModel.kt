package com.mobatia.bisad.activity.message.model

import com.google.gson.annotations.SerializedName

data class MessageDetailNotificationModel (
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String,
    @SerializedName("url") val url: String,
    @SerializedName("alert_type") val alert_type: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
)