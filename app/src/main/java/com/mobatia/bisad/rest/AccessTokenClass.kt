package com.mobatia.bisad.rest

import android.content.Context
import android.os.Handler
import android.util.Log
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.manager.PreferenceData
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AccessTokenClass {

    companion object
    {
        lateinit var jsonConstans: JsonConstants
        lateinit var sharedprefs: PreferenceData

        fun getAccessToken(mContext : Context): String? {
            jsonConstans = JsonConstants()
            sharedprefs = PreferenceData()
            val call: Call<ResponseBody> = ApiClient.getClient.accesstoken(
                jsonConstans.grant_type,
                jsonConstans.client_id,
                jsonConstans.client_secret,
                jsonConstans.username,
                jsonConstans.password
            )
            call.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("Failed", t.localizedMessage)
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val responsedata = response.body()
                    Log.e("Response", responsedata.toString())
                    if (responsedata != null) {
                        try {
                            val jsonObject = JSONObject(responsedata.string())
                            val accessToken: String = jsonObject.optString("access_token")
                            Log.e("Accesstokenlog", accessToken)
                            sharedprefs.setaccesstoken(mContext, accessToken)
                            Log.e("SharedPrefsAccess", sharedprefs.getaccesstoken(mContext))

                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

            })

           return  sharedprefs.getaccesstoken(mContext)
        }
    }
}