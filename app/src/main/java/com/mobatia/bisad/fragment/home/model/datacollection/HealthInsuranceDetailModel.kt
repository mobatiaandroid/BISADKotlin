package com.mobatia.bisad.fragment.home.model.datacollection

import com.google.gson.annotations.SerializedName

class HealthInsuranceDetailModel (

    @SerializedName("id") val id: Int,
    @SerializedName("student_unique_id") val student_unique_id: String,
    @SerializedName("student_id") val student_id: String,
    @SerializedName("student_name") val student_name: String,
    @SerializedName("health_detail") val health_detail: String,
    @SerializedName("health_form_link") val health_form_link: String,
    @SerializedName("status") val status: Int,
    @SerializedName("request") val request: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
)

/* "id": 41,
        "student_unique_id": "4dcfbc057e2ae8589f9bbd98b591c50a",
        "student_id": "eyJpdiI6IldldHRlYWFCU01vejJ5eUd3aHlaZmc9PSIsInZhbHVlIjoiYkk4ajJrZXNNTlhuQ0NybE1IYk9EZz09IiwibWFjIjoiMjU0NTVmMzA3YmU5ZTM3YjhkMDZlY2ViOTFkMTdiNWUwMDI4YTA0ODQxYzBiZDNiODIyNTZkMzZhMTJjN2E2OSJ9",
        "student_name": "Reem Saif Hadher Khamis Alameemi",
        "health_detail": "Wearing glasses",
        "health_form_link": "https:\/\/fs30.formsite.com\/BritishInternationalSchoolAbuDhabi\/cnbwsvce2d\/index.html",
        "status": 0,
        "request": 0,
        "created_at": "2021-01-13 17:17:05",
        "updated_at": "2021-01-13 17:17:05"*/