package com.mobatia.bisad.activity.settings.termsofservice

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.RotateAnimation
import android.webkit.*
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.message.model.MessageDetailApiModel
import com.mobatia.bisad.activity.message.model.MessageDetailModel
import com.mobatia.bisad.activity.settings.termsofservice.model.TermsOfServiceModel
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.messages.adapter.MessageListRecyclerAdapter
import com.mobatia.bisad.fragment.messages.model.MessageListApiModel
import com.mobatia.bisad.fragment.messages.model.MessageListModel
import com.mobatia.bisad.manager.HeaderManagerNoColorSpace
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TermsOfServiceActivity : AppCompatActivity(){
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    var message:String=""
    var url:String=""
    var date:String=""
    private lateinit var relativeHeader: RelativeLayout
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    private lateinit var webView: WebView
    var myFormatCalende:String="yyyy-MM-dd HH:mm:ss"
    private lateinit var progressDialog: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_text_message_detail)
        mContext=this
        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()
        initUI()
        callMessageDetailAPI()
        getSettings()

    }
    fun initUI() {
        relativeHeader = findViewById(R.id.relativeHeader)
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        progressDialog = findViewById(R.id.progressDialog)
        webView = findViewById(R.id.webView)
        heading.setText("Terms of Services")
        btn_left.setOnClickListener(View.OnClickListener {
            finish()
        })
        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
    }
    fun callMessageDetailAPI()
    {
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<TermsOfServiceModel> = ApiClient.getClient.termsOfService("Bearer "+token)
        call.enqueue(object : Callback<TermsOfServiceModel> {
            override fun onFailure(call: Call<TermsOfServiceModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<TermsOfServiceModel>, response: Response<TermsOfServiceModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    var termsTitle=response.body()!!.responseArray.termsofService.title
                    var termsDescription=response.body()!!.responseArray.termsofService.description

                    var pushNotificationDetail="<!DOCTYPE html>\n" +
                            "<html>\n" +
                            "<head>\n" +
                            "<style>\n" +
                            "\n" +
                            "@font-face {\n" +
                            "font-family: SourceSansPro-Semibold;"+
                            "src: url(SourceSansPro-Semibold.ttf);"+

                            "font-family: SourceSansPro-Regular;"+
                            "src: url(SourceSansPro-Regular.ttf);"+
                            "}"+
                            ".title {"+
                            "font-family: SourceSansPro-Regular;"+
                            "font-size:16px;"+
                            "text-align:left;"+
                            "color:	#46C1D0;"+
                            "text-align: ####TEXT_ALIGN####;"+
                            "}"+

                            ".description {"+
                            "font-family: SourceSansPro-Light;"+
                            "text-align:justify;"+
                            "font-size:14px;"+
                            "color: #000000;"+
                            "text-align: ####TEXT_ALIGN####;"+
                            "}"+
                            "</style>\n"+"</head>"+
                            "<body>"+
                            "<p class='title'>"+termsTitle+"</p>"+"<p class='description'>"+termsDescription+"</p>"+
                            "</body>\n</html>";
                     var htmlData=pushNotificationDetail
                    Log.e("HTML DATA",htmlData)
                    //  webView.loadData(htmlData,"text/html; charset=utf-8","utf-8")
                    webView.loadDataWithBaseURL("file:///android_asset/fonts/",htmlData,"text/html; charset=utf-8", "utf-8", "about:blank")

                }


            }

        })
    }

    fun getSettings()
    {
        webView.settings.javaScriptEnabled = true
        webView.settings.setSupportZoom(false)
        webView.settings.setAppCacheEnabled(false)
        webView.settings.javaScriptCanOpenWindowsAutomatically = true
        webView.settings.domStorageEnabled = true
        webView.settings.databaseEnabled = true
        webView.settings.defaultTextEncodingName = "utf-8"
        webView.settings.loadsImagesAutomatically = true
        webView.settings.cacheMode = WebSettings.LOAD_NO_CACHE
        webView.settings.allowFileAccess = true
        webView.setBackgroundColor(Color.TRANSPARENT)
        webView.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null)


        webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(view: WebView, newProgress: Int) {
                progressDialog.visibility = View.VISIBLE
                println("testing2")
                if (newProgress == 100)
                {
                    println("testing1")
                    progressDialog.visibility = View.GONE

                }
            }
        }
    }
}

/*
private fun callTermsOfServiceApi()
{
    loader.visibility = View.VISIBLE
    val token = sharedprefs.getaccesstoken(mContext)
    val call: Call<TermsOfServiceModel> = ApiClient.getClient.termsOfService("Bearer "+token)
    call.enqueue(object : Callback<TermsOfServiceModel> {
        override fun onFailure(call: Call<TermsOfServiceModel>, t: Throwable) {
            loader.visibility = View.GONE
            Log.e("Error", t.localizedMessage)
        }
        override fun onResponse(call: Call<TermsOfServiceModel>, response: Response<TermsOfServiceModel>) {
            loader.visibility = View.GONE
            if (response.body()!!.status==100)
            {

            }
            else if(response.body()!!.status==116)
            {
                AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                callTermsOfServiceApi()
            }
            else
            {
                InternetCheckClass.checkApiStatusError(response.body()!!.status,mContext)
            }
        }

    })
}*/
