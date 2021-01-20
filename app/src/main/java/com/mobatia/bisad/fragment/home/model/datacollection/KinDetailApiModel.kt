package com.mobatia.bisad.fragment.home.model.datacollection

import com.google.gson.annotations.SerializedName

class KinDetailApiModel{
    @SerializedName("id") var id:Int=0
    @SerializedName("user_id") var user_id: Int=0
    @SerializedName("kin_id") var kin_id: Int=0
    @SerializedName("title") var title: String=""
    @SerializedName("name") var name: String=""
    @SerializedName("last_name") var last_name: String=""
    @SerializedName("relationship") var relationship: String=""
    @SerializedName("email") var email: String=""
    @SerializedName("phone") var phone: String=""
    @SerializedName("code") var code: String=""
    @SerializedName("user_mobile") var user_mobile: String=""
    @SerializedName("status") var status: Int=0
    @SerializedName("request") var request: Int=0
    @SerializedName("created_at") var created_at: String=""
    @SerializedName("updated_at") var updated_at: String=""
    @SerializedName("NewData") var NewData: Boolean=false
    @SerializedName("Newdata") var Newdata: String=""
    @SerializedName("isConfirmed") var isConfirmed: Boolean=false
}