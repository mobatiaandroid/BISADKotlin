package com.mobatia.bisad.fragment.communication

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
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
import com.mobatia.bisad.activity.home.PageView
import com.mobatia.bisad.activity.settings.termsofservice.TermsOfServiceActivity
import com.mobatia.bisad.activity.settings.tutorial.TutorialActivity
import com.mobatia.bisad.activity.social_media.SocialMediaDetailActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.communication.adapter.CommunicationRecyclerAdapter
import com.mobatia.bisad.fragment.home.loader
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

lateinit var bannerImage: ViewPager

class CommunicationFragment : Fragment(){
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var bannerImageViewPager: ImageView
    var bannerarray = ArrayList<String>()
    var currentPage: Int = 0
    var mCommunicationArrayList = ArrayList<String>()
    lateinit var progressDialog: RelativeLayout
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var socialMediaRecycler: RecyclerView
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
        mCommunicationArrayList.add("NewsLetters")
        initializeUI()
        getbannerimages()
    }

    private fun initializeUI()
    {
        bannerImage = view!!.findViewById<ViewPager>(R.id.bannerImagePager)
        linearLayoutManager = LinearLayoutManager(mContext)
        socialMediaRecycler = view!!.findViewById(R.id.socialMediaRecycler) as RecyclerView
        socialMediaRecycler.layoutManager = linearLayoutManager
        socialMediaRecycler.itemAnimator = DefaultItemAnimator()
        progressDialog = view!!.findViewById(R.id.progressDialog) as RelativeLayout
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        updatedata()
        val settingsAdapter = CommunicationRecyclerAdapter(mCommunicationArrayList)
        socialMediaRecycler.setAdapter(settingsAdapter)
        bannerImage?.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }
            override fun onPageSelected(position: Int) {
                currentPage = position
            }

        })
        socialMediaRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                if (position==0)
                {
                    val intent =Intent(activity, NewsLetterActivity::class.java)
                    activity?.startActivity(intent)
                }

            }
        })

    }

    fun getbannerimages()
    {
        val token = com.mobatia.bisad.fragment.home.sharedprefs.getaccesstoken(mContext)
        val call: Call<ResponseBody> = ApiClient.getClient.communication("Bearer "+token)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
              progressDialog.visibility=View.GONE
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                progressDialog.visibility=View.GONE

                val bannerresponse = response.body()
                if (bannerresponse != null) {
                    try {

                        val jsonObject = JSONObject(bannerresponse.string())
                        if(jsonObject.has(jsonConstans.STATUS))
                        {
                            val status : Int=jsonObject.optInt(jsonConstans.STATUS)
                            if (status==100)
                            {
                                val responseObj =jsonObject.getJSONObject("responseArray")
                                val dataArray = responseObj.getJSONArray("banner_images")
                                if (dataArray.length() > 0) {
                                    for (i in 0..dataArray.length()) {
                                        bannerarray.add(dataArray.optString(i))

                                    }
                                    bannerImage.adapter = activity?.let { PageView(it, bannerarray) }
                                }
                                else {
                                    bannerImage.setBackgroundResource(R.drawable.aboutbanner)
                                }
                            }

                            else{
                                if (status==116)
                                {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(mContext)
                                    getbannerimages()

                                }
                                else
                                {
                                    if (status==103)
                                    {
                                        //validation check error

                                    }
                                    else
                                    {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status,mContext)
                                    }
                                }

                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }
    fun updatedata() {
        val handler = Handler()

        val update = Runnable {
          if (currentPage == bannerarray.size) {
                currentPage = 0
                pager.setCurrentItem(
                    currentPage,
                    true
                )
            } else {

                pager
                    .setCurrentItem(currentPage++, true)
            }
        }
        val swipetimer = Timer()

        swipetimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 100, 6000)

    }
}




