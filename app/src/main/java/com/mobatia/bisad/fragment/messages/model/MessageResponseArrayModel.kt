package com.mobatia.bisad.fragment.messages.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.student_information.model.StudentList

data class MessageResponseArrayModel (
    @SerializedName("notifications") val notificationList: List<MessageListDetailModel>
)
