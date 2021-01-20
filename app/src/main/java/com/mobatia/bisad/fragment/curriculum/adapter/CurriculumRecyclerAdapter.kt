package com.mobatia.bisad.fragment.curriculum.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.WebviewLoad
import com.mobatia.bisad.fragment.calendar.adapter.CalendarListRecyclerAdapter
import com.mobatia.bisad.fragment.calendar.model.VEVENT
import com.mobatia.bisad.fragment.curriculum.CurriculumDetail
import com.mobatia.bisad.fragment.curriculum.model.CuriculumResponseArray
import com.mobatia.bisad.fragment.home.mContext

class CurriculumRecyclerAdapter (private var context:Context,private var calendarArrayList: List<CuriculumResponseArray>) :
    RecyclerView.Adapter<CurriculumRecyclerAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var relativeMain: RelativeLayout = view.findViewById(R.id.relativeMain)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_calender_list, parent, false)
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val summary = calendarArrayList[position]

        holder.title.text = summary.title
        holder.title.setOnClickListener(View.OnClickListener {
            Log.e("WORKING CLICK ADAPTER","ADAP")
            context.startActivity(Intent(context, CurriculumDetail::class.java).putExtra("Url",calendarArrayList.get(position).file))

        })
    }
    override fun getItemCount(): Int {

        return calendarArrayList.size

    }
}