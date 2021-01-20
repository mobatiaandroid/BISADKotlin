package com.mobatia.bisad.activity.settings.termsofservice.model

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.messages.model.MessageListDetailModel

data class TermsResponseArray (
    //@SerializedName("terms_of_service") val termsOfServiceList: List<TermsOfServiceDetailModel>
    @SerializedName("terms_of_service") val termsofService: TermsOfServiceDetailModel
)