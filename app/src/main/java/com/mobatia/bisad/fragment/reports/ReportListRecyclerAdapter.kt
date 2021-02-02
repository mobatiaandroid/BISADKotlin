package com.mobatia.bisad.fragment.reports

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.WebviewLoad
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.reports.model.ReportListDetailModel
import com.mobatia.bisad.fragment.reports.model.ReportResponseArray
import com.mobatia.calendardemopro.adapter.CalendarDetailAdapter
import com.mobatia.calendardemopro.adapter.ReportDetailAdapter

class ReportListRecyclerAdapter(private var mContext:Context,private var reportslist: ArrayList<ReportResponseArray>):
    RecyclerView.Adapter<ReportListRecyclerAdapter.MyViewHolder>() {

    lateinit var clickedurl:String
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var detailArray:ArrayList<ReportListDetailModel>
     inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var reportRecycler: RecyclerView = view.findViewById(R.id.reportRecycler)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_report_list, parent, false)
        return MyViewHolder(itemView)    }

    override fun getItemCount(): Int {
        return reportslist.size

    }

    override fun onBindViewHolder(holder: ReportListRecyclerAdapter.MyViewHolder, position: Int) {
        holder.title.text = reportslist[position].Acyear
        linearLayoutManager = LinearLayoutManager(mContext)
        holder.reportRecycler.layoutManager = linearLayoutManager
        holder.reportRecycler.itemAnimator = DefaultItemAnimator()
        detailArray=ArrayList()
         if(reportslist[position].data.size>0)
         {
             detailArray=reportslist[position].data
             val reportCycleAdapter = ReportDetailAdapter(mContext,detailArray)
             holder.reportRecycler.adapter = reportCycleAdapter
         }

    }
}