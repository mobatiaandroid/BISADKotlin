package com.mobatia.bisad.fragment.socialmedia

import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
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
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.social_media.SocialMediaDetailActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.socialmedia.adapter.SocialMediaRecyclerAdapter
import com.mobatia.bisad.fragment.socialmedia.model.SocialMediaDetailModel
import com.mobatia.bisad.fragment.socialmedia.model.SocialMediaListModel
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            callSocialMediaList()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }

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

                var mPackage: String =""
                when(socialMediaArrayList.get(position).tab_type){

                    "Youtube" -> mPackage = "com.google.android.youtube"
                    "Instagram" -> mPackage = "com.instagram.android"
                    "Twitter" -> mPackage = "com.twitter.android"
                    "Facebook" -> mPackage = "fb"
                    "Linkedin" -> mPackage = "com.linkedin.android"

                }


                if (mPackage == "fb"){

                    Log.d("ASD",socialMediaArrayList[position].page_id)

                    val facebookAppIntent: Intent
                    try {
                        facebookAppIntent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("fb://page/${socialMediaArrayList[position].page_id}")
                        )
                        startActivity(facebookAppIntent)
                    } catch (e: ActivityNotFoundException) {

                        val url = socialMediaArrayList.get(position).url
                        val intent = Intent(activity, SocialMediaDetailActivity::class.java)
                        intent.putExtra("url", url)
                        intent.putExtra("title", socialMediaArrayList.get(position).tab_type)
                        activity?.startActivity(intent)

                    }

                }else {

                    val uri = Uri.parse(socialMediaArrayList.get(position).url)
                    val likeIng = Intent(Intent.ACTION_VIEW, uri)
                    likeIng.setPackage(mPackage)
                    try {
                        startActivity(likeIng)
                    } catch (e: ActivityNotFoundException) {
                        val url = socialMediaArrayList.get(position).url
                        val intent = Intent(activity, SocialMediaDetailActivity::class.java)
                        intent.putExtra("url", url)
                        intent.putExtra("title", socialMediaArrayList.get(position).tab_type)
                        activity?.startActivity(intent)

                    }
                }


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
                Log.d("REEEEE",response.body().toString())
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    socialMediaArrayList.addAll(response.body()!!.responseArray.dataList)
                    val socialMediaRecyclerAdapter = SocialMediaRecyclerAdapter(socialMediaArrayList)
                    socialMediaRecycler.adapter = socialMediaRecyclerAdapter
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

    private fun openFacebookPage(pageId: String) {
        val facebookUrl = "www.facebook.com/$pageId"
        val facebookID = pageId

        try {
            val versionCode = activity!!.applicationContext.packageManager.getPackageInfo("com.facebook.katana", 0).versionCode
            if (!facebookID.isEmpty()) {
                // open the Facebook app using facebookID (fb://profile/facebookID or fb://page/facebookID)
                val uri = Uri.parse("fb://page/$facebookID")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            } else if (versionCode >= 3002850 && !facebookUrl.isEmpty()) {
                // open Facebook app using facebook url
                val uri = Uri.parse("fb://facewebmodal/f?href=$facebookUrl")
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            } else {
                // Facebook is not installed. Open the browser
                val uri = Uri.parse(facebookUrl)
                startActivity(Intent(Intent.ACTION_VIEW, uri))
            }
        } catch (e: PackageManager.NameNotFoundException) {
            // Facebook is not installed. Open the browser
            val uri = Uri.parse(facebookUrl)
            startActivity(Intent(Intent.ACTION_VIEW, uri))
        }
    }

}




