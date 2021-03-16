package com.mobatia.bisad.fragment.teacher_contact.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.student_information.adapter.StudentListAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.fragment.teacher_contact.model.StaffInfoDetail

internal class StaffListAdapter (private var studentList: List<StaffInfoDetail>) :
    RecyclerView.Adapter<StaffListAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var listTxtTitle: TextView = view.findViewById(R.id.listTxtTitle)
        var jobTxtTitle: TextView = view.findViewById(R.id.jobTxtTitle)
        var imagicon: ImageView = view.findViewById(R.id.imagicon)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_staff_list, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = studentList[position]
        holder.listTxtTitle.text = movie.name
        holder.jobTxtTitle.text = movie.role
        if(!movie.staff_photo.equals(""))
        {
            Glide.with(mContext) //1
                .load(movie.staff_photo)
                .placeholder(R.drawable.staff)
                .error(R.drawable.staff)
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                .transform(CircleCrop()) //4
                .into(holder.imagicon)
        }
        else{
            holder.imagicon.setImageResource(R.drawable.staff)
        }
    }
    override fun getItemCount(): Int {
        return studentList.size
    }
}