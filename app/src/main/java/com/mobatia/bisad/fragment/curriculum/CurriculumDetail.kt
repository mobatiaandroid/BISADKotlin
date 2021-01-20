package com.mobatia.bisad.fragment.curriculum

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.withStyledAttributes
import com.mobatia.bisad.R

class CurriculumDetail : AppCompatActivity() {

    lateinit var webview:WebView
    lateinit var progressDialog:RelativeLayout
    lateinit var mContext:Context
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_load)
        mContext=this
        webview = findViewById(R.id.webview)
        progressDialog = findViewById(R.id.progressDialog)
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        var passedintent = intent.getStringExtra("Url")
        webview.settings.javaScriptEnabled = true
        webview.settings.setSupportZoom(false)
        webview.settings.setAppCacheEnabled(false)
        webview.settings.javaScriptCanOpenWindowsAutomatically = true
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true
        webview.settings.defaultTextEncodingName = "utf-8"
        webview.settings.loadsImagesAutomatically = true
        webview.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webview.settings.allowFileAccess = true
        webview.setBackgroundColor(Color.TRANSPARENT)
        webview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)
        webview.loadUrl("https://docs.google.com/gview?embedded=true&url="+passedintent)

    }
}