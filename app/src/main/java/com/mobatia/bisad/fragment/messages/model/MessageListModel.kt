package com.mobatia.bisad.fragment.messages.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.student_information.model.ResponseArray

data class MessageListModel (
    @SerializedName("status") val status: Int,
    @SerializedName("responseArray") val responseArray: MessageResponseArrayModel
)
