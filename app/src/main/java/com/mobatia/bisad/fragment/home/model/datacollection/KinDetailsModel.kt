package com.mobatia.bisad.fragment.home.model.datacollection

import com.google.gson.annotations.SerializedName

class KinDetailsModel (

    @SerializedName("id") val id: Int,
    @SerializedName("user_id") val user_id: Int,
    @SerializedName("kin_id") val kin_id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("name") val name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("relationship") val relationship: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("code") val code: String,
    @SerializedName("user_mobile") val user_mobile: String,
    @SerializedName("status") val status: Int,
    @SerializedName("request") val request: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
)

/*"id": 883,
        "user_id": 50395,
        "kin_id": 43779,
        "title": "Mr. ",
        "name": "Saif Hadher Khamis",
        "last_name": "Alameemi",
        "relationship": "Father",
        "email": "notapplicable@notapplicable.com",
        "phone": "",
        "code": "",
        "user_mobile": "",
        "status": 0,
        "request": 0,
        "created_at": "2021-01-13 17:17:05",
        "updated_at": "2021-01-13 17:17:05"*/