package com.mobatia.bisad.fragment.time_table.adapter

import android.animation.ObjectAnimator
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.BounceInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.time_table.model.usagemodel.WeekModel


class TimeTableWeekListAdapter (private var weekArrayList: List<WeekModel>) :
    RecyclerView.Adapter<TimeTableWeekListAdapter.MyViewHolder>() {
     inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var weekTxt: TextView = view.findViewById(R.id.weekTxt)
        var lineImage: ImageView = view.findViewById(R.id.lineImage)
        var downArrowImage: ImageView = view.findViewById(R.id.downArrowImage)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_week_list, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = weekArrayList[position]
        holder.weekTxt.text = movie.weekName
       if (weekArrayList.get(position).positionSelected!=-1)
       {
           holder.weekTxt.setTextColor(mContext.resources.getColor(R.color.timetableblue))
           holder.lineImage.visibility=View.VISIBLE
           holder.downArrowImage.visibility=View.VISIBLE
//           val buttonAnimator =
//               ObjectAnimator.ofFloat(holder.lineImage, "translationX", 0f, 400f)
//           buttonAnimator.duration = 3000
//           buttonAnimator.interpolator = BounceInterpolator()
//           buttonAnimator.start()
       }
        else
       {
           holder.weekTxt.setTextColor(mContext.resources.getColor(R.color.dark_grey1))
           holder.lineImage.visibility=View.INVISIBLE
           holder.downArrowImage.visibility=View.INVISIBLE
       }
    }
    override fun getItemCount(): Int {

        return weekArrayList.size

    }
}