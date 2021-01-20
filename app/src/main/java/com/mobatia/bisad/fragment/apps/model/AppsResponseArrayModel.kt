package com.mobatia.bisad.fragment.apps.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.messages.model.MessageListDetailModel

data class AppsResponseArrayModel (
    @SerializedName("apps") val appsList: List<AppsListDetailModel>
)