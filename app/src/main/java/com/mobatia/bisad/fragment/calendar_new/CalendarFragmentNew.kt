package com.mobatia.bisad.fragment.calendar_new

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
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
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.calendar_new.model.*
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.ApiClient
import com.mobatia.calendardemopro.adapter.CalendarDateAdapter
import com.mobatia.calendardemopro.adapter.CategoryAdapter
import kotlinx.android.synthetic.main.fragment_passport_detail.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList

class CalendarFragmentNew : Fragment() {
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var calendarRecycler: RecyclerView
    lateinit var progressDialog: RelativeLayout
    lateinit var titleTextView: TextView
    lateinit var noEventTxt: TextView
    lateinit var noEventImage: ImageView
    lateinit var mContext: Context
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var monthYearTxt: TextView
    lateinit var hideBtn: TextView
    lateinit var showBtn: TextView
    lateinit var previousBtn: ImageView
    lateinit var nextBtn: ImageView
    lateinit var filterLinear: LinearLayout
    lateinit var hidePast: LinearLayout
    lateinit var monthLinear: LinearLayout
    var isPrimarySelected: Boolean = true
    var isSecondarySeleted: Boolean = true
    var isWholeSchoolSelected: Boolean = true
    var isAllSelected: Boolean = true
    var isHide: Boolean = false
    var isShow: Boolean = true
    var year: Int = 0
    lateinit var calendarArrayList: ArrayList<CalendarResponseArray>
    lateinit var primaryArrayList: ArrayList<VEVENT>
    lateinit var secondaryArrayList: ArrayList<VEVENT>
    lateinit var wholeSchoolArrayList: ArrayList<VEVENT>

    lateinit var primaryShowArrayList: ArrayList<PrimaryModel>
    lateinit var secondaryShowArrayList: ArrayList<PrimaryModel>
    lateinit var wholeSchoolShowArrayList: ArrayList<PrimaryModel>
    lateinit var calendarShowArrayList: ArrayList<PrimaryModel>
    lateinit var calendarFilterArrayList: ArrayList<PrimaryModel>
    lateinit var mTriggerModelArrayList: ArrayList<CategoryModel>
    lateinit var mCalendarFinalArrayList: ArrayList<CalendarDateModel>
    lateinit var TempCALENDARlIST: ArrayList<CalendarDateModel>
    lateinit var FILTERCALENDARlIST: ArrayList<PrimaryModel>
    val liveArray: ArrayList<CalendarDateModel> = ArrayList()
    lateinit var difference_In_Days: String


