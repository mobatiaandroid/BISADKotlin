package com.mobatia.bisad.fragment.contact_us.model

import com.google.gson.annotations.SerializedName

 class ContactsListDetailModel(
    @SerializedName("name") val name: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("email") val email: String
    )
