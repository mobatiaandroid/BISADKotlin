package com.mobatia.bisad.fragment.socialmedia

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.absence.AbsenceDetailActivity
import com.mobatia.bisad.activity.apps.AppsDetailActivity
import com.mobatia.bisad.activity.social_media.SocialMediaDetailActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.home.pager
import com.mobatia.bisad.fragment.messages.adapter.MessageListRecyclerAdapter
import com.mobatia.bisad.fragment.messages.model.MessageListApiModel
import com.mobatia.bisad.fragment.messages.model.MessageListModel
import com.mobatia.bisad.fragment.socialmedia.adapter.SocialMediaRecyclerAdapter
import com.mobatia.bisad.fragment.socialmedia.model.SocialMediaDetailModel
import com.mobatia.bisad.fragment.socialmedia.model.SocialMediaListModel
import com.mobatia.bisad.fragment.student_information.adapter.StudentInfoAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentInfoDetail
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class SocialMediaFragment : Fragment(){
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var bannerImageViewPager: ImageView
    var bannerarray = ArrayList<String>()
   lateinit var socialMediaArrayList : ArrayList<SocialMediaDetailModel>
    lateinit var progressDialog: RelativeLayout
    lateinit var mContext: Context
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var socialMediaRecycler: RecyclerView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_social_media, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        initializeUI()
        callSocialMediaList()
    }

    private fun initializeUI() {

        bannerImageViewPager = view!!.findViewById(R.id.bannerImageViewPager) as ImageView

        linearLayoutManager = LinearLayoutManager(mContext)
        socialMediaRecycler = view!!.findViewById(R.id.socialMediaRecycler) as RecyclerView
        socialMediaRecycler.layoutManager = linearLayoutManager
        socialMediaRecycler.itemAnimator = DefaultItemAnimator()
        progressDialog = view!!.findViewById(R.id.progressDialog) as RelativeLayout
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)

        socialMediaRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                val url = socialMediaArrayList.get(position).url
                val intent =Intent(activity,SocialMediaDetailActivity::class.java)
                intent.putExtra("url",url)
                intent.putExtra("title",socialMediaArrayList.get(position).tab_type)
                activity?.startActivity(intent)
            }
        })

    }

    fun callSocialMediaList()
    {
        socialMediaArrayList=ArrayList()
        bannerarray = ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<SocialMediaListModel> = ApiClient.getClient.socialMedia("Bearer "+token)
        call.enqueue(object : Callback<SocialMediaListModel> {
            override fun onFailure(call: Call<SocialMediaListModel>, t: Throwable) {
               // loader.visibility = View.GONE
                progressDialog.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<SocialMediaListModel>, response: Response<SocialMediaListModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    socialMediaArrayList.addAll(response.body()!!.responseArray.dataList)
                    val socialMediaRecyclerAdapter = SocialMediaRecyclerAdapter(socialMediaArrayList)
                    socialMediaRecycler.setAdapter(socialMediaRecyclerAdapter)
                    if (response.body()!!.responseArray.bannerList.size>0)
                    {
                        bannerarray.addAll(response.body()!!.responseArray.bannerList)

                    }
                    if (bannerarray.size>0)
                    {
                        Glide.with(mContext) //1
                            .load(bannerarray.get(0).toString())
                            .placeholder(R.drawable.socialbanner)
                            .error(R.drawable.socialbanner)
                            .skipMemoryCache(true) //2
                            .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                            .into(bannerImageViewPager)
                    }
                    else{
                        bannerImageViewPager.setBackgroundResource(R.drawable.socialbanner)
                    }

                }
                else if(response.body()!!.status==116)
                {
                    AccessTokenClass.getAccessToken(mContext)
                    callSocialMediaList()
                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.status,mContext)
                }
            }

        })
    }

}




