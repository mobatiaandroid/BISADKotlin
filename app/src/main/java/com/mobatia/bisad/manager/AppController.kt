package com.mobatia.bisad.manager

import android.content.res.TypedArray
import androidx.multidex.MultiDexApplication

class AppController : MultiDexApplication() {
    var listitemArrays: ArrayList<String> = ArrayList()
    var mListImgArrays: TypedArray? = null
    var mTitles: String? = null

}