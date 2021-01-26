package com.mobatia.calendardemopro.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.calendar_new.model.CalendarDateModel
import com.mobatia.bisad.fragment.calendar_new.model.CalendarDetailModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

lateinit var linearLayoutManager: LinearLayoutManager
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
            var detailArray=ArrayList<CalendarDetailModel>()
            detailArray=summary.detailList
            val calendarListAdapter = CalendarDetailAdapter(detailArray)
            holder.detailRecycler.adapter = calendarListAdapter
        }

    }
    override fun getItemCount(): Int {

        return calendarArrayList.size

    }
}