package com.mobatia.bisad.activity.calendar.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.calendar.adapter.CalendarListRecyclerAdapter
import com.mobatia.bisad.fragment.calendar.model.CalendarListDetailModel
import com.mobatia.bisad.fragment.calendar.model.CalendarListResponse
import com.mobatia.bisad.fragment.calendar.model.VEVENT

class CalendarDetailAdapter (private var calendarArrayList: List<VEVENT>) :
    RecyclerView.Adapter<CalendarDetailAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_calender_list, parent, false)
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val summary = calendarArrayList[position]

        holder.title.text = summary.SUMMARY

    }
    override fun getItemCount(): Int {

        return calendarArrayList.size

    }
}