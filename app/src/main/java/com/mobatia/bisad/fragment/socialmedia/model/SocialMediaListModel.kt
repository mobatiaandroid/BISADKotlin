package com.mobatia.bisad.fragment.socialmedia.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.report_absence.model.AbsenceResponseArray

data class SocialMediaListModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArray: SocialMediaResponseArray

)



//SocialMediaListModel