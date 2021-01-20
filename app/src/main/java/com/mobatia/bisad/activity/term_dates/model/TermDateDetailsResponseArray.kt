package com.mobatia.bisad.activity.term_dates.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.termdates.model.TermDatesListDetailModel

data class TermDateDetailsResponseArray (
    @SerializedName("termdates") val termdates: TermDatesDetailArrayList
)

