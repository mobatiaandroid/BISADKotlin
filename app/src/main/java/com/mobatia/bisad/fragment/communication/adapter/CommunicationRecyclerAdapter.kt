package com.mobatia.bisad.fragment.communication.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.settings.adapter.SettingsRecyclerAdapter

internal class CommunicationRecyclerAdapter (private var settingsArrayList: List<String>) :
    RecyclerView.Adapter<CommunicationRecyclerAdapter.MyViewHolder>() {
    internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var itemName: TextView = view.findViewById(R.id.itemName)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_communication_recycler, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = settingsArrayList[position]
        holder.itemName.text = movie.toString()


    }
    override fun getItemCount(): Int {

        return settingsArrayList.size

    }
}