package com.mobatia.bisad

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WebviewLoad : AppCompatActivity() {

    lateinit var webview:WebView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_load)

        webview = findViewById(R.id.webview)
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
        Log.e("PDF: ",passedintent)

    }
}