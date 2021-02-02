package com.mobatia.bisad

import android.annotation.SuppressLint
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebView
import android.widget.ImageView
import android.widget.RelativeLayout
import com.mobatia.bisad.fragment.home.mContext
import java.net.URLEncoder

@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class WebviewLoad : AppCompatActivity() {

    lateinit var webview: WebView
    lateinit var progressdialog: RelativeLayout
    lateinit var btn_left: ImageView
    lateinit var passedintent:String

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


        passedintent = intent.getStringExtra("Url")
        webview.settings.javaScriptEnabled = true
        webview.settings.setSupportZoom(true)
        webview.settings.setAppCacheEnabled(true)
        webview.settings.javaScriptCanOpenWindowsAutomatically = true
        webview.settings.domStorageEnabled = true
        webview.settings.databaseEnabled = true
        webview.settings.defaultTextEncodingName = "utf-8"
        webview.settings.loadsImagesAutomatically = true
        webview.settings.allowFileAccess = true
        webview.setBackgroundColor(Color.TRANSPARENT)
        webview.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)

        webview.webViewClient = WebViewClient()

        webview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(passedintent))

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
            //webview.loadUrl("https://docs.google.com/gview?embedded=true&url=" + URLEncoder.encode(passedintent))

        }
    }

    override fun onBackPressed() {
        finish()
    }
}