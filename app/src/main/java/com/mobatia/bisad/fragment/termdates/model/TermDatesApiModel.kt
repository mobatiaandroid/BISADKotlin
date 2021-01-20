package com.mobatia.bisad.fragment.termdates.model

import com.google.gson.annotations.SerializedName

data class TermDatesApiModel (
    @SerializedName("start") val start: Int,
    @SerializedName("limit") val limit: Int
)
