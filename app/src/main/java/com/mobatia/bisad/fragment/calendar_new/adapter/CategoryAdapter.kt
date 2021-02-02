package com.mobatia.calendardemopro.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.calendar_new.model.CategoryModel
import com.mobatia.bisad.fragment.home.mContext


class CategoryAdapter (private var mContext:Context,private var settingsArrayList: ArrayList<CategoryModel>) :
    RecyclerView.Adapter<CategoryAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var checkBoxImg: ImageView = view.findViewById(R.id.checkBoxImg)
        var categoryTypeTxt: TextView = view.findViewById(R.id.categoryTypeTxt)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_category, parent, false)
        return MyViewHolder(itemView)
    }
    @SuppressLint("ResourceAsColor")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val movie = settingsArrayList[position]
        holder.categoryTypeTxt.text = movie.categoryName
        if (movie.checkedCategory)
        {
            holder.checkBoxImg.setImageResource(R.drawable.tick)
            holder.checkBoxImg.setColorFilter(mContext.resources.getColor(R.color.white))

        }
        else{
            holder.checkBoxImg.setImageResource(R.drawable.tick)
            holder.checkBoxImg.setColorFilter(mContext.resources.getColor(R.color.rel_eight))
        }

        //    holder.categoryTypeTxt.setTextColor(Color.parseColor(movie.color))
            holder.checkBoxImg.setBackgroundColor(Color.parseColor(movie.color))


    }
    override fun getItemCount(): Int {

        return settingsArrayList.size

    }
}