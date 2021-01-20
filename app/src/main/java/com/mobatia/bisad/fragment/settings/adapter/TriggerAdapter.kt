package com.mobatia.bisad.fragment.settings.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.settings.model.TriggerDataModel

class TriggerAdapter  (private var settingsArrayList: ArrayList<TriggerDataModel>) :
    RecyclerView.Adapter<TriggerAdapter.MyViewHolder>() {
     inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var checkBoxImg: ImageView = view.findViewById(R.id.checkBoxImg)
        var categoryTypeTxt: TextView = view.findViewById(R.id.categoryTypeTxt)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_trigger_data_collection, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = settingsArrayList[position]
        holder.categoryTypeTxt.text = movie.categoryName
        if (movie.checkedCategory)
        {
            holder.checkBoxImg.setImageResource(R.drawable.check_box_header_tick)
        }
        else{
            holder.checkBoxImg.setImageResource(R.drawable.check_box_header)
        }

    }
    override fun getItemCount(): Int {

        return settingsArrayList.size

    }
}