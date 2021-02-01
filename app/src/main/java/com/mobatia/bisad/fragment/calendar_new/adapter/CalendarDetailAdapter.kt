package com.mobatia.calendardemopro.adapter

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.annotation.NonNull
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.calendar_new.model.CalendarDetailModel
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class CalendarDetailAdapter(private var mContext:Context,private var calendarArrayList: ArrayList<CalendarDetailModel>) :
    RecyclerView.Adapter<CalendarDetailAdapter.MyViewHolder>() {

    lateinit var difference_In_Days: String
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var timeTxt: TextView = view.findViewById(R.id.timeTxt)
        var backReal: RelativeLayout = view.findViewById(R.id.backReal)
        var clickLinear: LinearLayout = view.findViewById(R.id.clickLinear)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_calender_list_new, parent, false)
        return MyViewHolder(itemView)
    }


    @SuppressLint("SimpleDateFormat")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val summary = calendarArrayList[position]
        Log.e("POSITONVDALUE : ", position.toString())

        holder.title.text = summary.SUMMARY
        if (summary.DTSTART.length!=0)
        {
            if (summary.DTEND.length!=0)
            {
                if (summary.DTSTART.length==20)
                {
                    val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val outputFormat: DateFormat = SimpleDateFormat("hh:mm a")
                    val startdate: Date = inputFormat.parse(summary.DTSTART)
                    var outputDateStrstart:String= outputFormat.format(startdate)
                    if (summary.DTEND.length==20)
                    {
                        val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                        val outputFormat: DateFormat = SimpleDateFormat("hh:mm a")
                        val endDate: Date = inputFormat.parse(summary.DTEND)
                        var outputDateEND:String= outputFormat.format(endDate)
                        holder.timeTxt.text = outputDateStrstart+" - "+outputDateEND
                    }
                    else if (summary.DTEND.length==11)
                    {
                        holder.timeTxt.text = outputDateStrstart
                    }
                }
                else
                {
                    holder.timeTxt.text = "All day"
                }



            }
            else
            {
                if (summary.DTSTART.length==20)
                {
                    val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy hh:mm a")
                    val outputFormat: DateFormat = SimpleDateFormat("hh:mm a")
                    val startdate: Date = inputFormat.parse(summary.DTSTART)
                    var outputDateStrstart:String= outputFormat.format(startdate)
                    holder.timeTxt.text = outputDateStrstart
                    Log.e("calendardate:",outputDateStrstart)

                }
                else if (summary.DTSTART.length==11)
                {
                    holder.timeTxt.text = "All day"

                }
                else
                {
                    holder.timeTxt.text = summary.DTSTART
                }
            }

        }
       // holder.timeTxt.text = summary.DTSTART
        holder.backReal.setBackgroundColor(Color.parseColor(summary.color))

        holder.clickLinear.setOnClickListener(View.OnClickListener {

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
            description.visibility = View.GONE
            description.text = DescriptionCalendar
            start_text.setTextColor(mContext.let {
                ContextCompat.getColor(
                    it,
                    R.color.rel_one
                )
            })
            end_text.setTextColor(mContext.let {
                ContextCompat.getColor(
                    it,
                    R.color.rel_one
                )
            })

            if (StartCalendar.length == 20) {


                startcalendar.text = calendarArrayList[position].DTSTART
                endcalendar.text = calendarArrayList[position].DTEND



            }

            if (StartCalendar.length == 11) {
                val inputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")
                val outputFormat: DateFormat = SimpleDateFormat("MMM dd,yyyy")

                val startdate: Date = inputFormat.parse(calendarArrayList[position].DTSTART)
                val enddate: Date = inputFormat.parse(calendarArrayList[position].DTEND)

                outputDateStrstart = outputFormat.format(startdate)
                outputDateStrend = outputFormat.format(enddate)

                startcalendar.text = calendarArrayList[position].DTSTART
                endcalendar.text = calendarArrayList[position].DTEND
            }
            save_calendar.setOnClickListener {

                val calendar = Calendar.getInstance()
                calendar.timeZone = TimeZone.getDefault()

                if (StartCalendar.length == 16) {
                    try {
                        val startdatehelper =
                            SimpleDateFormat("MMM dd,yyyy hh:mm a").parse(calendarArrayList[position].DTSTART)
                        val stopdatehelper =
                            SimpleDateFormat("MMM dd,yyyy hh:mm a").parse(calendarArrayList[position].DTEND)

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
        })

    }
    override fun getItemCount(): Int {

        return calendarArrayList.size

    }
}