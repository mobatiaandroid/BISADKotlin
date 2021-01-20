package com.mobatia.bisad.fragment.teacher_contact.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.student_information.model.StudentInfoDetail

data class StaffResponseArray (
    @SerializedName("staff_list") val staff_info: List<StaffInfoDetail>
)
