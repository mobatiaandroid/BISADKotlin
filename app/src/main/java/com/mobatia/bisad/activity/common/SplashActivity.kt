package com.mobatia.bisad.activity.common

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.iid.FirebaseInstanceId
import com.mobatia.bisad.MainActivity
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass

class SplashActivity : AppCompatActivity() {
    lateinit var mContext: Context
    private val SPLASH_TIME_OUT:Long = 3000
    lateinit var sharedprefs: PreferenceData
    var firebaseid:String=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        mContext=this
        sharedprefs = PreferenceData()
        firebaseid = FirebaseInstanceId.getInstance().token.toString()

        Log.e("FIREBASE ID ANDROID:",firebaseid)
        sharedprefs.setFcmID(mContext,firebaseid)


        Handler().postDelayed({
            if (sharedprefs.getUserCode(mContext).equals(""))
            {
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
            }
            else
            {
                var accessTokenValue=AccessTokenClass.getAccessToken(mContext)
                Log.e("AccessToken",accessTokenValue)
                sharedprefs.setSuspendTrigger(mContext,"0")
                startActivity(Intent(this, HomeActivity::class.java))
                finish()
            }
        }, SPLASH_TIME_OUT)
    }
}