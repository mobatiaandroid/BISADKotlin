package com.mobatia.bisad.fragment.termdates.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.messages.adapter.MessageListRecyclerAdapter
import com.mobatia.bisad.fragment.messages.model.MessageListDetailModel
import com.mobatia.bisad.fragment.termdates.model.TermDatesListDetailModel

internal class TermDatesRecyclerAdapter (private var termDatesArrayList: List<TermDatesListDetailModel>) :
    RecyclerView.Adapter<TermDatesRecyclerAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView = view.findViewById(R.id.title)
        var Img: ImageView = view.findViewById(R.id.Img)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_term_dates, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = termDatesArrayList[position]
        holder.title.text = movie.title
    }
    override fun getItemCount(): Int {

        return termDatesArrayList.size

    }
}