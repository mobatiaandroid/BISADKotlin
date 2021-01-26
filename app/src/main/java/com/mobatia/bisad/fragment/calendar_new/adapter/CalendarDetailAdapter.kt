package com.mobatia.calendardemopro.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.calendar_new.model.CalendarDetailModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CalendarDetailAdapter(private var calendarArrayList: ArrayList<CalendarDetailModel>) :
    RecyclerView.Adapter<CalendarDetailAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var timeTxt: TextView = view.findViewById(R.id.timeTxt)
        var backReal: RelativeLayout = view.findViewById(R.id.backReal)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_calender_list_new, parent, false)
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val summary = calendarArrayList[position]

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

    }
    override fun getItemCount(): Int {

        return calendarArrayList.size

    }
}