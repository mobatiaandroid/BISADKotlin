package com.mobatia.bisad.activity.communication.newsletter.model

import com.google.gson.annotations.SerializedName

data class NewsLetterListAPiModel (
    @SerializedName("start") val start: Int,
    @SerializedName("limit") val limit: Int
)