package com.mobatia.bisad.activity.calendar

import android.annotation.SuppressLint
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
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.calendar.adapter.CalendarDetailAdapter
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.calendar.model.CalendarApiModel
import com.mobatia.bisad.fragment.calendar.model.CalendarListModel
import com.mobatia.bisad.fragment.calendar.model.VEVENT
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

class CalendarActivity : AppCompatActivity(){
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    var id:String=""
    var title:String=""

    private lateinit var relativeHeader: RelativeLayout
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    lateinit var progressDialog: RelativeLayout

    lateinit var calendarRecycler: RecyclerView
    lateinit var outputDateStrstart: String
    lateinit var outputDateStrend: String
    lateinit var SummaryCalendar: String
    lateinit var DescriptionCalendar: String

    var startTime16format: Long = 0
    var endTime16format: Long = 0
    var startTime8format: Long = 0
    var endTime8format: Long = 0

    lateinit var StartCalendar: String
    lateinit var EndCalendar: String
    lateinit var dialogcalendar: Dialog
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit  var calendarArrayList : ArrayList<VEVENT>

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calendar_detail)
        mContext=this
        id=intent.getStringExtra("id")
        title=intent.getStringExtra("title")
        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()
        initUI()
        callCalendarListApi()

    }
    fun initUI() {
        relativeHeader = findViewById(R.id.relativeHeader)
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        heading.setText(title)
        btn_left.setOnClickListener(View.OnClickListener {
            finish()
        })

        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })

        progressDialog = findViewById(R.id.progressDialog) as RelativeLayout
        calendarRecycler = findViewById(R.id.calendarRecycler) as RecyclerView
        linearLayoutManager = LinearLayoutManager(mContext)
        calendarRecycler.layoutManager = linearLayoutManager
        calendarRecycler.itemAnimator = DefaultItemAnimator()
        progressDialog.visibility= View.VISIBLE
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        calendarRecycler.addOnItemClickListener(object : OnItemClickListener {
            @SuppressLint("SimpleDateFormat", "SetTextI18n")
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemClicked(position: Int, view: View) {
                dialogcalendar = Dialog(mContext)
                dialogcalendar.requestWindowFeature(Window.FEATURE_NO_TITLE)
                dialogcalendar.setCancelable(false)
                // dialog.window!!.setLayout(1200,1400)
                dialogcalendar.setContentView(R.layout.calendar_popup)
                val summary = dialogcalendar.findViewById(R.id.summary) as TextView
                val close_popup = dialogcalendar.findViewById(R.id.close_popup) as ImageView
                val description = dialogcalendar.findViewById(R.id.description) as TextView
                val startcalendar = dialogcalendar.findViewById(R.id.start_date) as TextView
                val endcalendar = dialogcalendar.findViewById(R.id.end_date) as TextView
                val start_text = dialogcalendar.findViewById(R.id.start_text) as TextView
                val end_text = dialogcalendar.findViewById(R.id.end_text) as TextView
                val save_calendar = dialogcalendar.findViewById(R.id.save_calendar) as Button
                SummaryCalendar = calendarArrayList[position].SUMMARY
                DescriptionCalendar = calendarArrayList[position].DESCRIPTION
                StartCalendar = calendarArrayList[position].DTSTART
                EndCalendar = calendarArrayList[position].DTEND
                summary.text = SummaryCalendar
                description.visibility= View.GONE
                description.text = DescriptionCalendar
                start_text.setTextColor(mContext?.let {
                    ContextCompat.getColor(
                        it,
                        R.color.rel_one
                    )
                }!!)
                end_text.setTextColor(mContext?.let {
                    ContextCompat.getColor(
                        it,
                        R.color.rel_one
                    )
                }!!);

                if (StartCalendar.length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")

                    val startdate: Date = inputFormat.parse(calendarArrayList[position].DTSTART)
                    val enddate: Date = inputFormat.parse(calendarArrayList[position].DTEND)

                    outputDateStrstart = outputFormat.format(startdate)
                    outputDateStrend = outputFormat.format(enddate)

                    startcalendar.text = outputDateStrstart
                    endcalendar.text = outputDateStrend
                }

                if (StartCalendar.length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")

                    val startdate: Date = inputFormat.parse(calendarArrayList[position].DTSTART)
                    val enddate: Date = inputFormat.parse(calendarArrayList[position].DTEND)

                    outputDateStrstart = outputFormat.format(startdate)
                    outputDateStrend = outputFormat.format(enddate)

                    startcalendar.text = outputDateStrstart
                    endcalendar.text = outputDateStrend
                }
                save_calendar.setOnClickListener {

                    val calendar = Calendar.getInstance()
                    calendar.timeZone = TimeZone.getDefault()

                    if (StartCalendar.length == 16) {
                        try {
                            val startdatehelper =
                                SimpleDateFormat("MMM dd,yyyy hh:mm a").parse(outputDateStrstart)
                            val stopdatehelper =
                                SimpleDateFormat("MMM dd,yyyy hh:mm a").parse(outputDateStrend)

                            startTime16format = startdatehelper.time
                            endTime16format = stopdatehelper.time
                        } catch (e: Exception) {
                        }

                        val intent = Intent(Intent.ACTION_EDIT)
                        intent.type = "vnd.android.cursor.item/event"
                        intent.putExtra("beginTime", startTime16format)
                        intent.putExtra("allDay", false)
                        intent.putExtra("rule", "FREQ=YEARLY")
                        intent.putExtra("endTime", endTime16format)
                        intent.putExtra("title", SummaryCalendar)
                        startActivity(intent)
                        dialogcalendar.dismiss()
                    }

                    if (StartCalendar.length == 8) {
                        try {
                            val startdatehelper =
                                SimpleDateFormat("MMM dd,yyyy").parse(outputDateStrstart)
                            val stopdatehelper =
                                SimpleDateFormat("MMM dd,yyyy").parse(outputDateStrend)

                            startTime8format = startdatehelper.time
                            endTime8format = stopdatehelper.time
                        } catch (e: Exception) {
                        }

                        val intent = Intent(Intent.ACTION_EDIT)
                        intent.type = "vnd.android.cursor.item/event"
                        intent.putExtra("beginTime", startTime8format)
                        intent.putExtra("allDay", false)
                        intent.putExtra("rule", "FREQ=YEARLY")
                        intent.putExtra("endTime", endTime8format)
                        intent.putExtra("title", SummaryCalendar)
                        startActivity(intent)
                        dialogcalendar.dismiss()
                    }


                }

                if (DescriptionCalendar.length < 5) {
                    description.visibility = View.GONE

                }

                dialogcalendar.show()

                close_popup.setOnClickListener {
                    dialogcalendar.dismiss()
                }
            }


        })
    }

    fun callCalendarListApi() {
        calendarArrayList = ArrayList()
        progressDialog.visibility = View.VISIBLE
        val token = sharedprefs.getaccesstoken(mContext)
        val calendarBody = CalendarApiModel(id)
        val call: Call<CalendarListModel> = ApiClient.getClient.calendarDetail(calendarBody, "Bearer " + token)
        call.enqueue(object : Callback<CalendarListModel> {
            override fun onFailure(call: Call<CalendarListModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<CalendarListModel>,
                response: Response<CalendarListModel>
            ) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status == 100) {
                    calendarArrayList.addAll(response.body()!!.responseArray.calendarDetail.cal.VEVENT)
                    if (calendarArrayList.size>0)
                    {
                        calendarRecycler.visibility= View.VISIBLE
                        val calendarListAdapter = CalendarDetailAdapter(calendarArrayList)
                        calendarRecycler.adapter = calendarListAdapter

                    }
                    else
                    {
                        calendarRecycler.visibility= View.GONE
                        showSuccessAlert(mContext,"No data found.","Alert")

                    }

                } else if (response.body()!!.status == 116) {
                    AccessTokenClass.getAccessToken(mContext)
                    callCalendarListApi()
                }
                else {
                    InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
                }
            }

        })
    }

    fun showSuccessAlert(context: Context, message : String, msgHead : String)
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

