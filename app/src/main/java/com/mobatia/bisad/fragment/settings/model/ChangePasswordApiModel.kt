package com.mobatia.bisad.fragment.settings.model

import com.google.gson.annotations.SerializedName

data class ChangePasswordApiModel (
    @SerializedName("password") val password: String,
    @SerializedName("c_password") val c_password: String,
    @SerializedName("old_password") val old_password: String
)