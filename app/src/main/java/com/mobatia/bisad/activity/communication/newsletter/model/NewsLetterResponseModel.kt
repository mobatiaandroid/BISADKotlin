package com.mobatia.bisad.activity.communication.newsletter.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.messages.model.MessageListDetailModel

class NewsLetterResponseModel (
    @SerializedName("campaigns") val campaignsList: List<NewLetterListDetailModel>

)
