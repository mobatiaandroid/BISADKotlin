package com.mobatia.bisad.fragment.home.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.calendar.model.CalendarListDetailModel
import com.mobatia.bisad.fragment.messages.model.MessageListDetailModel

class TilesResponseArray (
    @SerializedName("titles") val titlesList: List<TitlesArrayList>

)