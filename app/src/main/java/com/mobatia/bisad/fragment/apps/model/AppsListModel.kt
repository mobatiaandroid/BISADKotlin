package com.mobatia.bisad.fragment.apps.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.messages.model.MessageResponseArrayModel

data class AppsListModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val appsList: List<AppsListDetailModel>
)