package com.mobatia.bisad.activity.term_dates.model

import com.google.gson.annotations.SerializedName

data class TermDatesDetailArrayList (
    @SerializedName("description") val description: String,
    @SerializedName("image") val image: String,
    @SerializedName("link") val link: String,
    @SerializedName("created_at") val created_at: String
)