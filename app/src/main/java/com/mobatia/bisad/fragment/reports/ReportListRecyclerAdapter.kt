package com.mobatia.bisad.fragment.reports

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.WebviewLoad
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.reports.model.ReportListDetailModel
import com.mobatia.bisad.fragment.reports.model.ReportResponseArray

internal class ReportListRecyclerAdapter(private var reportslist: List<ReportResponseArray>):
    RecyclerView.Adapter<ReportListRecyclerAdapter.MyViewHolder>() {

    lateinit var clickedurl:String


    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var report_cycle:TextView = view.findViewById(R.id.report_cycle)
        var relativeclick:RelativeLayout = view.findViewById(R.id.relativeclick)
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
        holder.report_cycle.text = reportslist[position].data[0].report_cycle

        holder.relativeclick.setOnClickListener {

            clickedurl = reportslist[position].data[0].file
            //var models: ReportListDetailModel = reportslist[position]
//            Log.e("PDF", reportslist[position].data[position].file)

           mContext.startActivity(Intent(mContext,WebviewLoad::class.java).putExtra("Url",reportslist[position].data[0].file))
        }

    }
}