package com.mobatia.bisad.fragment.calendar_new.model

import com.google.gson.annotations.SerializedName

class CategoryModel {
    @SerializedName("categoryName")
    var categoryName: String = ""
    @SerializedName("checkedCategory")
    var checkedCategory: Boolean = false
    @SerializedName("color")
    var color: String = ""
}
