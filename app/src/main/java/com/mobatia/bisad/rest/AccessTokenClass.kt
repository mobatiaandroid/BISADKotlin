package com.mobatia.bisad.rest

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.InternetCheckClass
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
            val call: Call<ResponseBody> = ApiClient.getClient.access_token(
               sharedprefs.getUserCode(mContext)
            )
            call.enqueue(object : Callback<ResponseBody> {
                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    Log.e("Failed", t.localizedMessage)
                }

                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    val responsedata = response.body()
                    Log.e("Response", responsedata.toString())
                    if (responsedata != null) {
//                        try {
//                            val jsonObject = JSONObject(responsedata.string())
//                            val accessToken: String = jsonObject.optString("access_token")
//                            Log.e("Accesstokenlog", accessToken)
//                            sharedprefs.setaccesstoken(mContext, accessToken)
//                            Log.e("SharedPrefsAccess", sharedprefs.getaccesstoken(mContext))
//
//                        }
//
                        try {

                            val jsonObject = JSONObject(responsedata.string())
                            if(jsonObject.has(jsonConstans.STATUS))
                            {
                                val status : Int=jsonObject.optInt(jsonConstans.STATUS)
                                Log.e("STATUS LOGIN",status.toString())
                                if (status==100)
                                {
                                    //Success API response
                                    val accessToken: String = jsonObject.optString("token")
                                    sharedprefs.setaccesstoken(mContext, accessToken)

                                }
                                else{
                                    if (status==116)
                                    {
                                        //call Token Expired
                                    }
                                    else
                                    {
                                        if (status==103)
                                        {
                                            //validation check error
                                        }
                                        else
                                        {
                                            //check status code checks
                                            Log.e("Login Status","Code entered")
                                            InternetCheckClass.checkApiStatusError(status,mContext)
                                        }
                                    }

                                }
                            }
//                        val accessToken: String = jsonObject.optString("access_token")
//                        Log.e("Accesstokenlog", accessToken)
//                        AccessTokenClass.sharedprefs.setaccesstoken(mContext, accessToken)
//                        Log.e("SharedPrefsAccess", AccessTokenClass.sharedprefs.getaccesstoken(mContext))
                        }
                        catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                }

            })

           return  sharedprefs.getaccesstoken(mContext)
        }
    }
}