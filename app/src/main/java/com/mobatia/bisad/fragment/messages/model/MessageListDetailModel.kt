package com.mobatia.bisad.fragment.messages.model

import com.google.gson.annotations.SerializedName

data class MessageListDetailModel (
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("alert_type") val alert_type: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
)
