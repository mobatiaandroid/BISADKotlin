package com.mobatia.bisad.fragment.student_information.model

import com.google.gson.annotations.SerializedName

data class StudentList (
    @SerializedName("id") val id: String,
    @SerializedName("unique_id") val unique_id: String,
    @SerializedName("name") val name: String,
    @SerializedName("class") val studentClass: String,
    @SerializedName("section") val section: String,
    @SerializedName("house") val house: String,
    @SerializedName("photo") val photo: String


)