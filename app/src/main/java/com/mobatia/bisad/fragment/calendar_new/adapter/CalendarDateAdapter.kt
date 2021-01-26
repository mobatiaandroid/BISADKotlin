package com.mobatia.calendardemopro.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.calendar_new.model.CalendarDateModel
import com.mobatia.bisad.fragment.calendar_new.model.CalendarDetailModel
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

lateinit var linearLayoutManager: LinearLayoutManager
var startTime16format: Long = 0
var endTime16format: Long = 0
var startTime8format: Long = 0
var endTime8format: Long = 0
lateinit var outputDateStrstart: String
lateinit var outputDateStrend: String
lateinit var SummaryCalendar: String
lateinit var DescriptionCalendar: String

lateinit var StartCalendar: String
lateinit var EndCalendar: String
lateinit var dialogcalendar: Dialog
var detailArray=ArrayList<CalendarDetailModel>()
class CalendarDateAdapter (private var mContext: Context, private var calendarArrayList: List<CalendarDateModel>) :
    RecyclerView.Adapter<CalendarDateAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var dateTxt: TextView = view.findViewById(R.id.dateTxt)
        var detailRecycler: RecyclerView = view.findViewById(R.id.detailRecycler)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_date_recycler, parent, false)
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val summary = calendarArrayList[position]
        if(summary.startDate.length!=0)
        {
            if (summary.startDate.length==20)
            {
                val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                val outputFormat: DateFormat = SimpleDateFormat("EEEE dd MMMM")
                val startdate: Date = inputFormat.parse(summary.startDate)
                var outputDateStrstart:String= outputFormat.format(startdate)
                holder.dateTxt.text = outputDateStrstart
            }
            else if (summary.startDate.length==11)
            {
                val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                val outputFormat: DateFormat = SimpleDateFormat("EEEE dd MMMM")
                val startdate: Date = inputFormat.parse(summary.startDate)
                var outputDateStrstart:String= outputFormat.format(startdate)
                holder.dateTxt.text = outputDateStrstart

            }
            else
            {
                holder.dateTxt.text = summary.startDate
            }
        }

        linearLayoutManager = LinearLayoutManager(mContext)
        holder.detailRecycler.layoutManager = linearLayoutManager
        holder.detailRecycler.itemAnimator = DefaultItemAnimator()
        if (summary.detailList.size>0)
        {

            detailArray=summary.detailList
            val calendarListAdapter = CalendarDetailAdapter(detailArray)
            holder.detailRecycler.adapter = calendarListAdapter
        }
        holder.detailRecycler.addOnItemClickListener(object : OnItemClickListener {
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
                SummaryCalendar = detailArray[position].SUMMARY
                DescriptionCalendar = detailArray[position].DESCRIPTION
                StartCalendar = detailArray[position].DTSTART
                EndCalendar = detailArray[position].DTEND
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

                if (StartCalendar.length == 20)
                {


                    startcalendar.text = detailArray[position].DTSTART
                    endcalendar.text = detailArray[position].DTEND
                }

                if (StartCalendar.length == 11) {
                    val inputFormat: DateFormat = SimpleDateFormat("yyyyMMdd")
                    val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")

                    val startdate: Date = inputFormat.parse(detailArray[position].DTSTART)
                    val enddate: Date = inputFormat.parse(detailArray[position].DTEND)

                    outputDateStrstart = outputFormat.format(startdate)
                    outputDateStrend = outputFormat.format(enddate)

                    startcalendar.text = detailArray[position].DTSTART
                    endcalendar.text = detailArray[position].DTEND
                }
                save_calendar.setOnClickListener {

                    val calendar = Calendar.getInstance()
                    calendar.timeZone = TimeZone.getDefault()

                    if (StartCalendar.length == 16) {
                        try {
                            val startdatehelper =
                                SimpleDateFormat("MMM dd,yyyy hh:mm a").parse(detailArray[position].DTSTART)
                            val stopdatehelper =
                                SimpleDateFormat("MMM dd,yyyy hh:mm a").parse(detailArray[position].DTEND)

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
                        mContext.startActivity(intent)
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
                        mContext.startActivity(intent)
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
    override fun getItemCount(): Int {

        return calendarArrayList.size

    }
}