package com.mobatia.bisad.fragment.home.model

import com.google.gson.annotations.SerializedName

class RelationShipListModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArray: RelationShipResponseArray
)