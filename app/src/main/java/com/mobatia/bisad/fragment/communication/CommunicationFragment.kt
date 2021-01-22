package com.mobatia.bisad.fragment.communication

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.absence.AbsenceDetailActivity
import com.mobatia.bisad.activity.communication.newsletter.NewsLetterActivity
import com.mobatia.bisad.activity.communication.newsletter.NewsLetterDetailActivity
import com.mobatia.bisad.activity.communication.newsletter.adapter.NewsLetterRecyclerAdapter
import com.mobatia.bisad.activity.communication.newsletter.model.NewLetterListDetailModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterDetailModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterListAPiModel
import com.mobatia.bisad.activity.communication.newsletter.model.NewsLetterListModel
import com.mobatia.bisad.activity.home.PageView
import com.mobatia.bisad.activity.settings.termsofservice.TermsOfServiceActivity
import com.mobatia.bisad.activity.settings.tutorial.TutorialActivity
import com.mobatia.bisad.activity.social_media.SocialMediaDetailActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.communication.adapter.CommunicationRecyclerAdapter
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.home.model.BannerModel
import com.mobatia.bisad.fragment.home.pager
import com.mobatia.bisad.fragment.settings.adapter.SettingsRecyclerAdapter
import com.mobatia.bisad.fragment.socialmedia.adapter.SocialMediaRecyclerAdapter
import com.mobatia.bisad.fragment.socialmedia.model.SocialMediaDetailModel
import com.mobatia.bisad.fragment.socialmedia.model.SocialMediaListModel
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList


class CommunicationFragment : Fragment(){
    lateinit var mContext:Context
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var newsLetterArrayList :ArrayList<NewLetterListDetailModel>
    lateinit var newsLetterShowArrayList :ArrayList<NewLetterListDetailModel>
    lateinit var progressDialog: RelativeLayout
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var newsLetterRecycler: RecyclerView
    var apiCall:Int=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_communication, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        initializeUI()
        callNewLetterListAPI()
    }

    private fun initializeUI()
    {
        newsLetterArrayList= ArrayList()
        linearLayoutManager = LinearLayoutManager(mContext)
        newsLetterRecycler = view!!.findViewById(R.id.newsLetterRecycler) as RecyclerView
        newsLetterRecycler.layoutManager = linearLayoutManager
        newsLetterRecycler.itemAnimator = DefaultItemAnimator()
        progressDialog = view!!.findViewById(R.id.progressDialog) as RelativeLayout
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        newsLetterRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                val intent =Intent(mContext, NewsLetterDetailActivity::class.java)
                intent.putExtra("id",newsLetterArrayList.get(position).id)
                intent.putExtra("title",newsLetterArrayList.get(position).title)
                startActivity(intent)
            }
        })

    }

    fun callNewLetterListAPI()
    {
        progressDialog.visibility = View.VISIBLE
        newsLetterShowArrayList= ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<NewsLetterListModel> = ApiClient.getClient.newsletters("Bearer "+token)
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
                    newsLetterArrayList.addAll(response.body()!!.responseArray.campaignsList)
                    if (newsLetterArrayList.size>0)
                    {
                        newsLetterRecycler.visibility=View.VISIBLE
                        val newsLetterAdapter = NewsLetterRecyclerAdapter(newsLetterArrayList)
                        newsLetterRecycler.setAdapter(newsLetterAdapter)

                    }
                    else
                    {
                        newsLetterRecycler.visibility=View.GONE
                        showSuccessAlert(mContext,"No data found.","Alert")

                    }
                }
                else if (response.body()!!.status == 116) {
                    apiCall=apiCall+1
                    if (apiCall<3)
                    {
                        AccessTokenClass.getAccessToken(mContext)
                        callNewLetterListAPI()
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




