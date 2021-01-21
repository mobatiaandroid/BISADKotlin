package com.mobatia.bisad.fragment.time_table.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.time_table.model.usagemodel.DayModel

class TimeTablePopUpRecyclerAdapter (private var mContetx: Context, private var timeTableList: ArrayList<DayModel>) :
    RecyclerView.Adapter<TimeTablePopUpRecyclerAdapter.MyViewHolder>() {
     inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var dateTimeTextView: TextView = view.findViewById(R.id.dateTimeTextView)
        var nameTextView: TextView = view.findViewById(R.id.nameTextView)
        var subjectNameTextView: TextView = view.findViewById(R.id.subjectNameTextView)
        var periodTextView: TextView = view.findViewById(R.id.periodTextView)

    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_timetable_popup, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val summary = timeTableList[position]
        Log.e("START TIME",timeTableList.get(position).starttime)
        if (timeTableList.get(position).day.equals(""))
        {

            holder.dateTimeTextView.visibility=View.GONE
        }
        else{
            if (timeTableList.get(position).starttime.equals(""))
            {
                holder.dateTimeTextView.setText(timeTableList.get(position).day)
            }
            else{
                holder.dateTimeTextView.setText(timeTableList.get(position).day+" | "+timeTableList.get(position).starttime+" - "+timeTableList.get(position).endtime)
            }
            holder.nameTextView.visibility=View.VISIBLE
            holder.dateTimeTextView.visibility=View.VISIBLE
          // holder.dateTimeTextView.setText(timeTableList.get(position).day)
        }
        if (position==0)
        {
         holder.periodTextView.setText(timeTableList.get(position).sortname)
        }
        else
        {
            holder.periodTextView.setText("")
        }

       holder.nameTextView.setText(timeTableList.get(position).subject_name)
       holder.subjectNameTextView.setText(timeTableList.get(position).staff)
        if (timeTableList.size>1)
        {
            if (position!= (timeTableList.size - 1))
            {
                holder.subjectNameTextView.setCompoundDrawablesWithIntrinsicBounds(null,null,null,mContetx.getResources().getDrawable(R.drawable.listdividewhite))

            }
            else{
                holder.subjectNameTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
            }
        }
        else{
            holder.subjectNameTextView.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }


    }
    override fun getItemCount(): Int {

        return timeTableList.size

    }
}