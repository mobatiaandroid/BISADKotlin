package com.mobatia.bisad.activity.communication.newsletter.model

import com.google.gson.annotations.SerializedName

data class NewsLetterDetailResponseModel (
    @SerializedName("html") val html: String
)