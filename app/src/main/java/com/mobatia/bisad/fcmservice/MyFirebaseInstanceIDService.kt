package com.mobatia.bisad.fcmservice

import android.content.Context
import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import com.mobatia.bisad.manager.PreferenceData

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class MyFirebaseInstanceIDService : FirebaseInstanceIdService() {
    var mContext: Context = this
    var sharedprefs: PreferenceData= PreferenceData()


    override fun onTokenRefresh() {

        //val refreshedToken = FirebaseInstanceId.getInstance().token

        val refreshedToken = FirebaseInstanceId.getInstance().token.toString()

        Log.e("FIREBASETOKEN", refreshedToken)
        sendRegistrationToServer(refreshedToken)
        super.onTokenRefresh()
    }

    private fun sendRegistrationToServer(refreshedToken: String) {
        if (refreshedToken != null) {
            sharedprefs.setFcmID(mContext, refreshedToken)
        }

    }
}