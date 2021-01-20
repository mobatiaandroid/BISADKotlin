package com.mobatia.bisad.fragment.termdates.model

import com.google.gson.annotations.SerializedName

data class TermDatesListDetailModel (

    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String
)
