package com.mobatia.bisad.fragment.calendar_new

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
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.calendar.model.CalendarListResponse
import com.mobatia.bisad.fragment.calendar_new.model.*
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.ApiClient
import com.mobatia.calendardemopro.adapter.CalendarDateAdapter
import com.mobatia.calendardemopro.adapter.CategoryAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragmentNew : Fragment() {
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var calendarRecycler: RecyclerView
    lateinit var progressDialog: RelativeLayout
    lateinit var titleTextView: TextView
    lateinit var mContext: Context
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var monthYearTxt: TextView
    lateinit var previousBtn: ImageView
    lateinit var nextBtn: ImageView
    lateinit var filterLinear: LinearLayout
    var isPrimarySelected:Boolean=true
    var isSecondarySeleted:Boolean=true
    var isWholeSchoolSelected:Boolean=true
    var isAllSelected:Boolean=true
    var year:Int=0
    lateinit var calendarArrayList:ArrayList<CalendarResponseArray>
    lateinit var primaryArrayList:ArrayList<VEVENT>
    lateinit var secondaryArrayList:ArrayList<VEVENT>
    lateinit var wholeSchoolArrayList:ArrayList<VEVENT>

    lateinit var primaryShowArrayList:ArrayList<PrimaryModel>
    lateinit var secondaryShowArrayList:ArrayList<PrimaryModel>
    lateinit var wholeSchoolShowArrayList:ArrayList<PrimaryModel>
    lateinit var calendarShowArrayList:ArrayList<PrimaryModel>
    lateinit var calendarFilterArrayList:ArrayList<PrimaryModel>
    lateinit var mTriggerModelArrayList: ArrayList<CategoryModel>
    lateinit var mCalendarFinalArrayList: ArrayList<CalendarDateModel>

    var currentMonth:Int=-1
    lateinit var monthTxt:String
    var primaryColor:String=""
    var secondaryColor:String=""
    var wholeSchoole:String=""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.calendar_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        initializeUI()
        callCalendarApi()
    }
    private fun initializeUI() {
        calendarRecycler = view!!.findViewById(R.id.calendarRecycler) as RecyclerView
        progressDialog = view!!.findViewById(R.id.progressDialog) as RelativeLayout
        titleTextView = view!!.findViewById(R.id.titleTextView) as TextView
        linearLayoutManager = LinearLayoutManager(mContext)
        calendarRecycler.layoutManager = linearLayoutManager
        calendarRecycler.itemAnimator = DefaultItemAnimator()
        titleTextView.text = "Calendar"
        progressDialog.visibility = View.VISIBLE
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        monthYearTxt=view!!.findViewById(R.id.monthYearTxt)
        previousBtn=view!!.findViewById(R.id.previousBtn)
        nextBtn=view!!.findViewById(R.id.nextBtn)
        filterLinear=view!!.findViewById(R.id.filterLinear)
        year= Calendar.getInstance().get(Calendar.YEAR)
        currentMonth= Calendar.getInstance().get(Calendar.MONTH)
        month(currentMonth,year)

        filterLinear.setOnClickListener(View.OnClickListener {

            if(calendarArrayList.size>0)
            {
                showTriggerDataCollection(mContext, R.drawable.questionmark_icon, R.drawable.round)
            }


        })

        nextBtn.setOnClickListener(View.OnClickListener {
            currentMonth=currentMonth+1
            if (currentMonth>11)
            {
                currentMonth=currentMonth-12
                year=year+1

            }
            month(currentMonth,year)
            showCalendarEvent(isAllSelected,isPrimarySelected,isSecondarySeleted,isWholeSchoolSelected)

        })

        previousBtn.setOnClickListener(View.OnClickListener {

            if (currentMonth==0)
            {
                currentMonth= 11-currentMonth
                year=year-1
            }
            else
            {
                currentMonth=currentMonth-1
            }
            month(currentMonth,year)
            showCalendarEvent(isAllSelected,isPrimarySelected,isSecondarySeleted,isWholeSchoolSelected)

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
            btn_Ok?.setOnClickListener()
            {
                dialog.dismiss()

            }
            dialog.show()
        }
    fun callCalendarApi()
    {
        calendarArrayList= ArrayList()
        primaryArrayList=ArrayList()
        secondaryArrayList=ArrayList()
        wholeSchoolArrayList=ArrayList()
        progressDialog.visibility = View.VISIBLE
        val call: Call<CalendarModel> = ApiClient.getClient.calendarList("Bearer " + "bdfbuddjajmnsdbcjf")
        call.enqueue(object : Callback<CalendarModel> {
            override fun onFailure(call: Call<CalendarModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
            }
            override fun onResponse(
                call: Call<CalendarModel>,
                response: Response<CalendarModel>
            ) {
                progressDialog.visibility = View.GONE
                if (response.body()!!.status == 100) {
                    calendarArrayList.addAll(response.body()!!.calendarList)
                    if (calendarArrayList.size>0)
                    {
                        for (i in 0..calendarArrayList.size-1)
                        {
                            if (calendarArrayList.get(i).title.equals("Primary"))
                            {
                                if(calendarArrayList.get(i).calendarDetail.cal.VEVENT.size>0)
                                {
                                    primaryArrayList.addAll(calendarArrayList.get(i).calendarDetail.cal.VEVENT)
                                    primaryColor=calendarArrayList.get(i).color
                                }

                            }
                            else if (calendarArrayList.get(i).title.equals("Secondary"))
                            {
                                if (calendarArrayList.get(i).calendarDetail.cal.VEVENT.size>0)
                                {

                                    secondaryArrayList.addAll(calendarArrayList.get(i).calendarDetail.cal.VEVENT)
                                    secondaryColor=calendarArrayList.get(i).color
                                }

                            }
                            else if (calendarArrayList.get(i).title.equals("Whole School"))
                            {
                                if (calendarArrayList.get(i).calendarDetail.cal.VEVENT.size>0)
                                {
                                    wholeSchoolArrayList.addAll(calendarArrayList.get(i).calendarDetail.cal.VEVENT)
                                    wholeSchoole=calendarArrayList.get(i).color
                                }

                            }

                            isAllSelected=true
                            isPrimarySelected=true
                            isSecondarySeleted=true
                            isWholeSchoolSelected=true
                            showCalendarEvent(isAllSelected,isPrimarySelected,isSecondarySeleted,isWholeSchoolSelected)
                        }
                        var categoryList = ArrayList<String>()
                        categoryList.add("Select all/none")
                        categoryList.add("Primary Event")
                        categoryList.add("Secondary Event")
                        categoryList.add("Whole School Event")

                        mTriggerModelArrayList = ArrayList()
                        for (i in 0..categoryList.size - 1) {
                            var model = CategoryModel()
                            model.categoryName = categoryList.get(i)
                            model.checkedCategory = true
                            if (i == 0) {
                                var whiteColor = "#ffffff"
                                model.color = whiteColor
                            } else {

                                if (i == 1) {
                                    model.color = primaryColor
                                }
                                if (i == 2) {
                                    model.color = secondaryColor
                                }
                                if (i == 3) {
                                    model.color = wholeSchoole
                                }
                            }

                            mTriggerModelArrayList.add(model)

                        }
                    }
                    else
                    {


                    }

                } else if (response.body()!!.status == 116) {
                    callCalendarApi()
                }
                else {

                }
            }

        })
    }
    fun showCalendarEvent(allSeleted:Boolean,primarySelected:Boolean,secondarySelected:Boolean,wholeSchoolSelected:Boolean)
    {
        primaryShowArrayList= ArrayList()
        secondaryShowArrayList= ArrayList()
        wholeSchoolShowArrayList=ArrayList()
        calendarFilterArrayList=ArrayList()
        if (primaryArrayList.size>0)
        {
            for (i in 0..primaryArrayList.size-1)
            {
                var pModel=PrimaryModel()
                if (primaryArrayList.get(i).DTSTART.toString().length==16)
                {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTSTART)
                    var outputDateStrstart:String= outputFormat.format(startdate)
                    pModel.DTSTART=outputDateStrstart

                }
                else if (primaryArrayList.get(i).DTSTART.toString().length==8)
                {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTSTART)
                    var outputDateStrstart:String= outputFormat.format(startdate)
                    pModel.DTSTART=outputDateStrstart

                }
                if (primaryArrayList.get(i).DTEND.toString().length==16)
                {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTEND)
                    var outputDateStrstart:String= outputFormat.format(startdate)
                    pModel.DTEND=outputDateStrstart

                }
                else if (primaryArrayList.get(i).DTEND.toString().length==8)
                {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTEND)
                    var outputDateStrstart:String= outputFormat.format(startdate)
                    pModel.DTEND=outputDateStrstart

                }

                pModel.SUMMARY=primaryArrayList.get(i).SUMMARY
                pModel.DESCRIPTION=primaryArrayList.get(i).DESCRIPTION
                pModel.LOCATION=primaryArrayList.get(i).LOCATION
                pModel.color=primaryColor
                pModel.type=1
                primaryShowArrayList.add(pModel)
            }
        }
        if (secondaryArrayList.size>0)
        {
            for (i in 0..secondaryArrayList.size-1)
            {
                var sModel=PrimaryModel()
                if (secondaryArrayList.get(i).DTSTART.toString().length==16)
                {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTSTART)
                    var outputDateStrstart:String= outputFormat.format(startdate)
                    sModel.DTSTART=outputDateStrstart

                }
                else if (secondaryArrayList.get(i).DTSTART.toString().length==8)
                {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTSTART)
                    var outputDateStrstart:String= outputFormat.format(startdate)
                    sModel.DTSTART=outputDateStrstart

                }
                if (secondaryArrayList.get(i).DTEND.equals("null") )
                {
                    sModel.DTEND=""
                }
                else if (secondaryArrayList.get(i).DTEND.toString().length==16)
                {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTEND)
                    var outputDateStrstart:String= outputFormat.format(startdate)
                    sModel.DTEND=outputDateStrstart

                }
                else if (secondaryArrayList.get(i).DTEND.toString().length==8)
                {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTEND)
                    var outputDateStrstart:String= outputFormat.format(startdate)
                    sModel.DTEND=outputDateStrstart

                }
                sModel.SUMMARY=secondaryArrayList.get(i).SUMMARY
                sModel.DESCRIPTION=secondaryArrayList.get(i).DESCRIPTION
                sModel.LOCATION=secondaryArrayList.get(i).LOCATION
                sModel.color=secondaryColor
                sModel.type=2
                secondaryShowArrayList.add(sModel)
            }
        }
        if (wholeSchoolArrayList.size>0)
        {
            for (i in 0..wholeSchoolArrayList.size-1)
            {
                var wModel=PrimaryModel()
                if (wholeSchoolArrayList.get(i).DTSTART.toString().length==16)
                {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTSTART)
                    var outputDateStrstart:String= outputFormat.format(startdate)
                    wModel.DTSTART=outputDateStrstart

                }
                else if (wholeSchoolArrayList.get(i).DTSTART.toString().length==8)
                {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTSTART)
                    var outputDateStrstart:String= outputFormat.format(startdate)
                    wModel.DTSTART=outputDateStrstart

                }
                if (wholeSchoolArrayList.get(i).DTEND.toString().length==16)
                {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTEND)
                    var outputDateStrstart:String= outputFormat.format(startdate)
                    wModel.DTEND=outputDateStrstart

                }
                else if (wholeSchoolArrayList.get(i).DTEND.toString().length==8)
                {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTEND)
                    var outputDateStrstart:String= outputFormat.format(startdate)
                    wModel.DTEND=outputDateStrstart

                }

                wModel.SUMMARY=wholeSchoolArrayList.get(i).SUMMARY
                wModel.DESCRIPTION=wholeSchoolArrayList.get(i).DESCRIPTION
                wModel.LOCATION=wholeSchoolArrayList.get(i).LOCATION
                wModel.color=wholeSchoole
                wModel.type=3
                wholeSchoolShowArrayList.add(wModel)
            }
        }

        if (allSeleted) {
            calendarShowArrayList=ArrayList()
            if (primaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }
            if (secondaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }
            if (wholeSchoolShowArrayList.size > 0) {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        }

        else if (!allSeleted && !primarySelected && !secondarySelected && !wholeSchoolSelected)
        {
            calendarShowArrayList=ArrayList()
            var dummy=ArrayList<PrimaryModel>()
            calendarShowArrayList=dummy
        }

        else if (!allSeleted && !primarySelected && !secondarySelected && wholeSchoolSelected )
        {
            calendarShowArrayList=ArrayList()
            if (wholeSchoolShowArrayList.size>0)
            {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        }
        else if (!allSeleted && !primarySelected && secondarySelected && !wholeSchoolSelected )
        {
            calendarShowArrayList=ArrayList()
            if (secondaryShowArrayList.size>0)
            {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }

        }
        else if (!allSeleted && !primarySelected && secondarySelected && wholeSchoolSelected )
        {
            calendarShowArrayList=ArrayList()
            if (secondaryShowArrayList.size>0)
            {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }
            if(wholeSchoolShowArrayList.size>0)
            {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        }
        else if (!allSeleted && primarySelected && !secondarySelected && !wholeSchoolSelected )
        {
            calendarShowArrayList=ArrayList()
            if (primaryShowArrayList.size>0)
            {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }

        }

        else if (!allSeleted && primarySelected && !secondarySelected && wholeSchoolSelected )
        {
            calendarShowArrayList=ArrayList()
            if (primaryShowArrayList.size>0)
            {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }
            if(wholeSchoolShowArrayList.size>0)
            {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        }

        else if (!allSeleted && primarySelected && secondarySelected && !wholeSchoolSelected )
        {
            calendarShowArrayList=ArrayList()
            if (primaryShowArrayList.size>0)
            {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }
            if(secondaryShowArrayList.size>0)
            {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }

        }

        if (calendarShowArrayList.size>0)
        {
            var listMonth: String = ""
            var listYear: String = ""
            for (i in 0..calendarShowArrayList.size - 1) {
                if (calendarShowArrayList.get(i).DTSTART.length == 20) {
                    val inputFormat: DateFormat =
                        SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val outputFormatYear: DateFormat = SimpleDateFormat("yyyy")
                    val outputFormatMonth: DateFormat = SimpleDateFormat("MMMM")
                    val startdate: Date =
                        inputFormat.parse(calendarShowArrayList.get(i).DTSTART)
                    var outputDateMonth: String =
                        outputFormatMonth.format(startdate)
                    var outputDateYear: String = outputFormatYear.format(startdate)
                    listMonth = outputDateMonth
                    listYear = outputDateYear
                } else if (calendarShowArrayList.get(i).DTSTART.length == 11) {
                    val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val outputFormatYear: DateFormat = SimpleDateFormat("yyyy")
                    val outputFormatMonth: DateFormat = SimpleDateFormat("MMMM")
                    val startdate: Date =
                        inputFormat.parse(calendarShowArrayList.get(i).DTSTART)
                    var outputDateMonth: String =
                        outputFormatMonth.format(startdate)
                    var outputDateYear: String = outputFormatYear.format(startdate)
                    listMonth = outputDateMonth
                    listYear = outputDateYear
                }
                if (listYear.equals(year.toString()))
                {
                    if (monthTxt.equals(listMonth)) {
                        var model = PrimaryModel()
                        model.DTSTART = calendarShowArrayList.get(i).DTSTART
                        model.DTEND = calendarShowArrayList.get(i).DTEND
                        model.SUMMARY = calendarShowArrayList.get(i).SUMMARY
                        model.DESCRIPTION = calendarShowArrayList.get(i).DESCRIPTION
                        model.LOCATION = calendarShowArrayList.get(i).LOCATION
                        model.color = calendarShowArrayList.get(i).color
                        model.type = calendarShowArrayList.get(i).type
                        calendarFilterArrayList.add(model)
                    }
                }

                if (calendarFilterArrayList.size>0)
                {
                    calendarRecycler.visibility=View.VISIBLE
                    calendarFilterArrayList.sortByDescending { calendarFilterArrayList->calendarFilterArrayList.DTSTART }
                    calendarFilterArrayList.reverse()
                    mCalendarFinalArrayList=ArrayList()
                    for (i in 0..calendarFilterArrayList.size-1)
                    {
                        var cModel=CalendarDateModel()
                        cModel.startDate=calendarFilterArrayList.get(i).DTSTART
                        var calendarDetaiArray=ArrayList<CalendarDetailModel>()
                        var dModel=CalendarDetailModel()
                        dModel.DTSTART=calendarFilterArrayList.get(i).DTSTART
                        dModel.DTEND=calendarFilterArrayList.get(i).DTEND
                        dModel.SUMMARY=calendarFilterArrayList.get(i).SUMMARY
                        dModel.DESCRIPTION=calendarFilterArrayList.get(i).DESCRIPTION
                        dModel.LOCATION=calendarFilterArrayList.get(i).LOCATION
                        dModel.color=calendarFilterArrayList.get(i).color
                        dModel.type=calendarFilterArrayList.get(i).type
                        calendarDetaiArray.add(dModel)
                        cModel.detailList=calendarDetaiArray
                        mCalendarFinalArrayList.add(cModel)
                    }

                    val calendarListAdapter = CalendarDateAdapter(mContext,mCalendarFinalArrayList)
                    calendarRecycler.adapter = calendarListAdapter
                }
                else
                {
                    calendarRecycler.visibility=View.GONE
                }
            }


        }
        else
        {
            calendarRecycler.visibility=View.GONE
        }

    }

    fun month(month:Int,year:Int)
    {
        when(month)
        {
            0->
            {
                monthTxt="January"
                monthYearTxt.setText(monthTxt +year.toString())

            }

            1->
            {
                monthTxt="February"
                monthYearTxt.setText(monthTxt+year.toString())
            }

            2->
            {
                monthTxt="March"
                monthYearTxt.setText(monthTxt+year.toString())
            }

            3->
            {
                monthTxt="April"
                monthYearTxt.setText(monthTxt+year.toString())
            }

            4->
            {
                monthTxt="May"
                monthYearTxt.setText(monthTxt+year.toString())
            }

            5->
            {
                monthTxt="June"
                monthYearTxt.setText(monthTxt+year.toString())
            }

            6->
            {
                monthTxt="July"
                monthYearTxt.setText(monthTxt+year.toString())
            }

            7->
            {
                monthTxt="August"
                monthYearTxt.setText(monthTxt+year.toString())
            }

            8->
            {
                monthTxt="September"
                monthYearTxt.setText(monthTxt+year.toString())
            }

            9->
            {
                monthTxt="October"
                monthYearTxt.setText(monthTxt+year.toString())
            }

            10->
            {
                monthTxt="November"
                monthYearTxt.setText(monthTxt+year.toString())
            }

            11->
            {
                monthTxt="December"
                monthYearTxt.setText(monthTxt+year.toString())
            }

        }
    }


    fun showTriggerDataCollection(context: Context, ico: Int, bgIcon: Int) {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_calendar_category)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var checkRecycler = dialog.findViewById(R.id.checkRecycler) as RecyclerView
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var linearLayoutManagerM: LinearLayoutManager = LinearLayoutManager(mContext)
        checkRecycler.layoutManager = linearLayoutManagerM
        checkRecycler.itemAnimator = DefaultItemAnimator()
        iconImageView.setBackgroundResource(bgIcon)
        iconImageView.setImageResource(ico)

        var triggerAdapter = CategoryAdapter(mTriggerModelArrayList)
        checkRecycler.setAdapter(triggerAdapter)
        btn_Cancel.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        btn_Ok.setOnClickListener(View.OnClickListener {
            showCalendarEvent(isAllSelected,isPrimarySelected,isSecondarySeleted,isWholeSchoolSelected)
            dialog.dismiss()
        })
        checkRecycler.addOnItemClickListener(object : OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                var selectedPosition: Int = 0
                if (position == 0) {
                    var pos0: Boolean = mTriggerModelArrayList.get(0).checkedCategory
                    if (pos0) {
                        isAllSelected = false
                        isPrimarySelected = false
                        isSecondarySeleted = false
                        isWholeSchoolSelected = false
                        mTriggerModelArrayList.get(0).checkedCategory = false
                        mTriggerModelArrayList.get(1).checkedCategory = false
                        mTriggerModelArrayList.get(2).checkedCategory = false
                        mTriggerModelArrayList.get(3).checkedCategory = false
                    } else {
                        isAllSelected = true
                        isPrimarySelected = true
                        isSecondarySeleted = true
                        isWholeSchoolSelected = true
                        mTriggerModelArrayList.get(0).checkedCategory = true
                        mTriggerModelArrayList.get(1).checkedCategory = true
                        mTriggerModelArrayList.get(2).checkedCategory = true
                        mTriggerModelArrayList.get(3).checkedCategory = true
                    }

                } else {
                    if (position == 1) {
                        var pos0: Boolean = mTriggerModelArrayList.get(0).checkedCategory
                        var pos1: Boolean = mTriggerModelArrayList.get(1).checkedCategory
                        var pos2: Boolean = mTriggerModelArrayList.get(2).checkedCategory
                        var pos3: Boolean = mTriggerModelArrayList.get(3).checkedCategory
                        //0000
                        if (!pos0 && !pos1 && !pos2 && !pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = true
                            mTriggerModelArrayList.get(2).checkedCategory = false
                            mTriggerModelArrayList.get(3).checkedCategory = false
                            isAllSelected = false
                            isPrimarySelected = true
                            isSecondarySeleted = false
                            isWholeSchoolSelected = false
                        }
                        //0001
                        else if (!pos0 && !pos1 && !pos2 && pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = true
                            mTriggerModelArrayList.get(2).checkedCategory = false
                            mTriggerModelArrayList.get(3).checkedCategory = true
                            isAllSelected = false
                            isPrimarySelected = true
                            isSecondarySeleted = false
                            isWholeSchoolSelected = true
                        }
                        //0010
                        else if (!pos0 && !pos1 && pos2 && !pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = true
                            mTriggerModelArrayList.get(2).checkedCategory = true
                            mTriggerModelArrayList.get(3).checkedCategory = false
                            isAllSelected = false
                            isPrimarySelected = true
                            isSecondarySeleted = true
                            isWholeSchoolSelected = false
                        }
                        //0011
                        else if (!pos0 && !pos1 && pos2 && pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = true
                            mTriggerModelArrayList.get(1).checkedCategory = true
                            mTriggerModelArrayList.get(2).checkedCategory = true
                            mTriggerModelArrayList.get(3).checkedCategory = true
                            isAllSelected = true
                            isPrimarySelected = true
                            isSecondarySeleted = true
                            isWholeSchoolSelected = false
                        }
                        //0100
                        else if (!pos0 && pos1 && !pos2 && !pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = false
                            mTriggerModelArrayList.get(2).checkedCategory = false
                            mTriggerModelArrayList.get(3).checkedCategory = false
                            isAllSelected = false
                            isPrimarySelected = false
                            isSecondarySeleted = false
                            isWholeSchoolSelected = false
                        }
                        //0101
                        else if (!pos0 && pos1 && !pos2 && pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = false
                            mTriggerModelArrayList.get(2).checkedCategory = false
                            mTriggerModelArrayList.get(3).checkedCategory = true
                            isAllSelected = false
                            isPrimarySelected = false
                            isSecondarySeleted = false
                            isWholeSchoolSelected = true
                        }
                        //0110
                        else if (!pos0 && pos1 && pos2 && !pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = false
                            mTriggerModelArrayList.get(2).checkedCategory = true
                            mTriggerModelArrayList.get(3).checkedCategory = false
                            isAllSelected = false
                            isPrimarySelected = false
                            isSecondarySeleted = true
                            isWholeSchoolSelected = false
                        }
                        //1111
                        else if (pos0 && pos1 && pos2 && pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = false
                            mTriggerModelArrayList.get(2).checkedCategory = true
                            mTriggerModelArrayList.get(3).checkedCategory = true
                            isAllSelected = false
                            isPrimarySelected = false
                            isSecondarySeleted = true
                            isWholeSchoolSelected = true
                        }

                    } else if (position == 2) {
                        var pos0: Boolean = mTriggerModelArrayList.get(0).checkedCategory
                        var pos1: Boolean = mTriggerModelArrayList.get(1).checkedCategory
                        var pos2: Boolean = mTriggerModelArrayList.get(2).checkedCategory
                        var pos3: Boolean = mTriggerModelArrayList.get(3).checkedCategory
                        //0000
                        if (!pos0 && !pos1 && !pos2 && !pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = false
                            mTriggerModelArrayList.get(2).checkedCategory = true
                            mTriggerModelArrayList.get(3).checkedCategory = false
                            isAllSelected = false
                            isPrimarySelected = false
                            isSecondarySeleted = true
                            isWholeSchoolSelected = false
                        }

                        //0001
                        else if (!pos0 && !pos1 && !pos2 && pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = false
                            mTriggerModelArrayList.get(2).checkedCategory = true
                            mTriggerModelArrayList.get(3).checkedCategory = true
                            isAllSelected = false
                            isPrimarySelected = false
                            isSecondarySeleted = true
                            isWholeSchoolSelected = true
                        }
                        //0010
                        else if (!pos0 && !pos1 && pos2 && !pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = false
                            mTriggerModelArrayList.get(2).checkedCategory = false
                            mTriggerModelArrayList.get(3).checkedCategory = false
                            isAllSelected = false
                            isPrimarySelected = false
                            isSecondarySeleted = false
                            isWholeSchoolSelected = false
                        }
                        //0011
                        else if (!pos0 && !pos1 && pos2 && pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = false
                            mTriggerModelArrayList.get(2).checkedCategory = false
                            mTriggerModelArrayList.get(3).checkedCategory = true
                            isAllSelected = false
                            isPrimarySelected = false
                            isSecondarySeleted = false
                            isWholeSchoolSelected = true
                        }
                        //0100
                        else if (!pos0 && pos1 && !pos2 && !pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = true
                            mTriggerModelArrayList.get(2).checkedCategory = true
                            mTriggerModelArrayList.get(3).checkedCategory = false
                            isAllSelected = false
                            isPrimarySelected = true
                            isSecondarySeleted = true
                            isWholeSchoolSelected = false
                        }

                        //0101
                        else if (!pos0 && pos1 && !pos2 && pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = true
                            mTriggerModelArrayList.get(1).checkedCategory = true
                            mTriggerModelArrayList.get(2).checkedCategory = true
                            mTriggerModelArrayList.get(3).checkedCategory = true
                            isAllSelected = true
                            isPrimarySelected = true
                            isSecondarySeleted = true
                            isWholeSchoolSelected = true
                        }
                        //0110
                        else if (!pos0 && pos1 && pos2 && !pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = true
                            mTriggerModelArrayList.get(2).checkedCategory = false
                            mTriggerModelArrayList.get(3).checkedCategory = false
                            isAllSelected = false
                            isPrimarySelected = true
                            isSecondarySeleted = false
                            isWholeSchoolSelected = false
                        }

                        //1111
                        else if (pos0 && pos1 && pos2 && pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = true
                            mTriggerModelArrayList.get(2).checkedCategory = false
                            mTriggerModelArrayList.get(3).checkedCategory = true
                            isAllSelected = false
                            isPrimarySelected = true
                            isSecondarySeleted = false
                            isWholeSchoolSelected = true
                        }
                    } else if (position == 3) {
                        var pos0: Boolean = mTriggerModelArrayList.get(0).checkedCategory
                        var pos1: Boolean = mTriggerModelArrayList.get(1).checkedCategory
                        var pos2: Boolean = mTriggerModelArrayList.get(2).checkedCategory
                        var pos3: Boolean = mTriggerModelArrayList.get(3).checkedCategory
                        //0000
                        if (!pos0 && !pos1 && !pos2 && !pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = false
                            mTriggerModelArrayList.get(2).checkedCategory = false
                            mTriggerModelArrayList.get(3).checkedCategory = true
                            isAllSelected = false
                            isPrimarySelected = false
                            isSecondarySeleted = false
                            isWholeSchoolSelected = true
                        }
                        //0001
                        else if (!pos0 && !pos1 && !pos2 && pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = false
                            mTriggerModelArrayList.get(2).checkedCategory = false
                            mTriggerModelArrayList.get(3).checkedCategory = false
                            isAllSelected = false
                            isPrimarySelected = false
                            isSecondarySeleted = false
                            isWholeSchoolSelected = false
                        }
                        //0010
                        else if (!pos0 && !pos1 && pos2 && !pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = false
                            mTriggerModelArrayList.get(2).checkedCategory = true
                            mTriggerModelArrayList.get(3).checkedCategory = true
                            isAllSelected = false
                            isPrimarySelected = false
                            isSecondarySeleted = true
                            isWholeSchoolSelected = true
                        }
                        //0011
                        else if (!pos0 && !pos1 && pos2 && pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = false
                            mTriggerModelArrayList.get(2).checkedCategory = true
                            mTriggerModelArrayList.get(3).checkedCategory = false
                            isAllSelected = false
                            isPrimarySelected = false
                            isSecondarySeleted = true
                            isWholeSchoolSelected = false
                        }
                        //0100
                        else if (!pos0 && pos1 && !pos2 && !pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = true
                            mTriggerModelArrayList.get(2).checkedCategory = false
                            mTriggerModelArrayList.get(3).checkedCategory = true
                            isAllSelected = false
                            isPrimarySelected = true
                            isSecondarySeleted = false
                            isWholeSchoolSelected = true
                        }
                        //0101
                        else if (!pos0 && pos1 && !pos2 && pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = true
                            mTriggerModelArrayList.get(2).checkedCategory = false
                            mTriggerModelArrayList.get(3).checkedCategory = false
                            isAllSelected = false
                            isPrimarySelected = true
                            isSecondarySeleted = false
                            isWholeSchoolSelected = false
                        }
                        //0110
                        else if (!pos0 && pos1 && pos2 && !pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = true
                            mTriggerModelArrayList.get(1).checkedCategory = true
                            mTriggerModelArrayList.get(2).checkedCategory = true
                            mTriggerModelArrayList.get(3).checkedCategory = true
                            isAllSelected = true
                            isPrimarySelected = true
                            isSecondarySeleted = true
                            isWholeSchoolSelected = true
                        }
                        //1111
                        else if (pos0 && pos1 && pos2 && pos3) {
                            mTriggerModelArrayList.get(0).checkedCategory = false
                            mTriggerModelArrayList.get(1).checkedCategory = true
                            mTriggerModelArrayList.get(2).checkedCategory = true
                            mTriggerModelArrayList.get(3).checkedCategory = false
                            isAllSelected = false
                            isPrimarySelected = true
                            isSecondarySeleted = true
                            isWholeSchoolSelected = false
                        }
                    }
                }

                var triggerAdapter = CategoryAdapter(mTriggerModelArrayList)
                checkRecycler.setAdapter(triggerAdapter)

            }
        })
        dialog.show()
    }
}
