package com.mobatia.bisad.fragment.termdates.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.report_absence.model.AbsenceRequestListDetailModel

data class TermDateResponseArray (
    @SerializedName("termdates") val termDatesList: List<TermDatesListDetailModel>
)
