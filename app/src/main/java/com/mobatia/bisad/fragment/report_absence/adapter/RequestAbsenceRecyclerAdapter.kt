package com.mobatia.bisad.fragment.report_absence.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.report_absence.model.AbsenceRequestListDetailModel
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

internal class RequestAbsenceRecyclerAdapter (private var studentInfoList: List<AbsenceRequestListDetailModel>) :
    RecyclerView.Adapter<RequestAbsenceRecyclerAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var listDate: TextView = view.findViewById(R.id.listDate)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_absence_leave_recycelr, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = studentInfoList[position]

        val fromDate=movie.from_date
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val inputDateStr = fromDate
        val date: Date = inputFormat.parse(inputDateStr)
        val outputDateStr: String = outputFormat.format(date)

        val toDate=movie.to_date
        val inputFormat1: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat1: DateFormat = SimpleDateFormat("dd MMM yyyy")
        val inputDateStr1 = toDate
        val date1: Date = inputFormat1.parse(inputDateStr1)
        val outputDateStr1: String = outputFormat1.format(date1)
        holder.listDate.text = outputDateStr+" - "+outputDateStr1

    }
    override fun getItemCount(): Int {
        return studentInfoList.size
    }
}