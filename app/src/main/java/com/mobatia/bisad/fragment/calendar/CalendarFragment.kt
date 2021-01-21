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
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.calendar.adapter.CalendarListRecyclerAdapter
import com.mobatia.bisad.fragment.calendar.model.CalendarApiModel
import com.mobatia.bisad.fragment.calendar.model.CalendarListModel
import com.mobatia.bisad.fragment.calendar.model.VEVENT
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

    lateinit var outputDateStrstart: String
    lateinit var outputDateStrend: String
    lateinit var SummaryCalendar: String
    lateinit var DescriptionCalendar: String

    lateinit var titleTextView: TextView

    var startTime16format: Long = 0
    var endTime16format: Long = 0
    var startTime8format: Long = 0
    var endTime8format: Long = 0

    lateinit var StartCalendar: String
    lateinit var EndCalendar: String
    lateinit var dialogcalendar: Dialog
    lateinit var studentName: String
    lateinit var studentId: String
    lateinit var studentImg: String
    lateinit var studentClass: String
    lateinit var studentSpinner: LinearLayout
    lateinit var studImg: ImageView
    lateinit var studentNameTxt: TextView
    lateinit var mContext: Context
    var studentListArrayList = ArrayList<StudentList>()
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit  var calendarArrayList : ArrayList<VEVENT>
    var isLoading:Boolean = false
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
        callStudentListApi()

    }

    fun callStudentListApi()
    {
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<StudentListModel> = ApiClient.getClient.studentList("Bearer " + token)
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
            }

            override fun onResponse(
                call: Call<StudentListModel>,
                response: Response<StudentListModel>
            ) {
              //  val arraySize: Int = response.body()!!.responseArray!!.studentList.size
                if (response.body()!!.status == 100) {

                    studentListArrayList.addAll(response.body()!!.responseArray!!.studentList)
                    if (sharedprefs.getStudentID(mContext).equals("")) {
                        studentName = studentListArrayList.get(0).name
                        studentImg = studentListArrayList.get(0).photo
                        studentId = studentListArrayList.get(0).id
                        studentClass = studentListArrayList.get(0).section
                        sharedprefs.setStudentID(mContext, studentId)
                        sharedprefs.setStudentName(mContext, studentName)
                        sharedprefs.setStudentPhoto(mContext, studentImg)
                        sharedprefs.setStudentClass(mContext, studentClass)
                        studentNameTxt.text = studentName
                        if (!studentImg.equals("")) {
                            Glide.with(mContext) //1
                                .load(studentImg)
                                .placeholder(R.drawable.student)
                                .error(R.drawable.student)
                                .skipMemoryCache(true) //2
                                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                                .transform(CircleCrop()) //4
                                .into(studImg)
                        } else {
                            studImg.setImageResource(R.drawable.student)

                        }

                    } else {
                        studentName = sharedprefs.getStudentName(mContext)!!
                        studentImg = sharedprefs.getStudentPhoto(mContext)!!
                        studentId = sharedprefs.getStudentID(mContext)!!
                        studentClass = sharedprefs.getStudentClass(mContext)!!
                        studentNameTxt.text = studentName
                        if (!studentImg.equals("")) {
                            Glide.with(mContext) //1
                                .load(studentImg)
                                .placeholder(R.drawable.student)
                                .error(R.drawable.student)
                                .skipMemoryCache(true) //2
                                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                                .transform(CircleCrop()) //4
                                .into(studImg)
                        } else {
                            studImg.setImageResource(R.drawable.student)
                        }


                    }
                    callCalendarListApi()
                }
                else
                {
                    if(response.body()!!.status == 116)
                    {
                        AccessTokenClass.getAccessToken(mContext)
                        callStudentListApi()
                    }
                }


            }

        })
    }

    private fun initializeUI()
    {
        calendarRecycler = view!!.findViewById(R.id.calendarRecycler) as RecyclerView
        progressDialog = view!!.findViewById(R.id.progressDialog) as RelativeLayout
        studentSpinner = view!!.findViewById(R.id.studentSpinner) as LinearLayout
        studImg = view!!.findViewById(R.id.studImg) as ImageView
        studentNameTxt = view!!.findViewById(R.id.studentName) as TextView
        titleTextView = view!!.findViewById(R.id.titleTextView) as TextView
        linearLayoutManager = LinearLayoutManager(mContext)
        calendarRecycler.layoutManager = linearLayoutManager
        calendarRecycler.itemAnimator = DefaultItemAnimator()

        titleTextView.text = "Calendar"

        progressDialog.visibility=View.VISIBLE
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)

        studentSpinner.setOnClickListener {

            showStudentList(mContext, studentListArrayList)
        }

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
                description.text = DescriptionCalendar
                start_text.setTextColor(context?.let {
                    ContextCompat.getColor(
                        it,
                        R.color.rel_one
                    )
                }!!)
                end_text.setTextColor(context?.let {
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

    fun showStudentList(context: Context, mStudentList: ArrayList<StudentList>) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialogue_student_list)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var btn_dismiss = dialog.findViewById(R.id.btn_dismiss) as Button
        var studentListRecycler = dialog.findViewById(R.id.studentListRecycler) as RecyclerView
        iconImageView.setImageResource(R.drawable.boy)
        //if(mSocialMediaArray.get())
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            btn_dismiss.setBackgroundDrawable(
                mContext.resources.getDrawable(R.drawable.button_new)
            )
        } else {
            btn_dismiss.setBackground(mContext.resources.getDrawable(R.drawable.button_new))
        }

        studentListRecycler.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        studentListRecycler.setLayoutManager(llm)
        val studentAdapter = StudentListAdapter(mContext,mStudentList)
        studentListRecycler.setAdapter(studentAdapter)
        btn_dismiss?.setOnClickListener()
        {
            dialog.dismiss()
        }
        studentListRecycler.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                studentName = studentListArrayList.get(position).name
                studentImg = studentListArrayList.get(position).photo
                studentId = studentListArrayList.get(position).id
                studentClass = studentListArrayList.get(position).section
                sharedprefs.setStudentID(mContext, studentId)
                sharedprefs.setStudentName(mContext, studentName)
                sharedprefs.setStudentPhoto(mContext, studentImg)
                sharedprefs.setStudentClass(mContext, studentClass)
                studentNameTxt.text = studentName
                if (!studentImg.equals("")) {
                    Glide.with(mContext) //1
                        .load(studentImg)
                        .placeholder(R.drawable.student)
                        .error(R.drawable.student)
                        .skipMemoryCache(true) //2
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                        .transform(CircleCrop()) //4
                        .into(studImg)
                } else {
                    studImg.setImageResource(R.drawable.student)
                }
                progressDialog.visibility=View.VISIBLE

                callCalendarListApi()
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    fun callCalendarListApi() {
        calendarArrayList = ArrayList()
        progressDialog.visibility = View.VISIBLE
        val token = sharedprefs.getaccesstoken(mContext)
        val calendarBody = CalendarApiModel(studentId)
        val call: Call<CalendarListModel> = ApiClient.getClient.calendarList(calendarBody, "Bearer " + token)
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
                        calendarRecycler.visibility=View.VISIBLE
                        val calendarListAdapter = CalendarListRecyclerAdapter(calendarArrayList)
                        calendarRecycler.adapter = calendarListAdapter

                    }
                    else
                    {
                        calendarRecycler.visibility=View.GONE
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
