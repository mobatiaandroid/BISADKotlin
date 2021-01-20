package com.mobatia.bisad.fragment.curriculum.model

import com.google.gson.annotations.SerializedName

class CuriculumResponseArray (
    @SerializedName("id") val id: String,
    @SerializedName("title") val title: String,
    @SerializedName("file") val file: String,
    @SerializedName("view_curriculm_guide") val view_curriculm_guide: String,
    @SerializedName("updated_at") val updated_at: String
)