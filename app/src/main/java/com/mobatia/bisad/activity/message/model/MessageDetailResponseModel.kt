package com.mobatia.bisad.activity.message.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterDetailResponseModel

data class MessageDetailResponseModel (
    @SerializedName("notification") val notificationArray:MessageDetailNotificationModel
)