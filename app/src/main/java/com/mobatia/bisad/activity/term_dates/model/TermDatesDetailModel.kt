package com.mobatia.bisad.activity.term_dates.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.termdates.model.TermDateResponseArray

data class TermDatesDetailModel (

    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArray: TermDatesDetailArrayList
)