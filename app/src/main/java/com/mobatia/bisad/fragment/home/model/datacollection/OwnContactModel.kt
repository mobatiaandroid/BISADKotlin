package com.mobatia.bisad.fragment.home.model.datacollection

import com.google.gson.annotations.SerializedName

class OwnContactModel {

    @SerializedName("id") var id: Int=0
    @SerializedName("user_id") var user_id: Int=0
    @SerializedName("title") var title:String=""
    @SerializedName("name") var name:String=""
    @SerializedName("last_name") var last_name:String=""
    @SerializedName("relationship") var relationship:String=""
    @SerializedName("email") var email:String=""
    @SerializedName("phone") var phone:String=""
    @SerializedName("code") var code:String=""
    @SerializedName("user_mobile") var user_mobile:String=""
    @SerializedName("address1") var address1:String=""
    @SerializedName("address2") var address2:String=""
    @SerializedName("address3") var address3:String=""
    @SerializedName("town") var town:String=""
    @SerializedName("state") var state:String=""
    @SerializedName("country") var country:String=""
    @SerializedName("pincode") var pincode:String=""
    @SerializedName("status") var status:Int=0
    @SerializedName("created_at") var created_at:String=""
    @SerializedName("updated_at") var updated_at:String=""
    @SerializedName("isUpdated") var isUpdated:Boolean=false
    @SerializedName("isConfirmed") var isConfirmed:Boolean=false

}
