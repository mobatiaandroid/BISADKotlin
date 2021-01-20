package com.mobatia.bisad.fragment.home.model

import com.google.gson.annotations.SerializedName

class RelationShipResponseArray (
    @SerializedName("contact_types") val contactTypesList: List<RelationShipDetailModel>
)