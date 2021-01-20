package com.mobatia.bisad.fragment.student_information.model

import com.google.gson.annotations.SerializedName

data class StudentInfoResponseArray (
    @SerializedName("student_info") val studentInfo: List<StudentInfoDetail>

)



//StudentInfoResponseArray