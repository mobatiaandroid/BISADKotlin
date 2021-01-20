package com.mobatia.bisad.fragment.report_absence.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.student_information.model.ResponseArray

data class AbsenceListModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArray: AbsenceResponseArray

)





//AbsenceListModel