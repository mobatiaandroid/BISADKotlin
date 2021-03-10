package com.mobatia.bisad.activity.home.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.fragment.home.model.StudentListDataCollection
import com.mobatia.bisad.fragment.home.model.datacollection.KinDetailApiModel

class StudentInfoRecyclerAdapter (private var familyContactArrayList: ArrayList<StudentListDataCollection>) :
    RecyclerView.Adapter<StudentInfoRecyclerAdapter.MyViewHolder>() {
    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var studentNameTxt: TextView = view.findViewById(R.id.studentNameTxt)
        var classTxt: TextView = view.findViewById(R.id.classTxt)
        var confirmBtn: ImageView = view.findViewById(R.id.confirmBtn)
        var confirm_text: TextView = view.findViewById(R.id.confirm_text)
        var mainRelative: RelativeLayout = view.findViewById(R.id.mainRelative)
    }
    @NonNull
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.adapter_student_info_recycler, parent, false)
        return MyViewHolder(itemView)
    }
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.studentNameTxt.text = familyContactArrayList.get(position).name
        holder.classTxt.text = familyContactArrayList.get(position).studentListClass
        if (familyContactArrayList.get(position).isConfirmed)
        {
            holder.confirmBtn.visibility= View.GONE
            holder.confirm_text.visibility= View.GONE
            holder.mainRelative.setBackgroundResource(R.drawable.rect_background_grey)

        }
        else{
            holder.confirmBtn.visibility= View.VISIBLE
            holder.confirm_text.visibility= View.VISIBLE
            holder.mainRelative.setBackgroundResource(R.drawable.rect_data_collection_red)
        }


    }
    override fun getItemCount(): Int {

        Log.e("ARRAYSIZE",familyContactArrayList.size.toString())
        return familyContactArrayList.size

    }
}