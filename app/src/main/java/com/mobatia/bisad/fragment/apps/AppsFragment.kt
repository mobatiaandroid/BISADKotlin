package com.mobatia.bisad.fragment.apps

import android.annotation.SuppressLint
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
import com.mobatia.bisad.activity.apps.AppsDetailActivity
import com.mobatia.bisad.activity.communication.newsletter.NewsLetterDetailActivity
import com.mobatia.bisad.activity.communication.newsletter.adapter.NewsLetterRecyclerAdapter
import com.mobatia.bisad.activity.social_media.SocialMediaDetailActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.apps.adapter.AppsRecyclerAdapter
import com.mobatia.bisad.fragment.apps.model.AppsApiModel
import com.mobatia.bisad.fragment.apps.model.AppsListDetailModel
import com.mobatia.bisad.fragment.apps.model.AppsListModel
import com.mobatia.bisad.fragment.calendar.adapter.CalendarListRecyclerAdapter
import com.mobatia.bisad.fragment.calendar.model.CalendarApiModel
import com.mobatia.bisad.fragment.calendar.model.CalendarListModel
import com.mobatia.bisad.fragment.calendar.model.VEVENT
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.messages.adapter.MessageListRecyclerAdapter
import com.mobatia.bisad.fragment.messages.model.MessageListApiModel
import com.mobatia.bisad.fragment.messages.model.MessageListDetailModel
import com.mobatia.bisad.fragment.messages.model.MessageListModel
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

class AppsFragment  : Fragment() {
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var calendarRecycler: RecyclerView
    lateinit var progressDialog: RelativeLayout
    lateinit var titleTextView: TextView
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
    lateinit  var calendarArrayList : ArrayList<AppsListDetailModel>
    var start:Int=0
    var limit:Int=20
    var stopLoading:Boolean=false
    var isLoading:Boolean=false
    var apiCall:Int=0
    var apiCallDetail:Int=0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_apps, container, false)
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
            callStudentListApi()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(mContext)
        }


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
                    start=0
                    limit=20
                    calendarArrayList = ArrayList()
                    var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                    if (internetCheck)
                    {
                        callAppsList(start,limit)
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(mContext)
                    }

                }
                else
                {
                    if(response.body()!!.status == 116)
                    {
                        if(apiCall!=4)
                        {
                            apiCall=apiCall+1
                            AccessTokenClass.getAccessToken(mContext)
                            callStudentListApi()
                        }
                        else{
                            progressDialog.visibility=View.GONE
                            showSuccessAlert(mContext,"Something went wrong.Please try again later","Alert")

                        }


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
        titleTextView.text = "Apps"
        progressDialog.visibility=View.VISIBLE
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)

        studentSpinner.setOnClickListener {

            showStudentList(mContext, studentListArrayList)
        }
        calendarRecycler.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(@NonNull recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
            }

            override fun onScrolled(
                @NonNull recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager =
                    recyclerView.layoutManager as LinearLayoutManager?
                if (!isLoading) {
                    if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == calendarArrayList.size - 1) {
                        //bottom of list!
                        if (!stopLoading)
                        {
                            start=start+limit
                            progressDialog.visibility = View.VISIBLE
                            callAppsList(start,limit)
                            isLoading = true
                        }

                    }
                }
            }
        })
        calendarRecycler.addOnItemClickListener(object : OnItemClickListener {
            @SuppressLint("SimpleDateFormat", "SetTextI18n")
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onItemClicked(position: Int, view: View)
            {
                val intent =Intent(mContext, AppsDetailActivity::class.java)
                intent.putExtra("url",calendarArrayList.get(position).link)
                intent.putExtra("heading",calendarArrayList.get(position).name)
                startActivity(intent)
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
                start=0
                limit=20
                calendarArrayList = ArrayList()
                progressDialog.visibility = View.VISIBLE
                callAppsList(start,limit)
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    fun callAppsList(start:Int,limit:Int) {
        var appsShowArrayList=ArrayList<AppsListDetailModel>()
        progressDialog.visibility = View.VISIBLE
        val token = sharedprefs.getaccesstoken(mContext)
        val appsBody = AppsApiModel(studentId,start,limit)
        val call: Call<AppsListModel> = ApiClient.getClient.appsList(appsBody, "Bearer " + token)
        call.enqueue(object : Callback<AppsListModel> {
            override fun onFailure(call: Call<AppsListModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
            }

            override fun onResponse(
                call: Call<AppsListModel>,
                response: Response<AppsListModel>
            ) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status == 100) {
                    appsShowArrayList.addAll(response.body()!!.appsList)
                    calendarArrayList.addAll(appsShowArrayList)

                    if (appsShowArrayList.size==20)
                    {
                        stopLoading=false
                    }
                    else{
                        stopLoading=true
                    }

                    if (calendarArrayList.size>0)
                    {
                        calendarRecycler.visibility=View.VISIBLE
                        val calendarListAdapter = AppsRecyclerAdapter(calendarArrayList)
                        calendarRecycler.adapter = calendarListAdapter
                        isLoading=false
                        if(calendarArrayList.size>20)
                        {
                            if(calendarArrayList.size>20)
                            {
                                calendarRecycler.scrollToPosition(start)
                            }
                        }
                    }
                    else
                    {
                        calendarRecycler.visibility=View.GONE
                        showSuccessAlert(mContext,"No app is available.","Alert")

                    }

                }
                else if (response.body()!!.status==132)
                {
                    showSuccessAlert(mContext,"No app is available.","Alert")
                }
                else if (response.body()!!.status == 116)
                {
                    if (apiCallDetail!=4)
                    {
                        apiCallDetail=apiCallDetail+1
                        AccessTokenClass.getAccessToken(mContext)
                        callAppsList(start,limit)
                    }
                    else{
                        progressDialog.visibility=View.GONE
                        showSuccessAlert(mContext,"Something went wrong.Please try again later","Alert")
                    }

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
