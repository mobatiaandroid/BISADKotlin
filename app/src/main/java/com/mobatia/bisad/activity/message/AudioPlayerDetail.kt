@file:Suppress("UNREACHABLE_CODE")

package com.mobatia.bisad.activity.message

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.bisad.R

import com.mobatia.bisad.activity.message.model.MessageDetailApiModel
import com.mobatia.bisad.activity.message.model.MessageDetailModel
import com.mobatia.bisad.activity.message.model.MessageDetailNotificationModel
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import tcking.github.com.giraffeplayer.GiraffePlayer
import tv.danmaku.ijk.media.player.IMediaPlayer


lateinit var player: GiraffePlayer

class AudioPlayerDetail : AppCompatActivity() {

    lateinit var extras: Bundle
    lateinit var audio_title: String
    lateinit var audio_id: String
    lateinit var audio_updated: String
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    var mesageDetailList = ArrayList<MessageDetailNotificationModel>()
    lateinit var mContext: Context
    var alert_type: String = ""
    var created_at: String = ""
    var title: String = ""
    var message: String = ""
    var updated_at: String = ""
    var url: String = ""
    private lateinit var progressDialog: RelativeLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_audio_player_detail)
        extras = intent.extras!!
        audio_id = extras.getString("audio_id")!!
        audio_title = extras.getString("audio_title")!!
        audio_updated = extras.getString("audio_updated")!!
        sharedprefs=PreferenceData()
        mContext = this
        player = GiraffePlayer(this)
        progressDialog = findViewById(R.id.progressDialog)
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        audiodetails()
        player.onComplete {
            Toast.makeText(applicationContext, "Play completed", Toast.LENGTH_SHORT).show()
        }.onInfo { what, extra ->
            when (what) {
                IMediaPlayer.MEDIA_INFO_BUFFERING_START -> {

                }
                IMediaPlayer.MEDIA_INFO_BUFFERING_END -> {

                }
                IMediaPlayer.MEDIA_INFO_NETWORK_BANDWIDTH -> {

                }
                IMediaPlayer.MEDIA_INFO_VIDEO_RENDERING_START -> {

                }
            }
        }.onError { what, extra ->

            Toast.makeText(applicationContext, "Can't play this audio$what", Toast.LENGTH_SHORT)
                .show()

        }
    }


    fun audiodetails() {
        val token = sharedprefs.getaccesstoken(mContext)
        val studentbody= MessageDetailApiModel(audio_id)
        progressDialog.visibility = View.VISIBLE
        val call: Call<MessageDetailModel> = ApiClient.getClient.notifictaionDetail(studentbody,"Bearer "+token)
        call.enqueue(object : Callback<MessageDetailModel> {
            override fun onFailure(call: Call<MessageDetailModel>, t: Throwable) {
              //  progressDialog.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
                progressDialog.visibility = View.GONE
            }
            override fun onResponse(call: Call<MessageDetailModel>, response: Response<MessageDetailModel>) {
                progressDialog.visibility = View.GONE
               // progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)

                {
                    title = response.body()!!.responseArray.notificationArray.title
                    message = response.body()!!.responseArray.notificationArray.message
                    alert_type = response.body()!!.responseArray.notificationArray.alert_type
                    created_at = response.body()!!.responseArray.notificationArray.created_at
                    updated_at = response.body()!!.responseArray.notificationArray.updated_at
                    url = response.body()!!.responseArray.notificationArray.url
                    player.play(url)
                    println("MSGRESPONSEAUDIO:" + response.body()!!.responseArray.notificationArray.url)
                } else if (response.body()!!.status == 116) {
                    AccessTokenClass.getAccessToken(mContext)
                    audiodetails()
                } else {
                    InternetCheckClass.checkApiStatusError(
                        response.body()!!.status,mContext
                    )
                }


            }

        })

    }

    override fun onPause() {
        super.onPause()
        player.onPause()
    }

    override fun onResume() {
        super.onResume()
        player.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()
        player.onDestroy()
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        player.onConfigurationChanged(newConfig)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        return
        super.onBackPressed()
    }

}