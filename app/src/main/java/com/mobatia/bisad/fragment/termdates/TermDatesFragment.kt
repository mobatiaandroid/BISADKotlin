package com.mobatia.bisad.fragment.termdates

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.NonNull
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
import com.mobatia.bisad.activity.term_dates.TermDatesDetailActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.home.loader
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.messages.adapter.MessageListRecyclerAdapter
import com.mobatia.bisad.fragment.messages.model.MessageListApiModel
import com.mobatia.bisad.fragment.messages.model.MessageListDetailModel
import com.mobatia.bisad.fragment.messages.model.MessageListModel
import com.mobatia.bisad.fragment.report_absence.adapter.RequestAbsenceRecyclerAdapter
import com.mobatia.bisad.fragment.report_absence.model.AbsenceLeaveApiModel
import com.mobatia.bisad.fragment.report_absence.model.AbsenceListModel
import com.mobatia.bisad.fragment.report_absence.model.AbsenceRequestListDetailModel
import com.mobatia.bisad.fragment.student_information.adapter.StudentListAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
import com.mobatia.bisad.fragment.termdates.adapter.TermDatesRecyclerAdapter
import com.mobatia.bisad.fragment.termdates.model.TermDatesApiModel
import com.mobatia.bisad.fragment.termdates.model.TermDatesListDetailModel
import com.mobatia.bisad.fragment.termdates.model.TermDatesListModel
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TermDatesFragment : Fragment(){
    lateinit var jsonConstans: JsonConstants
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var sharedprefs: PreferenceData
    lateinit var mTermDatesRecycler: RecyclerView
    lateinit var progressDialog: RelativeLayout
    lateinit var mContext: Context
    lateinit var termDatesArrrayList : ArrayList<TermDatesListDetailModel>
     var start:Int=0
    var limit:Int=15
    var isLoading:Boolean=false
    var stopLoading:Boolean=false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_term_dates, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        initializeUI()
        start=0
        limit=20
        callTermDatesListAPI(start,limit)

    }

    private fun initializeUI() {
        termDatesArrrayList=ArrayList()
        mTermDatesRecycler = view!!.findViewById(R.id.mTermDatesRecycler) as RecyclerView
        linearLayoutManager = LinearLayoutManager(mContext)
        mTermDatesRecycler.layoutManager = linearLayoutManager
        mTermDatesRecycler.itemAnimator = DefaultItemAnimator()
        mTermDatesRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val intent = Intent(activity, TermDatesDetailActivity::class.java)
                intent.putExtra("id",termDatesArrrayList.get(position).id)
                intent.putExtra("title",termDatesArrrayList.get(position).title)
                activity?.startActivity(intent)
            }
        })


        mTermDatesRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(
                @NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == termDatesArrrayList.size - 1) {
                        //bottom of list!
                        if (!stopLoading)
                        {
                            start=start+limit
                            callTermDatesListAPI(start,limit)
                            isLoading = true
                        }

                    }
                }
            }
        })
        progressDialog = view!!.findViewById(R.id.progressDialog) as RelativeLayout
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
    }



    fun callTermDatesListAPI(startValue:Int,limitValue:Int)
    {
        var termList=ArrayList<TermDatesListDetailModel>()
        progressDialog.visibility = View.VISIBLE
        val token = sharedprefs.getaccesstoken(mContext)
        val termsDatesBody= TermDatesApiModel(startValue,limitValue)
        val call: Call<TermDatesListModel> = ApiClient.getClient.termDatesList(termsDatesBody,"Bearer "+token)
        call.enqueue(object : Callback<TermDatesListModel>{
            override fun onFailure(call: Call<TermDatesListModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<TermDatesListModel>, response: Response<TermDatesListModel>) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status==100)
                {
                    termList.addAll(response.body()!!.responseArray!!.termDatesList)
                    termDatesArrrayList.addAll(termList)
                    if (termList.size==20)
                    {
                        stopLoading=false
                    }
                    else{
                        stopLoading=true
                    }
                  if (termDatesArrrayList.size>0)
                  {
                      mTermDatesRecycler.visibility=View.VISIBLE
                      val termDatesAdapter = TermDatesRecyclerAdapter(termDatesArrrayList)
                      mTermDatesRecycler.setAdapter(termDatesAdapter)
                      if(termDatesArrrayList.size>20)
                      {
                          mTermDatesRecycler.scrollToPosition(startValue)
                      }

                      isLoading=false
                  }
                    else
                  {
                      mTermDatesRecycler.visibility=View.GONE
                      showSuccessAlert(mContext,"No data found.","Alert")
                  }

                }
                else if(response.body()!!.status==116)
                {
                    AccessTokenClass.getAccessToken(mContext)
                    callTermDatesListAPI(startValue,limitValue)
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




