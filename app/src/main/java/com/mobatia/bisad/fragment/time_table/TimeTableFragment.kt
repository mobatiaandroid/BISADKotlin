package com.mobatia.bisad.fragment.time_table

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.Dialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.BounceInterpolator
import android.widget.*
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.downloader.OnDownloadListener
import com.downloader.PRDownloader
import com.github.barteksc.pdfviewer.PDFView
import com.mobatia.bisad.R
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.student_information.adapter.StudentListAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
import com.mobatia.bisad.fragment.time_table.adapter.TimeTableAllWeekSelectionAdapterNew
import com.mobatia.bisad.fragment.time_table.adapter.TimeTableSingleWeekSelectionAdapter
import com.mobatia.bisad.fragment.time_table.adapter.TimeTableWeekListAdapter
import com.mobatia.bisad.fragment.time_table.model.apimodel.*
import com.mobatia.bisad.fragment.time_table.model.usagemodel.*
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import com.ryanharter.android.tooltips.ToolTipLayout
import kotlinx.android.synthetic.main.fragment_time_table.*
import kotlinx.android.synthetic.main.fragment_timetablenew.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
class TimeTableFragment : Fragment() {
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    var weekListArrayString = ArrayList<String>()
    lateinit var weekListArray: ArrayList<WeekModel>
    lateinit var studentName: String
    lateinit var studentId: String
    lateinit var studentImg: String
    lateinit var studentClass: String
    lateinit var studentSpinner: LinearLayout
    lateinit var studImg: ImageView
    lateinit var studentNameTxt: TextView
    lateinit var noDataImg: ImageView
    lateinit var tipContainer: ToolTipLayout
    var studentListArrayList = ArrayList<StudentList>()
    var mRangeModel = ArrayList<RangeModel>()
    var mPeriodModel = ArrayList<PeriodModel>()
    var mFieldModel = ArrayList<FieldModel>()
    var breakArrayList = ArrayList<String>()
    var mThursdayModelArraylist = ArrayList<DayModel>()
    private val STORAGE_PERMISSION_CODE: Int = 1000
    var title:String = "timetable"


