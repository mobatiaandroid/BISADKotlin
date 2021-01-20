package com.mobatia.bisad.fragment.home.model.datacollection

import com.google.gson.annotations.SerializedName

class PassportDetailModel (
    @SerializedName("id") val id: Int,
    @SerializedName("student_unique_id") val student_unique_id: String,
    @SerializedName("student_id") val student_id: String,
    @SerializedName("student_name") val student_name: String,
    @SerializedName("passport_number") val passport_number: String,
    @SerializedName("nationality") val nationality: String,
    @SerializedName("passport_image") val passport_image: String,
    @SerializedName("date_of_issue") val date_of_issue: String,
    @SerializedName("expiry_date") val expiry_date: String,
    @SerializedName("passport_expired") val passport_expired: String,
    @SerializedName("emirates_id_no") val emirates_id_no: String,
    @SerializedName("emirates_id_image") val emirates_id_image: String,
    @SerializedName("status") val status: Int,
    @SerializedName("request") val request: Int,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String
)

/*"  "id": 0,
        "student_unique_id": "4dcfbc057e2ae8589f9bbd98b591c50a",
        "student_id": "eyJpdiI6Iko4ZS9PUElWMDJwSzV2all5djZFSUE9PSIsInZhbHVlIjoiejNmdENHbVFVcGlGUXB5Y09KRERRZz09IiwibWFjIjoiNWUyYjZmNWI3YjUwOTE3MGM4ZjlkNzY0MjNkNjczMDI0YWY4MzZjNGUxMmE4Y2Q1MmIzZjBhNzMxMzczMmE0NyJ9",
        "student_name": "Reem Saif Hadher Khamis Alameemi",
        "passport_number": "",
        "nationality": "",
        "passport_image": "",
        "date_of_issue": "",
        "expiry_date": "",
        "passport_expired": "",
        "emirates_id_no": "",
        "emirates_id_image": "",
        "status": 0,
        "request": 1,
        "created_at": "",
        "updated_at": ""*/