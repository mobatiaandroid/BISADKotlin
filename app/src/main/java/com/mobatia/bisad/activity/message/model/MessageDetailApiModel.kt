package com.mobatia.bisad.activity.message.model

import com.google.gson.annotations.SerializedName

data class MessageDetailApiModel (
    @SerializedName("notification_id") val notification_id: String
)