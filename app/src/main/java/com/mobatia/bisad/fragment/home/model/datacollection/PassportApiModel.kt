package com.mobatia.bisad.fragment.home.model.datacollection

import com.google.gson.annotations.SerializedName

class PassportApiModel {

    @SerializedName("id") var id: Int=0
    @SerializedName("student_unique_id") var student_unique_id: String=""
    @SerializedName("student_id") var student_id: String=""
    @SerializedName("student_name") var student_name: String=""
    @SerializedName("passport_number") var passport_number: String=""
    @SerializedName("nationality") var nationality: String=""
    @SerializedName("passport_image") var passport_image: String=""
    @SerializedName("passport_image_name") var passport_image_name: String=""
    @SerializedName("passport_image_path") var passport_image_path: String=""
    @SerializedName("date_of_issue") var date_of_issue: String=""
    @SerializedName("expiry_date") var expiry_date: String=""
    @SerializedName("passport_expired") var passport_expired: String=""
    @SerializedName("emirates_id_no") var emirates_id_no: String=""
    @SerializedName("emirates_id_image") var emirates_id_image: String=""
    @SerializedName("emirates_id_image_path") var emirates_id_image_path: String=""
    @SerializedName("emirates_id_image_name") var emirates_id_image_name: String=""
    @SerializedName("status") var status: Int=5
    @SerializedName("request") var request: Int=0
    @SerializedName("created_at") var created_at: String=""
    @SerializedName("updated_at") var updated_at: String=""
    @SerializedName("is_date_changed") var is_date_changed: Boolean=false
}