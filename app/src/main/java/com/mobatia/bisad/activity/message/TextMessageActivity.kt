package com.mobatia.bisad.activity.message

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
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.message.model.MessageDetailApiModel
import com.mobatia.bisad.activity.message.model.MessageDetailModel
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class TextMessageActivity : AppCompatActivity(){
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    var id:String=""
    var title:String=""
    var idApi:String=""
    var titleApi:String=""
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
        id=intent.getStringExtra("id")
        title=intent.getStringExtra("title")
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
        heading.setText("Messages")
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
        val studentbody= MessageDetailApiModel(id)
        val call: Call<MessageDetailModel> = ApiClient.getClient.notifictaionDetail(studentbody,"Bearer "+token)
        call.enqueue(object : Callback<MessageDetailModel> {
            override fun onFailure(call: Call<MessageDetailModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<MessageDetailModel>, response: Response<MessageDetailModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                         idApi=id
                         titleApi=title
                         message=response.body()!!.responseArray.notificationArray.message
                         url=response.body()!!.responseArray.notificationArray.url
                         date=response.body()!!.responseArray.notificationArray.created_at
                         val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                         val outputFormat: DateFormat = SimpleDateFormat("hh:mm a")
                         val outputFormatdate: DateFormat = SimpleDateFormat("dd-MMM-yyyy")
                         val inputDateStr = date
                         val date: Date = inputFormat.parse(inputDateStr)
                         val outputDateStr: String = outputFormat.format(date)
                         val outputDateStr1: String = outputFormatdate.format(date)

                    var pushNotificationDetail="<!DOCTYPE html>\n"+
                                 "<html>\n" +
                                 "<head>\n" +
                                 "<style>\n" +
                                 "\n" +
                                 "@font-face {\n" +
                                 "font-family: SourceSansPro-Semibold;" +
                                 "src: url(SourceSansPro-Semibold.ttf);" +

                                 "font-family: SourceSansPro-Regular;" +
                                 "src: url(SourceSansPro-Regular.ttf);" +
                                 "}" +
                                 ".title {" +
                                 "font-family: SourceSansPro-Semibold;" +
                                 "font-size:16px;" +
                                 "text-align:left;" +
                                 "color: #46C1D0;" +
                                 "text-align: ####TEXT_ALIGN####;" +
                                 "}" +
                                 ".date {\n" +
                                 "font-family: SourceSansPro-Regular;\n" +
                                 "font-size:12px;\n" +
                                 "text-align:right;\n" +
                                 "color:#908C86;\n" +
                                 "text-align: ####TEXT_ALIGN####;\n" +
                                 "}"+
                                 ".description {" +
                                 "font-family: SourceSansPro-Semibold;" +
                                 "text-align:justify;" +
                                 "font-size:14px;" +
                                 "color: #000000;" +
                                 "text-align: ####TEXT_ALIGN####;" +
                                 "}" +
                                 "</style>\n" + "</head>" +
                                 "<body>" +
                                 "<p class='title'>"+title

                    pushNotificationDetail=pushNotificationDetail+"<p class='date'>"+outputDateStr1 +" "+outputDateStr+ "</p>"+"<hr>"+ "<p class='description'>" +message+ "</p>"
                    if (!url.equals(""))
                    {
                        pushNotificationDetail=pushNotificationDetail+"<center><img src='" + url + "'width='100%', height='auto'>"
                    }
                    pushNotificationDetail=pushNotificationDetail+"</body>\n</html>"
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