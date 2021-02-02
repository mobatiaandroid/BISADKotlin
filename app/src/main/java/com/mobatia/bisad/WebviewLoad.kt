package com.mobatia.bisad

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.fragment.home.mContext
import java.net.URLEncoder

class WebviewLoad : AppCompatActivity() {

    lateinit var webview: WebView
    lateinit var progressdialog: RelativeLayout
    lateinit var btn_left: ImageView

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_webview_load)

        webview = findViewById(R.id.webview)
        btn_left = findViewById(R.id.btn_left)
        progressdialog = findViewById(R.id.progressDialog)

        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressdialog.startAnimation(aniRotate)


        btn_left.setOnClickListener {
            finish()
        }


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

        webview.webViewClient = WebViewClient()

        //webview.loadUrl("http://www.amazon.com")



        //webview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + passedintent)


        webview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(passedintent))

       // webview.loadUrl(passedintent)
        Log.e("PDF: ", passedintent)

    }
    inner class WebViewClient : android.webkit.WebViewClient() {
        override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
            Log.e("PDFview: ",url)

            view.loadUrl(url)
            return false
        }

        override fun onPageFinished(view: WebView, url: String) {
            super.onPageFinished(view, url)
            progressdialog.visibility = View.GONE
        }
    }

    override fun onBackPressed() {
        finish()
    }
}