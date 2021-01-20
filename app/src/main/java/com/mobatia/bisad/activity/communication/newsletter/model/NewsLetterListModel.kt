package com.mobatia.bisad.activity.communication.newsletter.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.messages.model.MessageResponseArrayModel

data class NewsLetterListModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArray: NewsLetterResponseModel
)