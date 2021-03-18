package com.mobatia.bisad.activity.home

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.adapter.StudentInfoRecyclerAdapter
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.home.model.StudentListDataCollection
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener

class SecondScreenNew :Fragment() {
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var closeImg: ImageView
    lateinit var studentInfoRecycler: RecyclerView
    lateinit var studentArrayList:ArrayList<StudentListDataCollection>
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var studentInfoRecyclerAdapter: StudentInfoRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_second_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        initializeUI()
    }

    private fun initializeUI()
    {
        studentInfoRecycler=view!!.findViewById(R.id.studentInfoRecycler) as RecyclerView

        linearLayoutManager = LinearLayoutManager(mContext)
        studentInfoRecycler.layoutManager = linearLayoutManager
        studentInfoRecycler.itemAnimator = DefaultItemAnimator()
        closeImg=view!!.findViewById(R.id.closeImg)
        studentArrayList= ArrayList()
        studentArrayList=sharedprefs.getStudentArrayList(mContext)
        if (studentArrayList.size>0)
        {
            studentInfoRecyclerAdapter= StudentInfoRecyclerAdapter(studentArrayList)
            studentInfoRecycler.setAdapter(studentInfoRecyclerAdapter)
        }
        studentInfoRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                val intent = Intent(activity, SecondScreenDataCollection::class.java)
                intent.putExtra("studentId", sharedprefs.getStudentArrayList(mContext).get(position).id)
                intent.putExtra("uniqueID", sharedprefs.getStudentArrayList(mContext).get(position).unique_id)
                intent.putExtra("studentImage", sharedprefs.getStudentArrayList(mContext).get(position).photo)
                intent.putExtra("studentName", sharedprefs.getStudentArrayList(mContext).get(position).name)
                startActivity(intent)
            }
        })

        closeImg.setOnClickListener(View.OnClickListener {
            val dialog = Dialog(mContext)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(false)
            dialog.setContentView(R.layout.alert_dialogue_ok_layout)
            var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
            var alertHead = dialog.findViewById(R.id.alertHead) as TextView
            var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
            var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
            text_dialog.text = "Please update this information next time"
            alertHead.text = "Alert"
            btn_Ok.setText("Ok")
            iconImageView.setImageResource(R.drawable.exclamationicon)
            btn_Ok?.setOnClickListener()
            {
                sharedprefs.setSuspendTrigger(mContext,"1")
                sharedprefs.setDataCollectionShown(mContext,0)
                dialog.dismiss()
                activity?.finish()
            }
            dialog.show()
        })
    }

    override fun onResume() {
        super.onResume()
        studentArrayList= ArrayList()
        studentArrayList=sharedprefs.getStudentArrayList(mContext)
        if (studentArrayList.size>0)
        {
            studentInfoRecyclerAdapter= StudentInfoRecyclerAdapter(studentArrayList)
            studentInfoRecycler.setAdapter(studentInfoRecyclerAdapter)
        }
    }

}