    //Api ArrayList
    var feildAPIArrayList = ArrayList<FieldApiListModel>()
    var mRangeAPIArrayList = ArrayList<RangeApiModel>()
    var mSundayArrayList = ArrayList<TimeTableApiListModel>()
    var mMondayArrayList = ArrayList<TimeTableApiListModel>()
    var mTuesdayArrayList = ArrayList<TimeTableApiListModel>()
    var mWednesdayArrayList = ArrayList<TimeTableApiListModel>()
    var mThursdayArrayList = ArrayList<TimeTableApiListModel>()
    var mTimetableApiArrayList = ArrayList<TimeTableApiListModel>()
    lateinit var timeTableWeekListAdapter: TimeTableWeekListAdapter
    var weekPosition: Int = 0
    lateinit var dayOfTheWeek: String
    lateinit var mContext: Context
    private lateinit var linearLayoutManager: LinearLayoutManager
    private lateinit var linearLayoutManagerVertical: LinearLayoutManager
    private lateinit var linearLayoutManagerVertical1: LinearLayoutManager
    lateinit var weekRecyclerList: RecyclerView
    lateinit var timeTableSingleRecycler: RecyclerView
    lateinit var timeTableAllRecycler: RecyclerView
    lateinit var primary_pdf: PDFView
    lateinit var non_primarylinear: LinearLayout
    lateinit var card_viewAll: CardView
    lateinit var progressDialog: RelativeLayout
    var pdfdata: String = ""
    lateinit var sharedownloadlinear: LinearLayout
    lateinit var downloadpdf: ImageView
    lateinit var sharepdf: ImageView
    var apiCall: Int = 0
    var apiCallDetail: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_timetablenew, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        weekListArrayString.add("ALL")
        weekListArrayString.add("SUNDAY")
        weekListArrayString.add("MONDAY")
        weekListArrayString.add("TUESDAY")
        weekListArrayString.add("WEDNESDAY")
        weekListArrayString.add("THURSDAY")
        initializeUI()
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck) {
            callStudentListApi()
        } else {
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }

    }

    @SuppressLint("WrongViewCast")
    private fun initializeUI() {
        weekListArray = ArrayList()
        breakArrayList = ArrayList()
        mThursdayModelArraylist = ArrayList()
        mFieldModel = ArrayList()
        //API ARRAYLIST

        linearLayoutManager = LinearLayoutManager(mContext)
        linearLayoutManagerVertical = LinearLayoutManager(mContext)
        linearLayoutManagerVertical1 = LinearLayoutManager(mContext)
        weekRecyclerList = view!!.findViewById(R.id.weekRecyclerList) as RecyclerView
        linearLayoutManager.orientation = LinearLayoutManager.HORIZONTAL
        weekRecyclerList.layoutManager = linearLayoutManager
        weekRecyclerList.itemAnimator = DefaultItemAnimator()

        timeTableSingleRecycler = view!!.findViewById(R.id.timeTableSingleRecycler) as RecyclerView
        non_primarylinear = view!!.findViewById(R.id.non_primarylinear) as LinearLayout
        primary_pdf = view!!.findViewById(R.id.primary_pdf) as PDFView
        linearLayoutManagerVertical1.orientation = LinearLayoutManager.VERTICAL
        timeTableSingleRecycler.layoutManager = linearLayoutManagerVertical1
        timeTableSingleRecycler.itemAnimator = DefaultItemAnimator()

        recyclerinitializer()

        studentSpinner = view!!.findViewById(R.id.studentSpinner) as LinearLayout
        progressDialog = view!!.findViewById(R.id.progressDialog) as RelativeLayout
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        studImg = view!!.findViewById(R.id.studImg) as ImageView
        noDataImg = view!!.findViewById(R.id.noDataImg) as ImageView
        studentNameTxt = view!!.findViewById(R.id.studentName) as TextView
        card_viewAll = view!!.findViewById(R.id.card_viewAll) as CardView
        sharepdf = view!!.findViewById(R.id.sharepdf) as ImageView
        downloadpdf = view!!.findViewById(R.id.downloadpdf) as ImageView
        sharedownloadlinear = view!!.findViewById(R.id.sharedownloadlinear) as LinearLayout
        tipContainer = view!!.findViewById(R.id.tooltip_container) as ToolTipLayout
        for (i in 0..weekListArrayString.size - 1) {
            var weekModel =
                WeekModel(
                    weekListArrayString.get(i).toString(),
                    -1
                )
            weekListArray.add(weekModel)

        }
        timeTableWeekListAdapter = TimeTableWeekListAdapter(weekListArray)
        weekRecyclerList.adapter = timeTableWeekListAdapter
        studentSpinner.setOnClickListener {

            showStudentList(mContext, studentListArrayList)
        }

        breakArrayList = ArrayList()
        var sdf = SimpleDateFormat("EEEE")
        var d = Date()
        dayOfTheWeek = sdf.format(d)
        when (dayOfTheWeek) {
            "Sunday" -> {
                weekPosition = 1
            }
            "Monday" -> {
                weekPosition = 2
            }
            "Tuesday" -> {
                weekPosition = 3
            }
            "Wednesday" -> {
                weekPosition = 4
            }
            "Thursday" -> {
                weekPosition = 5
            }

            else -> {
                weekPosition = 0
            }
        }

        weekRecyclerList.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

//                val buttonAnimator =
//                    ObjectAnimator.ofFloat(tipContainer, "translationX", 0f, 400f)
//                buttonAnimator.duration = 3000
//                buttonAnimator.interpolator = BounceInterpolator()
//                buttonAnimator.start()
                // Your logic
                weekPosition = position
                if (weekPosition < 3) {
                    weekRecyclerList.scrollToPosition(0)
                } else {
                    weekRecyclerList.scrollToPosition(5)
                }
                for (i in 0 until weekListArray.size) {
                    if (i == position) {
                        weekListArray.get(i).positionSelected = i

                    } else {
                        weekListArray.get(i).positionSelected = -1
                    }
                }
                timeTableWeekListAdapter.notifyDataSetChanged()
                if (position != 0) {
                    timeTableAllRecycler.visibility = View.GONE
                    card_viewAll.visibility = View.GONE
                    //  timeTableAllRecycler.visibility=View.GONE
                    tipContainer.visibility = View.GONE
                    timeTableSingleRecycler.visibility = View.VISIBLE
//                    if (mRangeModel.size>0)
//                    {
                    //card_viewAll.visibility = View.GONE
                    // timeTableAllRecycler.visibility = View.GONE
                    timeTableSingleRecycler.visibility = View.VISIBLE
                    if (weekPosition == 1) {
                        var mRecyclerViewMainAdapter =
                            TimeTableSingleWeekSelectionAdapter(mContext, mSundayArrayList)
                        timeTableSingleRecycler.adapter = mRecyclerViewMainAdapter
                    } else if (weekPosition == 2) {
                        var mRecyclerViewMainAdapter =
                            TimeTableSingleWeekSelectionAdapter(mContext, mMondayArrayList)
                        timeTableSingleRecycler.adapter = mRecyclerViewMainAdapter

                    } else if (weekPosition == 3) {
                        var mRecyclerViewMainAdapter =
                            TimeTableSingleWeekSelectionAdapter(mContext, mTuesdayArrayList)
                        timeTableSingleRecycler.adapter = mRecyclerViewMainAdapter
                    } else if (weekPosition == 4) {
                        var mRecyclerViewMainAdapter =
                            TimeTableSingleWeekSelectionAdapter(mContext, mWednesdayArrayList)
                        timeTableSingleRecycler.adapter = mRecyclerViewMainAdapter
                    } else if (weekPosition == 5) {
                        var mRecyclerViewMainAdapter =
                            TimeTableSingleWeekSelectionAdapter(mContext, mThursdayArrayList)
                        timeTableSingleRecycler.adapter = mRecyclerViewMainAdapter
                    }
                } else {
                    timeTableSingleRecycler.visibility = View.GONE
                    // timeTableAllRecycler.visibility = View.VISIBLE
                    tipContainer.visibility = View.VISIBLE

                    card_viewAll.visibility = View.VISIBLE

                    if (mPeriodModel.size > 0) {
                        recyclerinitializer()
                        timeTableAllRecycler.visibility = View.VISIBLE
                        var mRecyclerAllAdapter = TimeTableAllWeekSelectionAdapterNew(
                            activity?.applicationContext!!,
                            mPeriodModel,
                            timeTableAllRecycler,
                            tipContainer,
                            feildAPIArrayList
                        )
                        timeTableAllRecycler.adapter = mRecyclerAllAdapter
                    }

                }
            }

        })

        downloadpdf.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (mContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_CODE
                    )
                }

                val fileWithinMyDir = File(getFilepath("$title.pdf"))

                if (fileWithinMyDir.exists()) {
                    fileWithinMyDir.delete()
                    startdownloading()
                    onDownloadComplete()
                } else {
                    startdownloading()
                    onDownloadComplete()
                }
            }
        }
        sharepdf.setOnClickListener {

            if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
                if (mContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
                    requestPermissions(
                        arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                        STORAGE_PERMISSION_CODE
                    )
                }

                if (mContext.checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                    val intentShareFile = Intent(Intent.ACTION_SEND)
                    val fileWithinMyDir = File(getFilepath(title + ".pdf"))
                    startdownloadingforshare()

                    intentShareFile.type = "application/pdf"
                    intentShareFile.putExtra(
                        Intent.EXTRA_STREAM,
                        Uri.parse("file://" + getFilepath(title + ".pdf"))
                    )
                    startActivity(Intent.createChooser(intentShareFile, "Share File"))
//                    if (fileWithinMyDir.exists()) {
//                        intentShareFile.type = "application/pdf"
//                        intentShareFile.putExtra(
//                            Intent.EXTRA_STREAM,
//                            Uri.parse("file://" + getFilepath(title + ".pdf"))
//                        )
//                        startActivity(Intent.createChooser(intentShareFile, "Share File"))
//                    }
//
//                    else {
//                        startdownloadingforshare()
//
//                        intentShareFile.type = "application/pdf"
//                        intentShareFile.putExtra(
//                            Intent.EXTRA_STREAM,
//                            Uri.parse("file://" + getFilepath(title + ".pdf"))
//                        )
//                        startActivity(Intent.createChooser(intentShareFile, "Share File"))
//
//                    }
                }

            }


        }
    }

    private fun startdownloadingforshare() {
        val request = DownloadManager.Request(Uri.parse(pdfdata))   //URL = URL to download
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS, title + ".pdf"

        )
        val manager = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    fun onDownloadComplete() {
        val onComplete = object : BroadcastReceiver() {
            override fun onReceive(context: Context?, intent: Intent?) {
                Toast.makeText(context, "File downloaded", Toast.LENGTH_SHORT).show()

            }

        }
        mContext.registerReceiver(onComplete, IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE))
    }

    private fun startdownloading() {
        val request = DownloadManager.Request(Uri.parse(pdfdata))   //URL = URL to download
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
        request.setTitle("Download")
        request.setDescription("The file is downloading...")
        request.allowScanningByMediaScanner()
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$title.pdf")
        val manager = mContext.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        manager.enqueue(request)
    }

    private fun getFilepath(filename: String): String? {
        return File(
            Environment.getExternalStorageDirectory().absolutePath,
            "/Download/$filename"
        ).path
    }



    fun callTimeTableApi() {
        feildAPIArrayList = ArrayList()
        progressDialog.visibility = View.VISIBLE
        val token = sharedprefs.getaccesstoken(mContext)
        val calendarBody = TimeTableApiModel(studentId)
        val call: Call<TimeTableApiDataModel> =
            ApiClient.getClient.timetable(calendarBody, "Bearer " + token)
        call.enqueue(object : Callback<TimeTableApiDataModel> {
            override fun onFailure(call: Call<TimeTableApiDataModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
                showSuccessAlert(mContext, "Something went wrong.", "Alert")
            }

            override fun onResponse(
                call: Call<TimeTableApiDataModel>,
                response: Response<TimeTableApiDataModel>
            ) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status == 100) {
                    pdfdata = response.body()!!.responseArray.pdf_timetable

                    when {
                        pdfdata.equals("Coming Soon", ignoreCase = true) -> {
                            sharedownloadlinear.visibility  =View.GONE
                            primary_pdf.visibility = View.GONE
                            non_primarylinear.visibility = View.GONE
                            showSuccessAlert(
                                mContext,
                                "Timetable will be Available soon.",
                                "Alert"
                            )

                        }
                        pdfdata == "" -> {
                            sharedownloadlinear.visibility  =View.GONE
                            primary_pdf.visibility = View.GONE
                            non_primarylinear.visibility = View.VISIBLE

                            if (response.body()!!.responseArray.timeTableList.isNotEmpty()) {

                                feildAPIArrayList.addAll(response.body()!!.responseArray.field1List)
                                for (i in 0..feildAPIArrayList.size - 1) {
                                    var model = FieldModel(
                                        feildAPIArrayList.get(i).sortname,
                                        feildAPIArrayList.get(i).starttime,
                                        feildAPIArrayList.get(i).endtime
                                    )
                                    mFieldModel.add(model)
                                }

                                val days =
                                    arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday")
                                mRangeModel = ArrayList()
                                mRangeAPIArrayList = ArrayList()
                                mThursdayModelArraylist = ArrayList()
                                mSundayArrayList = ArrayList()
                                mMondayArrayList = ArrayList()
                                mTuesdayArrayList = ArrayList()
                                mWednesdayArrayList = ArrayList()
                                mThursdayArrayList = ArrayList()
                                mSundayArrayList.addAll(response.body()!!.responseArray.range.SundayList)
                                mMondayArrayList.addAll(response.body()!!.responseArray.range.MondayList)
                                mTuesdayArrayList.addAll(response.body()!!.responseArray.range.TuesdayList)
                                mWednesdayArrayList.addAll(response.body()!!.responseArray.range.WednesdayList)
                                mThursdayArrayList.addAll(response.body()!!.responseArray.range.Thursday1List)

                                for (i in 0..weekListArray.size - 1) {
                                    if (i == weekPosition) {
                                        weekListArray.get(i).positionSelected = i
                                    } else {
                                        weekListArray.get(i).positionSelected = -1
                                    }

                                }

                                timeTableWeekListAdapter.notifyDataSetChanged()
                                timeTableSingleRecycler.visibility = View.GONE
                                timeTableAllRecycler.visibility = View.VISIBLE
                                card_viewAll.visibility = View.VISIBLE
                                mTimetableApiArrayList = ArrayList()
                                mTimetableApiArrayList.addAll(response.body()!!.responseArray.timeTableList)
                                mPeriodModel = ArrayList()
                                var mDataModelArrayList = ArrayList<DayModel>()
                                var s = 0
                                var m = 0
                                var tu = 0
                                var w = 0
                                var th = 0
                                for (f in 0..feildAPIArrayList.size - 1) {
                                    var mDayModel: DayModel = DayModel()
                                    var mPeriod: PeriodModel = PeriodModel()
                                    var timeTableListS = ArrayList<DayModel>()
                                    var timeTableListM = ArrayList<DayModel>()
                                    var timeTableListT = ArrayList<DayModel>()
                                    var timeTableListW = ArrayList<DayModel>()
                                    var timeTableListTh = ArrayList<DayModel>()
                                    for (t in 0..mTimetableApiArrayList.size - 1) {
                                        if (feildAPIArrayList.get(f).sortname.equals(
                                                mTimetableApiArrayList.get(t).sortname
                                            )
                                        ) {
                                            Log.e(
                                                "Sortname",
                                                mTimetableApiArrayList.get(t).sortname
                                            )
                                            mDayModel.id = mTimetableApiArrayList.get(t).id
                                            mDayModel.period_id =
                                                mTimetableApiArrayList.get(t).period_id
                                            mDayModel.day = mTimetableApiArrayList.get(t).day
                                            mDayModel.sortname =
                                                mTimetableApiArrayList.get(t).sortname
                                            mDayModel.starttime =
                                                mTimetableApiArrayList.get(t).starttime
                                            mDayModel.endtime =
                                                mTimetableApiArrayList.get(t).endtime
                                            mDayModel.subject_name =
                                                mTimetableApiArrayList.get(t).subject_name
                                            mDayModel.student_id =
                                                mTimetableApiArrayList.get(t).student_id
                                            //    mDayModel.staff=mTimetableApiArrayList.get(t).staff

                                            if (mTimetableApiArrayList.get(t).day.equals("Sunday")) {
                                                s = s + 1
                                                var dayModel = DayModel()
                                                dayModel.id = mTimetableApiArrayList.get(t).id
                                                dayModel.period_id =
                                                    mTimetableApiArrayList.get(t).period_id
                                                dayModel.day = mTimetableApiArrayList.get(t).day
                                                dayModel.sortname =
                                                    mTimetableApiArrayList.get(t).sortname
                                                dayModel.starttime =
                                                    mTimetableApiArrayList.get(t).starttime
                                                dayModel.endtime =
                                                    mTimetableApiArrayList.get(t).endtime
                                                dayModel.subject_name =
                                                    mTimetableApiArrayList.get(t).subject_name
                                                dayModel.student_id =
                                                    mTimetableApiArrayList.get(t).student_id
                                                dayModel.staff = mTimetableApiArrayList.get(t).staff
                                                timeTableListS.add(dayModel)
                                                mPeriod.sunday =
                                                    mTimetableApiArrayList.get(t).subject_name
                                            } else if (mTimetableApiArrayList.get(t).day.equals("Monday")) {
                                                m = m + 1
                                                var dayModel = DayModel()
                                                dayModel.id = mTimetableApiArrayList.get(t).id
                                                dayModel.period_id =
                                                    mTimetableApiArrayList.get(t).period_id
                                                dayModel.subject_name =
                                                    mTimetableApiArrayList.get(t).subject_name
                                                dayModel.staff = mTimetableApiArrayList.get(t).staff
                                                dayModel.student_id =
                                                    mTimetableApiArrayList.get(t).student_id
                                                dayModel.day = mTimetableApiArrayList.get(t).day
                                                dayModel.sortname =
                                                    mTimetableApiArrayList.get(t).sortname
                                                dayModel.starttime =
                                                    mTimetableApiArrayList.get(t).starttime
                                                dayModel.endtime =
                                                    mTimetableApiArrayList.get(t).endtime
                                                timeTableListM.add(dayModel)
                                                mPeriod.monday =
                                                    mTimetableApiArrayList.get(t).subject_name
                                            } else if (mTimetableApiArrayList.get(t).day.equals("Tuesday")) {
                                                tu = tu + 1
                                                var dayModel = DayModel()
                                                dayModel.id = mTimetableApiArrayList.get(t).id
                                                dayModel.period_id =
                                                    mTimetableApiArrayList.get(t).period_id
                                                dayModel.day = mTimetableApiArrayList.get(t).day
                                                dayModel.sortname =
                                                    mTimetableApiArrayList.get(t).sortname
                                                dayModel.starttime =
                                                    mTimetableApiArrayList.get(t).starttime
                                                dayModel.endtime =
                                                    mTimetableApiArrayList.get(t).endtime
                                                dayModel.subject_name =
                                                    mTimetableApiArrayList.get(t).subject_name
                                                dayModel.student_id =
                                                    mTimetableApiArrayList.get(t).student_id
                                                dayModel.staff = mTimetableApiArrayList.get(t).staff
                                                timeTableListT.add(dayModel)
                                                mPeriod.tuesday =
                                                    mTimetableApiArrayList.get(t).subject_name
                                            } else if (mTimetableApiArrayList.get(t).day.equals("Wednesday")) {
                                                w = w + 1
                                                var dayModel = DayModel()
                                                dayModel.id = mTimetableApiArrayList.get(t).id
                                                dayModel.period_id =
                                                    mTimetableApiArrayList.get(t).period_id
                                                dayModel.day = mTimetableApiArrayList.get(t).day
                                                dayModel.sortname =
                                                    mTimetableApiArrayList.get(t).sortname
                                                dayModel.starttime =
                                                    mTimetableApiArrayList.get(t).starttime
                                                dayModel.endtime =
                                                    mTimetableApiArrayList.get(t).endtime
                                                dayModel.subject_name =
                                                    mTimetableApiArrayList.get(t).subject_name
                                                dayModel.student_id =
                                                    mTimetableApiArrayList.get(t).student_id
                                                dayModel.staff = mTimetableApiArrayList.get(t).staff
                                                timeTableListW.add(dayModel)
                                                mPeriod.wednesday =
                                                    mTimetableApiArrayList.get(t).subject_name
                                            } else if (mTimetableApiArrayList.get(t).day.equals("Thursday")) {
                                                th = th + 1
                                                var dayModel = DayModel()
                                                dayModel.id = mTimetableApiArrayList.get(t).id
                                                dayModel.period_id =
                                                    mTimetableApiArrayList.get(t).period_id
                                                dayModel.day = mTimetableApiArrayList.get(t).day
                                                dayModel.sortname =
                                                    mTimetableApiArrayList.get(t).sortname
                                                dayModel.starttime =
                                                    mTimetableApiArrayList.get(t).starttime
                                                dayModel.endtime =
                                                    mTimetableApiArrayList.get(t).endtime
                                                dayModel.subject_name =
                                                    mTimetableApiArrayList.get(t).subject_name
                                                dayModel.student_id =
                                                    mTimetableApiArrayList.get(t).student_id
                                                dayModel.staff = mTimetableApiArrayList.get(t).staff
                                                timeTableListTh.add(dayModel)
                                                mPeriod.thursday =
                                                    mTimetableApiArrayList.get(t).subject_name
                                            } else {
                                                mPeriod.sunday = ""
                                                mPeriod.monday = ""
                                                mPeriod.tuesday = ""
                                                mPeriod.wednesday = ""
                                                mPeriod.thursday = ""
                                            }
                                            mPeriod.countS = s
                                            mPeriod.countM = m
                                            mPeriod.countT = tu
                                            mPeriod.countW = w
                                            mPeriod.countTh = th
                                            mPeriod.timeTableListS = timeTableListS
                                            mPeriod.timeTableListM = timeTableListM
                                            mPeriod.timeTableListTu = timeTableListT
                                            mPeriod.timeTableListW = timeTableListW
                                            mPeriod.timeTableListTh = timeTableListTh

                                        }
                                    }
                                    mDataModelArrayList.add(mDayModel)
                                    mPeriod.timeTableDayModel = mDataModelArrayList
                                    mPeriodModel.add(mPeriod)
                                }


                                if (weekPosition != 0) {
                                    card_viewAll.visibility = View.GONE
                                    timeTableAllRecycler.visibility = View.GONE
                                    timeTableSingleRecycler.visibility = View.VISIBLE
                                    if (weekPosition == 1) {
                                        var mRecyclerViewMainAdapter =
                                            TimeTableSingleWeekSelectionAdapter(
                                                mContext,
                                                mSundayArrayList
                                            )
                                        timeTableSingleRecycler.adapter = mRecyclerViewMainAdapter
                                    } else if (weekPosition == 2) {
                                        var mRecyclerViewMainAdapter =
                                            TimeTableSingleWeekSelectionAdapter(
                                                mContext,
                                                mMondayArrayList
                                            )
                                        timeTableSingleRecycler.adapter = mRecyclerViewMainAdapter

                                    } else if (weekPosition == 3) {
                                        var mRecyclerViewMainAdapter =
                                            TimeTableSingleWeekSelectionAdapter(
                                                mContext,
                                                mTuesdayArrayList
                                            )
                                        timeTableSingleRecycler.adapter = mRecyclerViewMainAdapter
                                    } else if (weekPosition == 4) {
                                        var mRecyclerViewMainAdapter =
                                            TimeTableSingleWeekSelectionAdapter(
                                                mContext,
                                                mWednesdayArrayList
                                            )
                                        timeTableSingleRecycler.adapter = mRecyclerViewMainAdapter
                                    } else if (weekPosition == 5) {
                                        var mRecyclerViewMainAdapter =
                                            TimeTableSingleWeekSelectionAdapter(
                                                mContext,
                                                mThursdayArrayList
                                            )
                                        timeTableSingleRecycler.adapter = mRecyclerViewMainAdapter
                                    }
                                } else {

                                    timeTableSingleRecycler.visibility = View.GONE
                                    timeTableAllRecycler.visibility = View.VISIBLE
                                    card_viewAll.visibility = View.VISIBLE
                                    var mRecyclerAllAdapter = TimeTableAllWeekSelectionAdapterNew(
                                        mContext,
                                        mPeriodModel,
                                        timeTableAllRecycler,
                                        tipContainer,
                                        feildAPIArrayList
                                    )
                                    timeTableAllRecycler.adapter = mRecyclerAllAdapter
                                }
                                if (weekPosition < 3) {
                                    weekRecyclerList.scrollToPosition(0)
                                } else {
                                    weekRecyclerList.scrollToPosition(5)
                                }
                            } else {
                                timeTableSingleRecycler.visibility = View.GONE
                                timeTableAllRecycler.visibility = View.GONE
                                showSuccessAlert(mContext, "No data found.", "Alert")
                            }
                        }
                        else -> {
                            primary_pdf.visibility = View.VISIBLE
                            non_primarylinear.visibility = View.GONE
                            displaypdf()
                            sharedownloadlinear.visibility  =View.VISIBLE
                        }
                    }
                    Log.e("APISUCCESS", response.body().toString())


                } else if (response.body()!!.status == 116) {
                    if (apiCallDetail >= 4) {
                        apiCallDetail = apiCallDetail + 1
                        AccessTokenClass.getAccessToken(mContext)
                        callTimeTableApi()
                    } else {
                        progressDialog.visibility = View.GONE
                        showSuccessAlert(
                            mContext,
                            "Something went wrong.Please try again later",
                            "Alert"
                        )
                    }
//                    AccessTokenClass.getAccessToken(mContext)
//                    callTimeTableApi()
                } else {
                    InternetCheckClass.checkApiStatusError(response.body()!!.status, mContext)
                }
            }

        })
    }

    private fun displaypdf() {
        PRDownloader.initialize(mContext)
        val fileName = "myFile.pdf"

        downloadPdfFromInternet(
            pdfdata,
            getRootDirPath(mContext),
            fileName
        )
    }

    private fun downloadPdfFromInternet(url: String, dirPath: String, fileName: String) {
        PRDownloader.download(
            url,
            dirPath,
            fileName
        ).build()
            .start(object : OnDownloadListener {
                override fun onDownloadComplete() {
                    val downloadedFile = File(dirPath, fileName)

                    showPdfFromFile(downloadedFile)
                }

                override fun onError(error: com.downloader.Error?) {

                }

            })

    }

    private fun showPdfFromFile(file: File) {

        primary_pdf.fromFile(file)
            .password(null)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .enableDoubletap(true)
            .onPageError { page, _ ->
            }
            .load()
    }

    fun getRootDirPath(context: Context): String {
        return if (Environment.MEDIA_MOUNTED == Environment.getExternalStorageState()) {
            val file: File = ContextCompat.getExternalFilesDirs(
                context.applicationContext,
                null
            )[0]
            file.absolutePath
        } else {
            context.applicationContext.filesDir.absolutePath
        }
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
            btn_dismiss.background = mContext.resources.getDrawable(R.drawable.button_new)
        }

        studentListRecycler.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        studentListRecycler.layoutManager = llm
        if (mStudentList.size > 0) {
            val studentAdapter = StudentListAdapter(mContext, mStudentList)
            studentListRecycler.adapter = studentAdapter
        }

        btn_dismiss.setOnClickListener()
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
                timeTableSingleRecycler.visibility = View.GONE
                timeTableAllRecycler.visibility = View.GONE
                callTimeTableApi()
                dialog.dismiss()
            }
        })
        dialog.show()
    }

    fun callStudentListApi() {
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<StudentListModel> = ApiClient.getClient.studentList("Bearer " + token)
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<StudentListModel>,
                response: Response<StudentListModel>
            ) {
                //  val arraySize: Int = response.body()!!.responseArray!!.studentList.size
                if (response.body()!!.status == 100) {

                    studentListArrayList.addAll(response.body()!!.responseArray.studentList)
                    System.out.println("CalendarResoponse" + response.body())
                    if (sharedprefs.getStudentID(mContext).equals("")) {
                        Log.e("Empty Img", "Empty")
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
                    var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                    if (internetCheck) {
                        callTimeTableApi()
                    } else {
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }


                } else {
                    if (response.body()!!.status == 116) {
                        if (apiCall != 4) {
                            apiCall = apiCall + 1
                            AccessTokenClass.getAccessToken(mContext)
                            callStudentListApi()
                        } else {
                            progressDialog.visibility = View.GONE
                            showSuccessAlert(
                                mContext,
                                "Something went wrong.Please try again later",
                                "Alert"
                            )

                        }
//                        AccessTokenClass.getAccessToken(mContext)
//                        callStudentListApi()
                    }
                }


            }

        })
    }


    fun showSuccessAlert(context: Context, message: String, msgHead: String) {
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
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()

        }
        dialog.show()
    }

    fun recyclerinitializer() {
        timeTableAllRecycler = view!!.findViewById(R.id.timeTableAllRecycler) as RecyclerView
        linearLayoutManagerVertical.orientation = LinearLayoutManager.VERTICAL
        timeTableAllRecycler.layoutManager = linearLayoutManagerVertical
        timeTableAllRecycler.itemAnimator = DefaultItemAnimator()
    }

}




