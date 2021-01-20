package com.mobatia.bisad.fragment.home.model.datacollection

import com.google.gson.annotations.SerializedName

class OwnDetailsModel (
    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("name") val name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("relationship") val relationship: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("code") val code: String,
    @SerializedName("user_mobile") val user_mobile: String,
    @SerializedName("address1") val address1: String,
    @SerializedName("address2") val address2: String,
    @SerializedName("address3") val address3: String,
    @SerializedName("town") val town: String,
    @SerializedName("state") val state: String,
    @SerializedName("country") val country: String,
    @SerializedName("pincode") val pincode: String,
    @SerializedName("status") val status: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
)


/*"id": 832,
        "user_id": 50395,
        "title": "Mr. ",
        "name": "Jacob",
        "last_name": "Cooper",
        "relationship": "Emergency",
        "email": "jeenu.js@mobatia.com",
        "phone": "0501302302",
        "code": "",
        "user_mobile": "0501302302",
        "address1": "",
        "address2": "",
        "address3": "",
        "town": "",
        "state": "",
        "country": "",
        "pincode": "",
        "status": 0,
        "created_at": "2021-01-13 17:17:05",
        "updated_at": "2021-01-13 17:17:05"*/