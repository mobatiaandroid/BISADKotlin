package com.mobatia.bisad.fragment.settings.model

import com.google.gson.annotations.SerializedName

class TriggerDataModel {
    @SerializedName("categoryName")
    var categoryName: String = ""
    @SerializedName("checkedCategory")
    var checkedCategory: Boolean = false
}



