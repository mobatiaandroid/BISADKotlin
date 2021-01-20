package com.mobatia.bisad.fragment.home.model.datacollection

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.home.model.TilesResponseArray

class DataCollectionModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArray: DataCollectionResponseArray
)