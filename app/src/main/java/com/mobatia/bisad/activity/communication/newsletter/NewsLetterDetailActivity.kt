package com.mobatia.bisad.activity.communication.newsletter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.communication.newsletter.adapter.NewsLetterRecyclerAdapter
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterDetailApiModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterDetailModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterListAPiModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterListModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class NewsLetterDetailActivity : AppCompatActivity(){
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    var id:String=""
    var title:String?=""
    private lateinit var relativeHeader: RelativeLayout
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    private lateinit var webView: WebView
    private lateinit var progressDialog: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newsletter_detail)
        mContext=this
        id=intent.getStringExtra("id")
        title=intent.getStringExtra("title")
        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()
        initUI()
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            callNewLetterListAPI()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }

        getSettings()

    }
    fun initUI() {
        relativeHeader = findViewById(R.id.relativeHeader)
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        progressDialog = findViewById(R.id.progressDialog)
        webView = findViewById(R.id.webView)
        heading.setText("NewsLetter")
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
    fun callNewLetterListAPI()
    {
        val token = sharedprefs.getaccesstoken(mContext)
        val studentbody= NewsLetterDetailApiModel(id)
        val call: Call<NewsLetterDetailModel> = ApiClient.getClient.newsletterdetail(studentbody,"Bearer "+token)
        call.enqueue(object : Callback<NewsLetterDetailModel> {
            override fun onFailure(call: Call<NewsLetterDetailModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<NewsLetterDetailModel>, response: Response<NewsLetterDetailModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {

                    if (response.body()!!.responseArray.html.equals(""))
                    {

                    }
                    else{
                        var htmlData=response.body()!!.responseArray.html

                      //  webView.loadData(htmlData,"text/html; charset=utf-8","utf-8")
                        webView.loadDataWithBaseURL("file:///android_asset/fonts/",htmlData,"text/html; charset=utf-8", "utf-8", "about:blank")
                    }
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

                if (newProgress == 100)
                {

                    progressDialog.visibility = View.GONE

                }
            }
        }
    }
}