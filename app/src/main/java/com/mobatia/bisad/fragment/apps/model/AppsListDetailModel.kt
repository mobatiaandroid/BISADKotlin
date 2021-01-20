package com.mobatia.bisad.fragment.apps.model

import com.google.gson.annotations.SerializedName

data class AppsListDetailModel (
    @SerializedName("name") val name: String,
    @SerializedName("link") val link: String,
    @SerializedName("created_at") val created_at: String
)