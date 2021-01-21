package com.mobatia.bisad.activity.home.model

import com.google.gson.annotations.SerializedName

class HealthInsuranceDetailAPIModel {

    @SerializedName("id") var id: Int=0
    @SerializedName("student_unique_id") var student_unique_id: String=""
    @SerializedName("student_id") var student_id: String=""
    @SerializedName("student_name") var student_name: String=""
    @SerializedName("health_detail") var health_detail: String=""
    @SerializedName("health_form_link") var health_form_link: String=""
    @SerializedName("status") var status: Int=5
    @SerializedName("request") var request: Int=0
    @SerializedName("created_at") var created_at: String=""
    @SerializedName("updated_at") var updated_at: String=""
}