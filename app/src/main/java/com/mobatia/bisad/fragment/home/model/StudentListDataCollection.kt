package com.mobatia.bisad.fragment.home.model

import com.google.gson.annotations.SerializedName

class StudentListDataCollection {

    @SerializedName("id") var id: String=""
    @SerializedName("unique_id") var unique_id: String=""
    @SerializedName("name") var name: String=""
    @SerializedName("studentListClass") var studentListClass: String=""
    @SerializedName("section") var section: String=""
    @SerializedName("house") var house: String=""
    @SerializedName("photo") var photo: String=""
    @SerializedName("isConfirmed") var isConfirmed: Boolean=false

}