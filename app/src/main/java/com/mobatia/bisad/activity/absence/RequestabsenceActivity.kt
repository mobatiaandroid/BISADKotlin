package com.mobatia.bisad.activity.absence

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.absence.model.RequestAbsenceApiModel
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.messages.model.MessageListApiModel
import com.mobatia.bisad.fragment.report_absence.model.AbsenceLeaveApiModel
import com.mobatia.bisad.fragment.student_information.adapter.StudentListAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RequestabsenceActivity : AppCompatActivity(){
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    lateinit var studentNameTxt: TextView
    lateinit var enterStratDate: TextView
    lateinit var enterEndDate: TextView
    lateinit var submitBtn: Button
    lateinit var enterMessage: EditText
    lateinit var studImg: ImageView
    lateinit var studentName: String
    lateinit var studentId: String
    lateinit var studentImg: String
    lateinit var studentClass: String
     var fromDate: String=""
     var toDate: String =""
    lateinit var fromDateAPI:String
    lateinit var toDateAPI:String
    lateinit var reasonAPI:String
    lateinit var studentSpinner: LinearLayout
    lateinit var submitLayout: LinearLayout
    lateinit var studentArrayList :ArrayList<StudentList>
    lateinit var myCalendar :Calendar
    lateinit var currentDate:Date
    lateinit var sdate:Date
    lateinit var edate:Date
    var elapsedDays:Long = 0
    private lateinit var relativeHeader: RelativeLayout
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    lateinit var progressDialog: RelativeLayout
    lateinit var backRelative: RelativeLayout
    override fun onCreate(savedInstanceState: Bundle?)
    { super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_request_absence)
        mContext=this
        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()

        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            callStudentListApi()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }
        initUI()

    }
    fun initUI()
    {
        myCalendar= Calendar.getInstance()
        currentDate=Calendar.getInstance().time
        studentNameTxt = findViewById<TextView>(R.id.studentName)
        enterStratDate = findViewById<TextView>(R.id.enterStratDate)
        enterEndDate = findViewById<TextView>(R.id.enterEndDate)
        studImg = findViewById<ImageView>(R.id.studImg)
        enterMessage = findViewById<EditText>(R.id.enterMessage)
        relativeHeader = findViewById(R.id.relativeHeader)
        studentSpinner = findViewById(R.id.studentSpinner) as LinearLayout
        submitLayout = findViewById(R.id.submitLayout) as LinearLayout
        submitBtn = findViewById(R.id.submitBtn) as Button
        relativeHeader = findViewById(R.id.relativeHeader)
        backRelative = findViewById(R.id.backRelative)
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        progressDialog = findViewById(R.id.progressDialog) as RelativeLayout
        heading.setText("Absence")
        btn_left.setOnClickListener(View.OnClickListener {
            finish()
        })
        backRelative.setOnClickListener(View.OnClickListener {
            finish()
        })
        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })

        studentSpinner.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                showStudentList(mContext,studentArrayList)
            }
        })
        submitBtn.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {

                if(enterStratDate.text.equals(""))
                {
                    InternetCheckClass. showErrorAlert(mContext,"Please select First day of absence","Alert")
                }
                else{
                    if(enterEndDate.text.equals(""))
                    {
                        InternetCheckClass. showErrorAlert(mContext,"Please select Return to school","Alert")
                    }
                    else{
                        if(enterMessage.text.toString().trim().equals(""))
                        {
                            InternetCheckClass. showErrorAlert(mContext,"Please enter reason for your absence","Alert")
                        }
                        else{
                            val aniRotate: Animation =
                                AnimationUtils.loadAnimation(com.mobatia.bisad.fragment.home.mContext, R.anim.linear_interpolator)
                            progressDialog.startAnimation(aniRotate)
                                reasonAPI=enterMessage.text.toString().trim()
                               Log.e("Pass Value",fromDate+"  "+toDate+"   "+reasonAPI)
                            var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                            if (internetCheck)
                            {
                                callAbsenceSubmitApi(fromDate,toDate,reasonAPI)
                            }
                            else{
                                InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                            }

                        }
                    }
                }
            }
        })
        val date = OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            fromDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
            if (toDate != "") {
                val dateFormatt =
                    SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                try {
                    sdate = dateFormatt.parse(fromDate)
                    edate = dateFormatt.parse(toDate)
                    printDifference(sdate, edate)
                } catch (e: Exception) {
                }
            }
            if(elapsedDays < 0 && toDate != "")
            {
                InternetCheckClass. showErrorAlert(mContext,"Choose first day of absence(date) less than or equal to selected return to school(date)","Alert")

            }
            else
            {
                updateLabel()
            }

        }
        val dateEnd = OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
            myCalendar[Calendar.YEAR] = year
            myCalendar[Calendar.MONTH] = monthOfYear
            myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
            toDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
            if (toDate != "") {
                val dateFormatt =
                    SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
                try {
                    sdate = dateFormatt.parse(fromDate)
                    edate = dateFormatt.parse(toDate)
                    printDifference(sdate, edate)
                } catch (e: java.lang.Exception) {
                }
                if (elapsedDays < 0) {
                    InternetCheckClass. showErrorAlert(mContext,"Choose return to school(date) greater than or equal to first day of absence(date)","Alert")
                } else {
                    updateLabelEnd()
                }
            }
        }
        enterStratDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                DatePickerDialog(
                    mContext, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
                ).show()
            }
        })
        enterEndDate.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                DatePickerDialog(
                    mContext, dateEnd, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
                ).show()
            }
        })

    }
   fun printDifference(startDate :Date,endDate:Date)
   {
       var different = endDate.time - startDate.time
       val secondsInMilli: Long = 1000
       val minutesInMilli = secondsInMilli * 60
       val hoursInMilli = minutesInMilli * 60
       val daysInMilli = hoursInMilli * 24
       elapsedDays = different / daysInMilli
       different = different % daysInMilli
       val elapsedHours = different / hoursInMilli
       different = different % hoursInMilli
       val elapsedMinutes = different / minutesInMilli
       different = different % minutesInMilli
       val elapsedSeconds = different / secondsInMilli


   }
    fun updateLabel()
    {
        val myFormat = "dd MMMM yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        enterStratDate.setText(sdf.format(myCalendar.getTime()))
    }
    fun updateLabelEnd()
    {
        val myFormat = "dd MMMM yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        enterEndDate.setText(sdf.format(myCalendar.getTime()))
    }
    fun callStudentListApi()
    {
        studentArrayList=ArrayList<StudentList>()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<StudentListModel> = ApiClient.getClient.studentList("Bearer "+token)
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<StudentListModel>, response: Response<StudentListModel>) {
                val arraySize :Int =response.body()!!.responseArray!!.studentList.size
                if (response.body()!!.status==100)
                {
                    studentArrayList.addAll(response.body()!!.responseArray!!.studentList)

                    if (sharedprefs.getStudentID(mContext).equals(""))
                    {
                        Log.e("Empty Img","Empty")
                        studentName=studentArrayList.get(0).name
                        studentImg=studentArrayList.get(0).photo
                        studentId=studentArrayList.get(0).id
                        studentClass=studentArrayList.get(0).section
                        sharedprefs.setStudentID(mContext,studentId)
                        sharedprefs.setStudentName(mContext,studentName)
                        sharedprefs.setStudentPhoto(mContext,studentImg)
                        sharedprefs.setStudentClass(mContext,studentClass)
                        studentNameTxt.text=studentName
                        if(!studentImg.equals(""))
                        {
                            Glide.with(mContext) //1
                                .load(studentImg)
                                .placeholder(R.drawable.student)
                                .error(R.drawable.student)
                                .skipMemoryCache(true) //2
                                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                                .transform(CircleCrop()) //4
                                .into(studImg)
                        }
                        else{
                            studImg.setImageResource(R.drawable.student)
                        }

                    }
                    else{
                        studentName= sharedprefs.getStudentName(mContext)!!
                        studentImg= sharedprefs.getStudentPhoto(mContext)!!
                        studentId= sharedprefs.getStudentID(mContext)!!
                        studentClass= sharedprefs.getStudentClass(mContext)!!
                        studentNameTxt.text=studentName
                        if(!studentImg.equals(""))
                        {
                            Glide.with(mContext) //1
                                .load(studentImg)
                                .placeholder(R.drawable.student)
                                .error(R.drawable.student)
                                .skipMemoryCache(true) //2
                                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                                .transform(CircleCrop()) //4
                                .into(studImg)
                        }
                        else{
                            studImg.setImageResource(R.drawable.student)
                        }
                    }
                }
            }
        })
    }


    fun showStudentList(context: Context ,mStudentList : ArrayList<StudentList>)
    {
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
        studentListRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                studentName=studentArrayList.get(position).name
                studentImg=studentArrayList.get(position).photo
                studentId=studentArrayList.get(position).id
                studentClass=studentArrayList.get(position).section
                sharedprefs.setStudentID(mContext,studentId)
                sharedprefs.setStudentName(mContext,studentName)
                sharedprefs.setStudentPhoto(mContext,studentImg)
                sharedprefs.setStudentClass(mContext,studentClass)
                studentNameTxt.text=studentName
                if(!studentImg.equals(""))
                {
                    Glide.with(mContext) //1
                        .load(studentImg)
                        .placeholder(R.drawable.student)
                        .error(R.drawable.student)
                        .skipMemoryCache(true) //2
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                        .transform(CircleCrop()) //4
                        .into(studImg)
                }
                else{
                    studImg.setImageResource(R.drawable.student)
                }
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    //Signup API Call
    fun callAbsenceSubmitApi(from:String,toDate:String,reason:String)
    {
        val token = sharedprefs.getaccesstoken(mContext)
        val requestLeaveBody= RequestAbsenceApiModel(sharedprefs.getStudentID(mContext)!!,from,toDate,reason)
        val call: Call<ResponseBody> = ApiClient.getClient.leaveRequest(requestLeaveBody,"Bearer "+token)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)
                progressDialog.visibility = View.GONE
            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responsedata = response.body()
                progressDialog.visibility = View.GONE
                Log.e("Response Signup", responsedata.toString())
                if (responsedata != null) {
                    try {

                        val jsonObject = JSONObject(responsedata.string())
                        if(jsonObject.has(jsonConstans.STATUS)) {
                            val status: Int = jsonObject.optInt(jsonConstans.STATUS)
                            Log.e("STATUS LOGIN", status.toString())
                            if (status == 100) {
                                showErrorAlert(mContext,"Successfully submitted your leave request.","Success")

                            } else {
                                if (status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                                    callAbsenceSubmitApi(from,toDate,reason)
                                } else {
                                    if (status == 103) {
                                        //validation check error
                                    } else {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status, mContext)
                                    }
                                }

                            }
                        }
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }

    fun showErrorAlert(context: Context,message : String,msgHead : String)
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
        btn_Ok?.setOnClickListener()
        {
            dialog.dismiss()
            finish()
        }
        dialog.show()
    }
}

