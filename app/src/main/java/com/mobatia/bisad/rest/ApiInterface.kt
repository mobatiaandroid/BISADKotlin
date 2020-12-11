package com.mobatia.bisad.rest

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiInterface {
    @POST("bisad/public/oauth/access_token")
    @FormUrlEncoded
    fun accesstoken(
        @Field("grant_type") grant_type: String,
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("username") username: String,
        @Field("password") password: String
    ): Call<ResponseBody>

    @POST("bisad/api/home_banner_images_V1")
    @FormUrlEncoded
    fun bannerimages(
        @Field("access_token") access_token: String
    ): Call<ResponseBody>

    @POST("bisadv8/public/api/v1/parent_signup")
    @FormUrlEncoded
    fun signup(
        @Field("email") email: String,
        @Field("devicetype") devicetype: String,
        @Field("deviceid") deviceid: String
    ): Call<ResponseBody>
}