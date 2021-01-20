package com.mobatia.bisad.activity.communication.newsletter.model

import com.google.gson.annotations.SerializedName

data class NewsLetterDetailModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArray: NewsLetterDetailResponseModel
    )