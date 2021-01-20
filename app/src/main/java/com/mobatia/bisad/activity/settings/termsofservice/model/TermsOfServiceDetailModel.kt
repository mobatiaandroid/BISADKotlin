package com.mobatia.bisad.activity.settings.termsofservice.model

import com.google.gson.annotations.SerializedName

data class TermsOfServiceDetailModel (
    @SerializedName("title") val title: String,
    @SerializedName("description") val description: String
)
