package com.mobatia.bisad.fragment.termdates.model

import com.google.gson.annotations.SerializedName

data class TermDatesListModel (

    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArray: TermDateResponseArray

)
