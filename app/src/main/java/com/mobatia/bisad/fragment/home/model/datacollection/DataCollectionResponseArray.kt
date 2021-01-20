package com.mobatia.bisad.fragment.home.model.datacollection

import com.google.gson.annotations.SerializedName
import com.mobatia.bisad.fragment.home.model.CountryiesDetailModel

class DataCollectionResponseArray(
    @SerializedName("display_message") val display_message: String,
    @SerializedName("own_details") val ownDetailsList: ArrayList<OwnDetailsModel>,
    @SerializedName("kin_details") val kinDetailsList: ArrayList<KinDetailsModel>,
    @SerializedName("health_and_insurence") val healthInsurenceList: ArrayList<HealthInsuranceDetailModel>,
    @SerializedName("passport_details") val passportDetailsList: ArrayList<PassportDetailModel>
)