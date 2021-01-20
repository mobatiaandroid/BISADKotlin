package com.mobatia.bisad.fragment.contact_us.model

import com.google.gson.annotations.SerializedName


class ContactusResponseArray (
    @SerializedName("description") val description: String,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String,
    @SerializedName("contacts") val contacts: List<ContactsListDetailModel>

)

