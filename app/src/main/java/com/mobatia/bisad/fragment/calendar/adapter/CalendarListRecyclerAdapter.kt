package com.mobatia.bisad.fragment.calendar.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.calendar.model.CalendarListResponse
import com.mobatia.bisad.fragment.calendar.model.VEVENT
import com.mobatia.bisad.manager.OnBottomReachedListener


 class CalendarListRecyclerAdapter (private var calendarArrayList: List<CalendarListResponse>) :
    RecyclerView.Adapter<CalendarListRecyclerAdapter.MyViewHolder>() {
     inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var realMain: RelativeLayout = view.findViewById(R.id.realMain)
        var card_view: CardView = view.findViewById(R.id.card_view)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_calendar_new, parent, false)
        return MyViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val summary = calendarArrayList[position]

        holder.title.text = summary.title
        holder.realMain.setBackgroundColor(Color.parseColor(summary.color))
     //   holder.card_view.setBackgroundColor(Color.parseColor(summary.color))

    }
    override fun getItemCount(): Int {

        return calendarArrayList.size

    }
}