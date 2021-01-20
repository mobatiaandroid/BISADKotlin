package com.mobatia.bisad.activity.settings.termsofservice.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.messages.model.MessageResponseArrayModel

data class TermsOfServiceModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArray: TermsResponseArray
)
