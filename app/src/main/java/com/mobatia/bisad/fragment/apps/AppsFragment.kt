package com.mobatia.bisad.fragment.apps

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.RelativeLayout
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.apps.AppsDetailActivity
import com.mobatia.bisad.activity.social_media.SocialMediaDetailActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.apps.adapter.AppsRecyclerAdapter
import com.mobatia.bisad.fragment.apps.model.AppsListDetailModel
import com.mobatia.bisad.fragment.apps.model.AppsListModel
import com.mobatia.bisad.fragment.home.loader
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.messages.adapter.MessageListRecyclerAdapter
import com.mobatia.bisad.fragment.messages.model.MessageListApiModel
import com.mobatia.bisad.fragment.messages.model.MessageListDetailModel
import com.mobatia.bisad.fragment.messages.model.MessageListModel
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AppsFragment  : Fragment(){
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var messageRecycler: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var progressDialog: RelativeLayout
    var appsListArray = ArrayList<AppsListDetailModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_class, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        initializeUI()
        callMessageListApi()
    }

    private fun initializeUI() {
        messageRecycler = view!!.findViewById(R.id.messageRecycler) as RecyclerView
        linearLayoutManager = LinearLayoutManager(mContext)
        messageRecycler.layoutManager = linearLayoutManager
        messageRecycler.itemAnimator = DefaultItemAnimator()
        progressDialog = view!!.findViewById(R.id.progressDialog) as RelativeLayout
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)

        messageRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                val url =appsListArray.get(position).link
                val intent = Intent(activity, AppsDetailActivity::class.java)
                intent.putExtra("url",url)
                intent.putExtra("heading",appsListArray.get(position).name)
                activity?.startActivity(intent)
            }
        })
    }


    fun callMessageListApi()
    {
        val call: Call<AppsListModel> = ApiClient.getClient.appsList(0,20)
        call.enqueue(object : Callback<AppsListModel> {
            override fun onFailure(call: Call<AppsListModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<AppsListModel>, response: Response<AppsListModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    appsListArray.addAll(response.body()!!.responseArray!!.appsList)

                    val messageListAdapter = AppsRecyclerAdapter(appsListArray)
                    messageRecycler.setAdapter(messageListAdapter)
                }
                else if(response.body()!!.status==116)
                {
                    AccessTokenClass.getAccessToken(mContext)
                    callMessageListApi()
                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
                }
            }

        })
    }

}




