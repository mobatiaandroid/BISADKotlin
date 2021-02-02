package com.mobatia.bisad.fragment.messages

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
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
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.message.AudioPlayerDetail
import com.mobatia.bisad.activity.message.ImageMessageActivity
import com.mobatia.bisad.activity.message.TextMessageActivity
import com.mobatia.bisad.activity.message.VideoMessageActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
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

class MessageFragment : Fragment(){
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var messageRecycler: RecyclerView
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var progressDialog: RelativeLayout
    var isLoading:Boolean=false
    var stopLoading=false
    var startValue:Int=0
    var limit:Int=20
   lateinit var mContext:Context
   lateinit var messageArrayList :ArrayList<MessageListDetailModel>
    lateinit var messageShowArrayList :ArrayList<MessageListDetailModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_message, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        messageShowArrayList= ArrayList()
        initializeUI()
        startValue=0
        limit=20
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            callMessageListApi(startValue,limit)
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }

    }

    private fun initializeUI() {
        messageRecycler = view!!.findViewById(R.id.messageRecycler) as RecyclerView
        progressDialog = view!!.findViewById(R.id.progressDialog) as RelativeLayout
        linearLayoutManager = LinearLayoutManager(mContext)
        messageRecycler.layoutManager = linearLayoutManager
        messageRecycler.itemAnimator = DefaultItemAnimator()
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)


        messageRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(
                @NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == messageShowArrayList.size - 1) {
                        //bottom of list!
                        if (!stopLoading)
                        {
                            startValue=startValue+limit
                            callMessageListApi(startValue,limit)
                            isLoading = true
                        }

                    }
                }
            }
        })
        messageRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if(messageShowArrayList.get(position).alert_type.equals("Text"))
                {
                    val intent = Intent(activity, TextMessageActivity::class.java)
                    intent.putExtra("id",messageShowArrayList.get(position).id)
                    intent.putExtra("title",messageShowArrayList.get(position).title)
                    activity?.startActivity(intent)
                }
                else if (messageShowArrayList.get(position).alert_type.equals("Video"))
                {
                    val intent = Intent(activity, VideoMessageActivity::class.java)
                    intent.putExtra("id",messageShowArrayList.get(position).id)
                    intent.putExtra("title",messageShowArrayList.get(position).title)
                    activity?.startActivity(intent)
                }
                else if (messageShowArrayList.get(position).alert_type.equals("Image"))
                {
                    val intent = Intent(activity, ImageMessageActivity::class.java)
                    intent.putExtra("id",messageShowArrayList.get(position).id)
                    intent.putExtra("title",messageShowArrayList.get(position).title)
                    activity?.startActivity(intent)
                }
                else if (messageShowArrayList.get(position).alert_type.equals("Voice"))
                {
                    val intent = Intent(activity, AudioPlayerDetail::class.java)
                    intent.putExtra("audio_title", messageShowArrayList[position].title)
                    intent.putExtra("audio_id", messageShowArrayList[position].id)
                    intent.putExtra("audio_updated", messageShowArrayList[position].updated_at)
                    activity?.startActivity(intent)
                }

            }
        })
    }


    fun callMessageListApi(start:Int,limit:Int)
    {
        messageArrayList= ArrayList()
        progressDialog.visibility = View.VISIBLE
        val token = sharedprefs.getaccesstoken(mContext)
        val notificationBody= MessageListApiModel(start,limit)
        val call: Call<MessageListModel> = ApiClient.getClient.notificationList(notificationBody,"Bearer "+token)
        call.enqueue(object : Callback<MessageListModel>{
            override fun onFailure(call: Call<MessageListModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
            }
            override fun onResponse(call: Call<MessageListModel>, response: Response<MessageListModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {

                    messageArrayList.addAll(response.body()!!.responseArray!!.notificationList)
                    if (messageArrayList.size!=20)
                    {
                        stopLoading=true

                    }
                    else{
                        stopLoading=false
                    }

                    messageShowArrayList.addAll(messageArrayList)
                    if (messageShowArrayList.size>0)
                    {
                        messageRecycler.visibility=View.VISIBLE
                        val messageListAdapter = MessageListRecyclerAdapter(messageShowArrayList)
                        messageRecycler.setAdapter(messageListAdapter)
                        if(messageShowArrayList.size>20)
                        {
                            messageRecycler.scrollToPosition(start)
                        }

                        isLoading=false
                    }
                    else{
                        messageRecycler.visibility=View.GONE
                        showSuccessAlert(mContext,"No data found.","Alert")
                    }

                }
                else if(response.body()!!.status==116)
                {
                    var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                    if (internetCheck)
                    {
                        AccessTokenClass.getAccessToken(mContext)
                        callMessageListApi(start,limit)
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.status,mContext)
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