    var currentMonth: Int = -1
    lateinit var monthTxt: String
    var primaryColor: String = ""
    var secondaryColor: String = ""
    var wholeSchoole: String = ""
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
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            callCalendarApi()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }

    }

    private fun initializeUI() {
        calendarRecycler = view!!.findViewById(R.id.calendarRecycler) as RecyclerView
        progressDialog = view!!.findViewById(R.id.progressDialog) as RelativeLayout
        titleTextView = view!!.findViewById(R.id.titleTextView) as TextView
        showBtn = view!!.findViewById(R.id.showBtn) as TextView
        hideBtn = view!!.findViewById(R.id.hideBtn) as TextView
        noEventTxt = view!!.findViewById(R.id.noEventTxt) as TextView
        noEventImage = view!!.findViewById(R.id.noEventImage) as ImageView
        linearLayoutManager = LinearLayoutManager(mContext)
        calendarRecycler.layoutManager = linearLayoutManager
        calendarRecycler.itemAnimator = DefaultItemAnimator()
        titleTextView.text = "Calendar"
        progressDialog.visibility = View.VISIBLE
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        monthYearTxt = view!!.findViewById(R.id.monthYearTxt)
        previousBtn = view!!.findViewById(R.id.previousBtn)
        nextBtn = view!!.findViewById(R.id.nextBtn)
        filterLinear = view!!.findViewById(R.id.filterLinear)
        hidePast = view!!.findViewById(R.id.hidePast)
        monthLinear = view!!.findViewById(R.id.monthLinear)
        year = Calendar.getInstance().get(Calendar.YEAR)
        currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        month(currentMonth, year)
        if(!isHide && isShow)
        {
            hideBtn.setTextColor(resources.getColor(R.color.black))
            showBtn.setTextColor(resources.getColor(R.color.split_bg))
        }
        else{
            hideBtn.setTextColor(resources.getColor(R.color.split_bg))
            showBtn.setTextColor(resources.getColor(R.color.black))
        }
        filterLinear.setOnClickListener(View.OnClickListener {

            if (calendarArrayList.size > 0) {
                showTriggerDataCollection(mContext, R.drawable.questionmark_icon, R.drawable.round)
            }


        })

        nextBtn.setOnClickListener(View.OnClickListener {
            currentMonth = currentMonth + 1
            if (currentMonth > 11) {
                currentMonth = currentMonth - 12
                year = year + 1

            }
            isHide=false
            isShow=true
            if(!isHide && isShow)
            {
                hideBtn.setTextColor(resources.getColor(R.color.black))
                showBtn.setTextColor(resources.getColor(R.color.split_bg))
            }
            else{
                hideBtn.setTextColor(resources.getColor(R.color.split_bg))
                showBtn.setTextColor(resources.getColor(R.color.black))
            }
            month(currentMonth, year)
            showCalendarEvent(
                isAllSelected,
                isPrimarySelected,
                isSecondarySeleted,
                isWholeSchoolSelected
            )

        })
        hideBtn.setOnClickListener(View.OnClickListener {
            hideBtn.setTextColor(resources.getColor(R.color.split_bg))
            showBtn.setTextColor(resources.getColor(R.color.black))
            isHide=true
            isShow=false
            showCalendarEvent(
                isAllSelected,
                isPrimarySelected,
                isSecondarySeleted,
                isWholeSchoolSelected
            )
        })

        showBtn.setOnClickListener(View.OnClickListener {
            hideBtn.setTextColor(resources.getColor(R.color.black))
            showBtn.setTextColor(resources.getColor(R.color.split_bg))
            isHide=false
            isShow=true
            showCalendarEvent(
                isAllSelected,
                isPrimarySelected,
                isSecondarySeleted,
                isWholeSchoolSelected
            )
        })
        previousBtn.setOnClickListener(View.OnClickListener {
            isHide=false
            isShow=true
            if(!isHide && isShow)
            {
                hideBtn.setTextColor(resources.getColor(R.color.black))
                showBtn.setTextColor(resources.getColor(R.color.split_bg))
            }
            else{
                hideBtn.setTextColor(resources.getColor(R.color.split_bg))
                showBtn.setTextColor(resources.getColor(R.color.black))
            }
            if (currentMonth == 0) {
                currentMonth = 11 - currentMonth
                year = year - 1
            } else {
                currentMonth = currentMonth - 1
            }
            month(currentMonth, year)
            showCalendarEvent(
                isAllSelected,
                isPrimarySelected,
                isSecondarySeleted,
                isWholeSchoolSelected
            )

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

    fun callCalendarApi() {
        calendarArrayList = ArrayList()
        primaryArrayList = ArrayList()
        secondaryArrayList = ArrayList()
        wholeSchoolArrayList = ArrayList()
        progressDialog.visibility = View.VISIBLE
        val call: Call<CalendarModel> =
            ApiClient.getClient.calendarList()
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
                    if (calendarArrayList.size > 0) {
                        for (i in 0..calendarArrayList.size - 1) {
                            if (calendarArrayList.get(i).title.equals("Primary Event")) {
                                if (calendarArrayList.get(i).calendarDetail.cal.VEVENT.size > 0) {
                                    primaryArrayList.addAll(calendarArrayList.get(i).calendarDetail.cal.VEVENT)
                                    primaryColor = calendarArrayList.get(i).color
                                }

                            } else if (calendarArrayList.get(i).title.equals("Secondary Event")) {
                                if (calendarArrayList.get(i).calendarDetail.cal.VEVENT.size > 0) {

                                    secondaryArrayList.addAll(calendarArrayList.get(i).calendarDetail.cal.VEVENT)
                                    secondaryColor = calendarArrayList.get(i).color
                                }

                            } else if (calendarArrayList.get(i).title.equals("Whole School Event")) {
                                if (calendarArrayList.get(i).calendarDetail.cal.VEVENT.size > 0) {
                                    wholeSchoolArrayList.addAll(calendarArrayList.get(i).calendarDetail.cal.VEVENT)
                                    wholeSchoole = calendarArrayList.get(i).color
                                }

                            }

                            isAllSelected = true
                            isPrimarySelected = true
                            isSecondarySeleted = true
                            isWholeSchoolSelected = true
                            showCalendarEvent(
                                isAllSelected,
                                isPrimarySelected,
                                isSecondarySeleted,
                                isWholeSchoolSelected
                            )
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
                                var whiteColor = "#000000"
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
                    } else {


                    }

                } else {

                }
            }

        })
    }

    @SuppressLint("SimpleDateFormat")
    fun showCalendarEvent(
        allSeleted: Boolean,
        primarySelected: Boolean,
        secondarySelected: Boolean,
        wholeSchoolSelected: Boolean
    ) {
        primaryShowArrayList = ArrayList()
        secondaryShowArrayList = ArrayList()
        wholeSchoolShowArrayList = ArrayList()
        calendarFilterArrayList = ArrayList()
        if (primaryArrayList.size > 0) {
            for (i in 0..primaryArrayList.size - 1) {
                var pModel = PrimaryModel()
                if (primaryArrayList.get(i).DTSTART.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTSTART)
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    val enddate: Date = inputFormat.parse(primaryArrayList.get(i).DTEND)
                    var outputDateStrend: String = outputFormat.format(enddate)
                    pModel.DTSTART = result

                } else if (primaryArrayList.get(i).DTSTART.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTSTART)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    pModel.DTSTART = outputDateStrstart

                }
                if (primaryArrayList.get(i).DTEND.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTEND)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    pModel.DTEND = result

                } else if (primaryArrayList.get(i).DTEND.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(primaryArrayList.get(i).DTEND)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    pModel.DTEND = outputDateStrstart

                }

                pModel.SUMMARY = primaryArrayList.get(i).SUMMARY
                pModel.DESCRIPTION = primaryArrayList.get(i).DESCRIPTION
                pModel.LOCATION = primaryArrayList.get(i).LOCATION
                pModel.color = primaryColor
                pModel.type = 1
                primaryShowArrayList.add(pModel)
            }
        }
        if (secondaryArrayList.size > 0) {
            for (i in 0..secondaryArrayList.size - 1) {
                var sModel = PrimaryModel()
                if (secondaryArrayList.get(i).DTSTART.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTSTART)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    sModel.DTSTART = result

                } else if (secondaryArrayList.get(i).DTSTART.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTSTART)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    sModel.DTSTART = outputDateStrstart

                }
                if (secondaryArrayList.get(i).DTEND.equals("null")) {
                    sModel.DTEND = ""
                } else if (secondaryArrayList.get(i).DTEND.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTEND)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    sModel.DTEND = result

                } else if (secondaryArrayList.get(i).DTEND.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(secondaryArrayList.get(i).DTEND)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    sModel.DTEND = outputDateStrstart

                }
                sModel.SUMMARY = secondaryArrayList.get(i).SUMMARY
                sModel.DESCRIPTION = secondaryArrayList.get(i).DESCRIPTION
                sModel.LOCATION = secondaryArrayList.get(i).LOCATION
                sModel.color = secondaryColor
                sModel.type = 2
                secondaryShowArrayList.add(sModel)
            }
        }
        if (wholeSchoolArrayList.size > 0) {
            for (i in 0..wholeSchoolArrayList.size - 1) {
                var wModel = PrimaryModel()
                if (wholeSchoolArrayList.get(i).DTSTART.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTSTART)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    wModel.DTSTART = result

                } else if (wholeSchoolArrayList.get(i).DTSTART.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTSTART)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    wModel.DTSTART = outputDateStrstart

                }
                if (wholeSchoolArrayList.get(i).DTEND.toString().length == 16) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd'T'HHmmss'Z'")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTEND)
                    var tz: TimeZone = TimeZone.getTimeZone("GMT+09:30")
                    outputFormat.timeZone = tz
                    var result = outputFormat.format(startdate)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    wModel.DTEND = result

                } else if (wholeSchoolArrayList.get(i).DTEND.toString().length == 8) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                    val startdate: Date = inputFormat.parse(wholeSchoolArrayList.get(i).DTEND)
                    var outputDateStrstart: String = outputFormat.format(startdate)
                    wModel.DTEND = outputDateStrstart

                }

                wModel.SUMMARY = wholeSchoolArrayList.get(i).SUMMARY
                wModel.DESCRIPTION = wholeSchoolArrayList.get(i).DESCRIPTION
                wModel.LOCATION = wholeSchoolArrayList.get(i).LOCATION
                wModel.color = wholeSchoole
                wModel.type = 3
                wholeSchoolShowArrayList.add(wModel)
            }
        }

        if (allSeleted) {
            calendarShowArrayList = ArrayList()
            if (primaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }
            if (secondaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }
            if (wholeSchoolShowArrayList.size > 0) {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        } else if (!allSeleted && !primarySelected && !secondarySelected && !wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            var dummy = ArrayList<PrimaryModel>()
            calendarShowArrayList = dummy
        } else if (!allSeleted && !primarySelected && !secondarySelected && wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (wholeSchoolShowArrayList.size > 0) {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        } else if (!allSeleted && !primarySelected && secondarySelected && !wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (secondaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }

        } else if (!allSeleted && !primarySelected && secondarySelected && wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (secondaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }
            if (wholeSchoolShowArrayList.size > 0) {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        } else if (!allSeleted && primarySelected && !secondarySelected && !wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (primaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }

        } else if (!allSeleted && primarySelected && !secondarySelected && wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (primaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }
            if (wholeSchoolShowArrayList.size > 0) {
                calendarShowArrayList.addAll(wholeSchoolShowArrayList)
            }

        } else if (!allSeleted && primarySelected && secondarySelected && !wholeSchoolSelected) {
            calendarShowArrayList = ArrayList()
            if (primaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(primaryShowArrayList)
            }
            if (secondaryShowArrayList.size > 0) {
                calendarShowArrayList.addAll(secondaryShowArrayList)
            }

        }
        if(calendarShowArrayList.size>0)
        {
            FILTERCALENDARlIST=ArrayList()
            for (n in 0..calendarShowArrayList.size-1)
            {
                val sdf = SimpleDateFormat("MMM dd,yyyy")
                try {

                    val d1 = sdf.parse(calendarShowArrayList[n].DTSTART)
                    var d2 :Date?=null
                    if(calendarShowArrayList[n].DTEND.equals(""))
                    {
                        d2= sdf.parse(calendarShowArrayList[n].DTSTART)
                    }
                    else
                    {
                        d2 = sdf.parse(calendarShowArrayList[n].DTEND)
                    }


                    val difference_In_Time = d2.time - d1.time

                    difference_In_Days =
                        (((difference_In_Time / (1000 * 60 * 60 * 24)) % 365).toString())
                } catch (e: ParseException) {
                    e.printStackTrace()
                }

                if (difference_In_Days.toInt() > 1)
                {
                    var start: LocalDate? = null
                    var end: LocalDate? = null
                    for (j in 0 until difference_In_Days.toInt()) {
                        if (calendarShowArrayList.get(n).DTSTART.length == 20) {
                            val inputFormat: DateFormat =
                                SimpleDateFormat("MMM dd,yyyy hh:mm a")
                            val targetFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                            val outputFormatYear: DateFormat = SimpleDateFormat("yyyy")
                            val outputFormatMonth: DateFormat = SimpleDateFormat("MMMM")
                            val startdate: Date =
                                inputFormat.parse(calendarShowArrayList.get(n).DTSTART)
                            var outputDateMonth: String =
                                outputFormatMonth.format(startdate)
                            var outputDateYear: String = outputFormatYear.format(startdate)
                            if (calendarShowArrayList[n].DTSTART.length == 20) {
                                val datestart =
                                    inputFormat.parse(calendarShowArrayList[n].DTSTART)
                                val formattedDatestart = targetFormat.format(datestart)
                                val dateend =
                                    inputFormat.parse(calendarShowArrayList[n].DTEND)
                                val formattedDateend = targetFormat.format(dateend)
                                start = LocalDate.parse(formattedDatestart)
                                end = LocalDate.parse(formattedDateend)
                            } else if (calendarShowArrayList[n].DTSTART.length == 11) {
                                val inputFormat: DateFormat =
                                    SimpleDateFormat("MMM dd,yyyy")
                                val targetFormat: DateFormat =
                                    SimpleDateFormat("yyyy-MM-dd")
                                val datestart =
                                    inputFormat.parse(calendarShowArrayList[n].DTSTART)
                                val formattedDatestart = targetFormat.format(datestart)
                                val dateend =
                                    inputFormat.parse(calendarShowArrayList[n].DTEND)
                                val formattedDateend = targetFormat.format(dateend)
                                start = LocalDate.parse(formattedDatestart)
                                end = LocalDate.parse(formattedDateend)
                            }

                        }
                        else if (calendarShowArrayList.get(n).DTSTART.length == 11) {
                            val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                            val targetFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                            val outputFormatYear: DateFormat = SimpleDateFormat("yyyy")
                            val outputFormatMonth: DateFormat = SimpleDateFormat("MMMM")
                            val startdate: Date =
                                inputFormat.parse(calendarShowArrayList.get(n).DTSTART)
                            var outputDateMonth: String =
                                outputFormatMonth.format(startdate)
                            var outputDateYear: String = outputFormatYear.format(startdate)
                            val datestart =
                                inputFormat.parse(calendarShowArrayList[n].DTSTART)
                            val formattedDatestart = targetFormat.format(datestart)
                            val dateend =
                                inputFormat.parse(calendarShowArrayList[n].DTEND)
                            val formattedDateend = targetFormat.format(dateend)
                            start = LocalDate.parse(formattedDatestart)
                            end = LocalDate.parse(formattedDateend)
//                            listMonth = outputDateMonth
//                            listYear = outputDateYear
                        }


                        val totalDates: ArrayList<LocalDate> = ArrayList()
                        while (!start!!.isAfter(end)) {
                            totalDates.add(start);
                            start = start.plusDays(1);
                        }

                        val datesArray: ArrayList<String> = ArrayList()

                        for (k in 0..totalDates.size - 1) {
                            datesArray.add(totalDates[k].toString())

                        }
                        Log.e("MULTIPLE DATES",datesArray.get(j).toString())
                        var model: PrimaryModel = PrimaryModel()
                        model.DTSTART= datesArray.get(j).toString()
                        model.DESCRIPTION=calendarShowArrayList.get(n).DESCRIPTION
                        model.DTEND=calendarShowArrayList.get(n).DTEND
                        model.LOCATION=calendarShowArrayList.get(n).LOCATION
                        model.SUMMARY=calendarShowArrayList.get(n).SUMMARY
                        model.color=calendarShowArrayList.get(n).color
                        model.type=calendarShowArrayList.get(n).type
                        FILTERCALENDARlIST.add(model)
                    }
                }
                else{
                    FILTERCALENDARlIST.add(calendarShowArrayList[n])
                }



            }

            if (FILTERCALENDARlIST.size>0)
            {
                var listMonth: String = ""
                var listYear: String = ""
                for (i in 0..FILTERCALENDARlIST.size-1)
                {
                    if (FILTERCALENDARlIST.get(i).DTSTART.length == 20) {
                        val inputFormat: DateFormat =
                            SimpleDateFormat("MMM dd,yyyy hh:mm a")
                        val outputFormatYear: DateFormat = SimpleDateFormat("yyyy")
                        val outputFormatMonth: DateFormat = SimpleDateFormat("MMMM")
                        val startdate: Date =
                            inputFormat.parse(FILTERCALENDARlIST.get(i).DTSTART)
                        var outputDateMonth: String =
                            outputFormatMonth.format(startdate)
                        var outputDateYear: String = outputFormatYear.format(startdate)
                        listMonth = outputDateMonth
                        listYear = outputDateYear
                    } else if (FILTERCALENDARlIST.get(i).DTSTART.length == 11) {
                        val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                        val outputFormatYear: DateFormat = SimpleDateFormat("yyyy")
                        val outputFormatMonth: DateFormat = SimpleDateFormat("MMMM")
                        val startdate: Date =
                            inputFormat.parse(FILTERCALENDARlIST.get(i).DTSTART)
                        var outputDateMonth: String =
                            outputFormatMonth.format(startdate)
                        var outputDateYear: String = outputFormatYear.format(startdate)
                        listMonth = outputDateMonth
                        listYear = outputDateYear
                    }
                    else if (FILTERCALENDARlIST.get(i).DTSTART.length==10)
                    {
                        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                        val outputFormatYear: DateFormat = SimpleDateFormat("yyyy")
                        val outputFormatMonth: DateFormat = SimpleDateFormat("MMMM")
                        val startdate: Date =
                            inputFormat.parse(FILTERCALENDARlIST.get(i).DTSTART)
                        var outputDateMonth: String =
                            outputFormatMonth.format(startdate)
                        var outputDateYear: String = outputFormatYear.format(startdate)
                        listMonth = outputDateMonth
                        listYear = outputDateYear
                    }
                    if (listYear.equals(year.toString())) {
                        if (monthTxt.equals(listMonth)) {
                            var model = PrimaryModel()
                            if (FILTERCALENDARlIST.get(i).DTSTART.length==10)
                            {
                                val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                                val outPutFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                                val startdate: Date =
                                    inputFormat.parse(FILTERCALENDARlIST.get(i).DTSTART)
                                var outputStart: String = outPutFormat.format(startdate)
                                model.DTSTART = outputStart
                            }
                            else
                            {
                                model.DTSTART = FILTERCALENDARlIST.get(i).DTSTART
                            }


                            model.DTEND = FILTERCALENDARlIST.get(i).DTEND
                            model.SUMMARY = FILTERCALENDARlIST.get(i).SUMMARY
                            model.DESCRIPTION = FILTERCALENDARlIST.get(i).DESCRIPTION
                            model.LOCATION = FILTERCALENDARlIST.get(i).LOCATION
                            model.color = FILTERCALENDARlIST.get(i).color
                            model.type = FILTERCALENDARlIST.get(i).type
                            calendarFilterArrayList.add(model)
                        }

                    }

                }


                if (calendarFilterArrayList.size>0)
                {
                    calendarRecycler.visibility = View.VISIBLE
                    calendarFilterArrayList.sortByDescending { calendarFilterArrayList -> calendarFilterArrayList.DTSTART }
                    calendarFilterArrayList.reverse()
                    mCalendarFinalArrayList = ArrayList()
                    for (n in 0 until calendarFilterArrayList.size)
                    {
                        if (mCalendarFinalArrayList.size==0)
                        {
                            Log.e("CALENDAR WORK","SIZE 0")
                            var cModel = CalendarDateModel()
                            cModel.startDate = calendarFilterArrayList.get(n).DTSTART
                            cModel.endDate = calendarFilterArrayList.get(n).DTEND
                            var calendarDetaiArray = ArrayList<CalendarDetailModel>()
                            var dModel = CalendarDetailModel()
                            dModel.DTSTART = calendarFilterArrayList.get(n).DTSTART
                            dModel.DTEND = calendarFilterArrayList.get(n).DTEND
                            dModel.SUMMARY = calendarFilterArrayList.get(n).SUMMARY
                            dModel.DESCRIPTION = calendarFilterArrayList.get(n).DESCRIPTION
                            dModel.LOCATION = calendarFilterArrayList.get(n).LOCATION
                            dModel.color = calendarFilterArrayList.get(n).color
                            dModel.type = calendarFilterArrayList.get(n).type
                            calendarDetaiArray.add(dModel)
                            cModel.detailList = calendarDetaiArray
                            mCalendarFinalArrayList.add(cModel)
                        }
                        else
                        {
                            Log.e("CALENDAR WORK",mCalendarFinalArrayList.size.toString())
                            var isFound:Boolean=false
                            var pos:Int=-1
                            for (o in 0 until mCalendarFinalArrayList.size)
                            {
                                Log.e("DATE ",calendarFilterArrayList.get(n).DTSTART+"  :: "+mCalendarFinalArrayList.get(o).startDate)
                                if (calendarFilterArrayList.get(n).DTSTART.equals(mCalendarFinalArrayList.get(o).startDate))
                                {
                                    Log.e("FOUND","DATE FOUND")
                                    isFound=true
                                    pos=o
                                }

                            }

                            if (!isFound) {
                                var cModel = CalendarDateModel()
                                cModel.startDate = calendarFilterArrayList.get(n).DTSTART
                                cModel.endDate = calendarFilterArrayList.get(n).DTEND
                                var calendarDetaiArray = ArrayList<CalendarDetailModel>()
                                var dModel = CalendarDetailModel()
                                dModel.DTSTART = calendarFilterArrayList.get(n).DTSTART
                                dModel.DTEND = calendarFilterArrayList.get(n).DTEND
                                dModel.SUMMARY = calendarFilterArrayList.get(n).SUMMARY
                                dModel.DESCRIPTION = calendarFilterArrayList.get(n).DESCRIPTION
                                dModel.LOCATION = calendarFilterArrayList.get(n).LOCATION
                                dModel.color = calendarFilterArrayList.get(n).color
                                dModel.type = calendarFilterArrayList.get(n).type
                                calendarDetaiArray.add(dModel)
                                cModel.detailList = calendarDetaiArray
                                mCalendarFinalArrayList.add(cModel)

                            } else {

                                var dModel = CalendarDetailModel()
                                dModel.DTSTART = calendarFilterArrayList.get(n).DTSTART
                                dModel.DTEND = calendarFilterArrayList.get(n).DTEND
                                dModel.SUMMARY = calendarFilterArrayList.get(n).SUMMARY
                                dModel.DESCRIPTION = calendarFilterArrayList.get(n).DESCRIPTION
                                dModel.LOCATION = calendarFilterArrayList.get(n).LOCATION
                                dModel.color = calendarFilterArrayList.get(n).color
                                dModel.type = calendarFilterArrayList.get(n).type
                                mCalendarFinalArrayList.get(pos).detailList.add(dModel)

                            }
                        }
                    }
                    monthLinear.visibility=View.VISIBLE
                    filterLinear.visibility=View.VISIBLE
                    hidePast.visibility=View.VISIBLE
                    if (isHide && !isShow)
                    {
                        var calendar=ArrayList<CalendarDateModel>()
                        if (mCalendarFinalArrayList.size>0)
                        {
                            for (s in 0 until mCalendarFinalArrayList.size)
                            {
                                if(mCalendarFinalArrayList.get(s).startDate.length==20)
                                {
                                    var sdf=SimpleDateFormat("MMM dd,yyyy hh:mm a")
                                    var strDate=sdf.parse(mCalendarFinalArrayList.get(s).startDate)
                                    if (System.currentTimeMillis()<strDate.time)
                                    {

                                        var model=CalendarDateModel()
                                        model.startDate=mCalendarFinalArrayList.get(s).startDate
                                        model.endDate=mCalendarFinalArrayList.get(s).endDate
                                        model.detailList=mCalendarFinalArrayList.get(s).detailList
                                        calendar.add(model)

                                    }
                                }
                                else if(mCalendarFinalArrayList.get(s).startDate.length==11)
                                {
                                    var sdf=SimpleDateFormat("MMM dd,yyyy")
                                    var strDate=sdf.parse(mCalendarFinalArrayList.get(s).startDate)
                                    if (System.currentTimeMillis()<strDate.time)
                                    {

                                        var model=CalendarDateModel()
                                        model.startDate=mCalendarFinalArrayList.get(s).startDate
                                        model.endDate=mCalendarFinalArrayList.get(s).endDate
                                        model.detailList=mCalendarFinalArrayList.get(s).detailList
                                        calendar.add(model)

                                    }
                                }
                                else if(mCalendarFinalArrayList.get(s).startDate.length==10)
                                {
                                    var sdf=SimpleDateFormat("yyyy-MM-dd")
                                    var strDate=sdf.parse(mCalendarFinalArrayList.get(s).startDate)
                                    if (System.currentTimeMillis()<strDate.time)
                                    {

                                        var model=CalendarDateModel()
                                        model.startDate=mCalendarFinalArrayList.get(s).startDate
                                        model.endDate=mCalendarFinalArrayList.get(s).endDate
                                        model.detailList=mCalendarFinalArrayList.get(s).detailList
                                        calendar.add(model)

                                    }
                                }




                            }
                        }
                        if (calendar.size>0)
                        {
                            noEventImage.visibility=View.GONE
                            noEventTxt.visibility=View.GONE
                            calendarRecycler.visibility=View.VISIBLE
                            val calendarListAdapter = CalendarDateAdapter(mContext, calendar)
                            calendarRecycler.adapter = calendarListAdapter
                        }
                        else
                        {
                            noEventImage.visibility=View.VISIBLE
                            noEventTxt.visibility=View.VISIBLE
                            calendarRecycler.visibility=View.GONE
                        }
                    }
                   else
                    {
                        Log.e("SIZE::::",mCalendarFinalArrayList.size.toString())
                        if (mCalendarFinalArrayList.size>0)
                        {
                            noEventImage.visibility=View.GONE
                            noEventTxt.visibility=View.GONE
                            calendarRecycler.visibility=View.VISIBLE
                            val calendarListAdapter = CalendarDateAdapter(mContext, mCalendarFinalArrayList)
                            calendarRecycler.adapter = calendarListAdapter
                        }
                        else
                        {
                            noEventImage.visibility=View.VISIBLE
                            noEventTxt.visibility=View.VISIBLE
                            calendarRecycler.visibility=View.GONE
                        }
                    }




                }
                else
                {
                    noEventImage.visibility=View.VISIBLE
                    noEventTxt.visibility=View.VISIBLE
                    calendarRecycler.visibility=View.GONE
                }
            }
            else
            {
                noEventImage.visibility=View.VISIBLE
                noEventTxt.visibility=View.VISIBLE
                calendarRecycler.visibility=View.GONE
            }
        }
        else
        {
            noEventImage.visibility=View.VISIBLE
            noEventTxt.visibility=View.VISIBLE
            calendarRecycler.visibility=View.GONE
        }
    }


    fun month(month: Int, year: Int) {
        when (month) {
            0 -> {
                monthTxt = "January"
                monthYearTxt.setText(monthTxt +" "+ year.toString())

            }

            1 -> {
                monthTxt = "February"
                monthYearTxt.setText(monthTxt +" "+ year.toString())
            }

            2 -> {
                monthTxt = "March"
                monthYearTxt.setText(monthTxt +" "+ year.toString())
            }

            3 -> {
                monthTxt = "April"
                monthYearTxt.setText(monthTxt +" "+ year.toString())
            }

            4 -> {
                monthTxt = "May"
                monthYearTxt.setText(monthTxt +" "+ year.toString())
            }

            5 -> {
                monthTxt = "June"
                monthYearTxt.setText(monthTxt +" "+ year.toString())
            }

            6 -> {
                monthTxt = "July"
                monthYearTxt.setText(monthTxt +" "+ year.toString())
            }

            7 -> {
                monthTxt = "August"
                monthYearTxt.setText(monthTxt +" "+ year.toString())
            }

            8 -> {
                monthTxt = "September"
                monthYearTxt.setText(monthTxt  +" "+ year.toString())
            }

            9 -> {
                monthTxt = "October"
                monthYearTxt.setText(monthTxt  +" "+ year.toString())
            }

            10 -> {
                monthTxt = "November"
                monthYearTxt.setText(monthTxt  +" "+ year.toString())
            }

            11 -> {
                monthTxt = "December"
                monthYearTxt.setText(monthTxt  +" "+ year.toString())
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

        var triggerAdapter = CategoryAdapter(mContext,mTriggerModelArrayList)
        checkRecycler.setAdapter(triggerAdapter)
        btn_Cancel.setOnClickListener(View.OnClickListener {
            dialog.dismiss()
        })
        btn_Ok.setOnClickListener(View.OnClickListener {
            isHide=false
            isShow=true
            if(!isHide && isShow)
            {
                hideBtn.setTextColor(resources.getColor(R.color.black))
                showBtn.setTextColor(resources.getColor(R.color.split_bg))
            }
            else{
                hideBtn.setTextColor(resources.getColor(R.color.split_bg))
                showBtn.setTextColor(resources.getColor(R.color.black))
            }
            showCalendarEvent(
                isAllSelected,
                isPrimarySelected,
                isSecondarySeleted,
                isWholeSchoolSelected
            )
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

                var triggerAdapter = CategoryAdapter(mContext,mTriggerModelArrayList)
                checkRecycler.setAdapter(triggerAdapter)

            }
        })
        dialog.show()
    }
}
