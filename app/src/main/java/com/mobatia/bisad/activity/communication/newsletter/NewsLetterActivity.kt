package com.mobatia.bisad.activity.communication.newsletter

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.absence.AbsenceDetailActivity
import com.mobatia.bisad.activity.communication.newsletter.adapter.NewsLetterRecyclerAdapter
import com.mobatia.bisad.activity.communication.newsletter.model.NewLetterListDetailModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterListAPiModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterListModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterResponseModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.report_absence.adapter.RequestAbsenceRecyclerAdapter
import com.mobatia.bisad.fragment.report_absence.model.AbsenceLeaveApiModel
import com.mobatia.bisad.fragment.report_absence.model.AbsenceListModel
import com.mobatia.bisad.fragment.report_absence.model.AbsenceRequestListDetailModel
import com.mobatia.bisad.fragment.student_information.adapter.StudentListAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsLetterActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    private lateinit var mNewsletterRecycler: RecyclerView
    private lateinit var progressDialog: RelativeLayout
    private lateinit var relativeHeader: RelativeLayout
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    var start:Int=0
    var limit:Int=20
    var stopLoading:Boolean=false
    var isLoading:Boolean=false
    var apiCall:Int=0
    lateinit var newsLetterArrayList :ArrayList<NewLetterListDetailModel>
    lateinit var newsLetterShowArrayList :ArrayList<NewLetterListDetailModel>
    private lateinit var linearLayoutManager: LinearLayoutManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_newsletter)
        initializeUI()
        start=0
        limit=20
        callNewLetterListAPI(start,limit)
    }

    private fun initializeUI() {
        mContext=this
        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()
        btn_left = findViewById(R.id.btn_left)
        newsLetterArrayList= ArrayList()
        logoClickImgView = findViewById(R.id.logoClickImgView)
        progressDialog = findViewById(R.id.progressDialog)
        relativeHeader = findViewById(R.id.relativeHeader)
        mNewsletterRecycler = findViewById(R.id.mNewsletterRecycler) as RecyclerView
        heading = findViewById(R.id.heading)
        heading.setText("NewsLetters")
        btn_left.setOnClickListener(View.OnClickListener {
            finish()
        })
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })
        linearLayoutManager = LinearLayoutManager(com.mobatia.bisad.fragment.home.mContext)
        mNewsletterRecycler.layoutManager = linearLayoutManager
        mNewsletterRecycler.itemAnimator = DefaultItemAnimator()
        mNewsletterRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(
                @NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == newsLetterArrayList.size - 1) {
                        //bottom of list!
                        if (!stopLoading)
                        {
                            start=start+limit
                            callNewLetterListAPI(start,limit)
                            isLoading = true
                        }

                    }
                }
            }
        })
        mNewsletterRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                val intent =Intent(mContext, NewsLetterDetailActivity::class.java)
                intent.putExtra("id",newsLetterArrayList.get(position).id)
                intent.putExtra("title",newsLetterArrayList.get(position).title)
                startActivity(intent)
            }
        })
    }


    fun callNewLetterListAPI(startValue:Int,limitValue:Int)
    {
        progressDialog.visibility = View.VISIBLE
        newsLetterShowArrayList= ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val studentbody= NewsLetterListAPiModel(startValue,limitValue)
        val call: Call<NewsLetterListModel> = ApiClient.getClient.newsletters(studentbody,"Bearer "+token)
        call.enqueue(object : Callback<NewsLetterListModel> {
            override fun onFailure(call: Call<NewsLetterListModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<NewsLetterListModel>, response: Response<NewsLetterListModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    progressDialog.visibility = View.GONE
                    newsLetterShowArrayList.addAll(response.body()!!.responseArray.campaignsList)
                    newsLetterArrayList.addAll(newsLetterShowArrayList)
                    if (newsLetterShowArrayList.size==20)
                    {
                        stopLoading=false
                    }
                    else{
                        stopLoading=true
                    }
                    if (newsLetterArrayList.size>0)
                    {
                        mNewsletterRecycler.visibility=View.VISIBLE
                        val newsLetterAdapter = NewsLetterRecyclerAdapter(newsLetterArrayList)
                        mNewsletterRecycler.setAdapter(newsLetterAdapter)
                        isLoading=false
                        if(newsLetterArrayList.size>20)
                        {
                            if(newsLetterArrayList.size>20)
                            {
                                mNewsletterRecycler.scrollToPosition(startValue)
                            }
                        }
                    }
                    else
                    {
                        mNewsletterRecycler.visibility=View.GONE
                        showSuccessAlert(mContext,"No data found.","Alert")

                    }
                }
                else if (response.body()!!.status == 116) {
                    apiCall=apiCall+1
                    if (apiCall<6)
                    {
                        AccessTokenClass.getAccessToken(mContext)
                        callNewLetterListAPI(startValue,limitValue)
                    }
                    else{
                        showSuccessAlert(mContext,"Something went wrong","Alert")
                    }

                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext
                    )
                }


            }

        })
    }

    fun showSuccessAlert(context: Context,message : String,msgHead : String)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        iconImageView.setImageResource(R.drawable.exclamationicon)
        btn_Ok?.setOnClickListener()
        {
            dialog.dismiss()

        }
        dialog.show()
    }
}