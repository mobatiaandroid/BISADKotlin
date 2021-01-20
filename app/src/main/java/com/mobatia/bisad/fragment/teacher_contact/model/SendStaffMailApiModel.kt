package com.mobatia.bisad.fragment.teacher_contact.model

import com.google.gson.annotations.SerializedName

data class SendStaffMailApiModel (
    @SerializedName("student_id") val student_id: String,
    @SerializedName("staff_email") val staff_email: String,
    @SerializedName("title") val title: String,
    @SerializedName("message") val message: String
)
