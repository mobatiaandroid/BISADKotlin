package com.mobatia.bisad.fragment.teacher_contact.model

import com.google.gson.annotations.SerializedName

data class StaffInfoDetail (
    @SerializedName("id") val id: String,
    @SerializedName("name") val name: String,
    @SerializedName("email") val email: String,
    @SerializedName("staff_photo") val staff_photo: String
)
