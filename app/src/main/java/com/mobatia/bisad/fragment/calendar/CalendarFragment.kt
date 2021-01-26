package com.mobatia.bisad.fragment.calendar

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.calendar.CalendarActivity
import com.mobatia.bisad.activity.communication.newsletter.NewsLetterDetailActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.calendar.adapter.CalendarListRecyclerAdapter
import com.mobatia.bisad.fragment.calendar.model.CalendarListResponse
import com.mobatia.bisad.fragment.calendar_new.model.CalendarModel

import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.student_information.adapter.StudentListAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragment : Fragment() {
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var calendarRecycler: RecyclerView
    lateinit var progressDialog: RelativeLayout
    lateinit var titleTextView: TextView
    lateinit var mContext: Context
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit  var calendarArrayList : ArrayList<CalendarListResponse>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_calendar, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        initializeUI()
        //callCalendarListApi()
    }
    private fun initializeUI()
    {
        calendarRecycler = view!!.findViewById(R.id.calendarRecycler) as RecyclerView
        progressDialog = view!!.findViewById(R.id.progressDialog) as RelativeLayout
        titleTextView = view!!.findViewById(R.id.titleTextView) as TextView
        linearLayoutManager = LinearLayoutManager(mContext)
        calendarRecycler.layoutManager = linearLayoutManager
        calendarRecycler.itemAnimator = DefaultItemAnimator()
        titleTextView.text = "Calendar"
        progressDialog.visibility=View.VISIBLE
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        calendarRecycler.addOnItemClickListener(object : OnItemClickListener {
            @SuppressLint("SimpleDateFormat", "SetTextI18n")
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemClicked(position: Int, view: View) {

                val intent =Intent(mContext, CalendarActivity::class.java)
                intent.putExtra("id",calendarArrayList.get(position).id)
                intent.putExtra("title",calendarArrayList.get(position).title)
                startActivity(intent)
            }
        })
    }

//    fun callCalendarListApi()
//    {
//        calendarArrayList = ArrayList()
//        progressDialog.visibility = View.VISIBLE
//        val token = sharedprefs.getaccesstoken(mContext)
//        val call: Call<CalendarModel> = ApiClient.getClient.calendarList("Bearer " + token)
//        call.enqueue(object : Callback<CalendarModel> {
//            override fun onFailure(call: Call<CalendarModel>, t: Throwable) {
//                progressDialog.visibility = View.GONE
//            }
//            override fun onResponse(
//                call: Call<CalendarModel>,
//                response: Response<CalendarModel>
//            ) {
//                progressDialog.visibility = View.GONE
//                if (response.body()!!.status == 100) {
//                    calendarArrayList.addAll(response.body()!!.calendarList)
//                    if (calendarArrayList.size>0)
//                    {
//                        calendarRecycler.visibility=View.VISIBLE
//                        val calendarListAdapter = CalendarListRecyclerAdapter(calendarArrayList)
//                        calendarRecycler.adapter = calendarListAdapter
//
//                    }
//                    else
//                    {
//                        calendarRecycler.visibility=View.GONE
//                        showSuccessAlert(mContext,"No data found.","Alert")
//
//                    }
//
//                } else if (response.body()!!.status == 116) {
//                    AccessTokenClass.getAccessToken(mContext)
//                    callCalendarListApi()
//                }
//                else {
//                    InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
//                }
//            }
//
//        })
//    }


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
