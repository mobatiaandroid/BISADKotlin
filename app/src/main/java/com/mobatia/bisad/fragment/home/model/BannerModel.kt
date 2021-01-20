package com.mobatia.bisad.fragment.home.model

import com.google.gson.annotations.SerializedName

data class BannerModel (

    @SerializedName("app_version") val app_version: String,
    @SerializedName("devicetype") val devicetype: Int)