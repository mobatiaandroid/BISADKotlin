package com.mobatia.bisad.fragment.messages.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.messages.model.MessageListDetailModel
import com.mobatia.bisad.fragment.student_information.adapter.StudentInfoAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentInfoDetail

internal class MessageListRecyclerAdapter (private var messageArrayList: List<MessageListDetailModel>) :
    RecyclerView.Adapter<MessageListRecyclerAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var Img: ImageView = view.findViewById(R.id.Img)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_message_list, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = messageArrayList[position]
        holder.title.text = movie.title
        if (movie.alert_type.equals("Video"))
        {
            holder.Img.setImageResource(R.drawable.alerticon_video)
        }
        else if (movie.alert_type.equals("Text"))
        {
            holder.Img.setImageResource(R.drawable.alerticon_text)
        }
        else if (movie.alert_type.equals("Image"))
        {
            holder.Img.setImageResource(R.drawable.alerticon_image)
        }
        else if (movie.alert_type.equals("Voice"))
        {
            holder.Img.setImageResource(R.drawable.alerticon_audio)
        }
    }
    override fun getItemCount(): Int {

        return messageArrayList.size

    }
}