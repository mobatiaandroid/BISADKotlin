package com.mobatia.bisad.activity.home

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.home.model.datacollection.HealthInsuranceDetailModel
import com.mobatia.bisad.manager.PreferenceData


class HealthScreen(studentID:String,studentImage:String,studentName:String,uniqueID:String) : Fragment(){

    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var MedicalNoteTxt: TextView
    lateinit var studentNameTxt: TextView
    lateinit var imagicon: ImageView
    lateinit var closeImg: ImageView
    lateinit var redirectLink: TextView
    lateinit var mContext: Context
    var foundPosition:Int=-1
    lateinit var healthArrayList: ArrayList<HealthInsuranceDetailModel>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_health_screen, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        healthArrayList=sharedprefs.getHealthDetailArrayList(mContext)!!
        initializeUI()
    }

    private fun initializeUI() {
        MedicalNoteTxt=view!!.findViewById(R.id.MedicalNoteTxt)
        imagicon=view!!.findViewById(R.id.imagicon)
        studentNameTxt=view!!.findViewById(R.id.studentName)
        redirectLink=view!!.findViewById(R.id.redirectLink)
        closeImg=view!!.findViewById(R.id.closeImg)
        var isFound :Boolean=false

        for(i in 0..healthArrayList.size-1)
        {
            if (uniqueID.equals(healthArrayList.get(i).student_unique_id))
            {
                isFound=true
                foundPosition=i

            }
        }

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
            btn_Ok.setText("Continue")
            iconImageView.setImageResource(R.drawable.exclamationicon)
            btn_Ok?.setOnClickListener()
            {
                sharedprefs.setSuspendTrigger(mContext,"1")
                dialog.dismiss()
                activity?.finish()
            }
            dialog.show()
        })

        MedicalNoteTxt.setText(healthArrayList.get(foundPosition).health_detail)
        if (studentImage != "") {
            Glide.with(com.mobatia.bisad.fragment.home.mContext) //1
                .load(studentImage)
                .placeholder(R.drawable.student)
                .error(R.drawable.student)
                .skipMemoryCache(true) //2
                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                .transform(CircleCrop()) //4
                .into(imagicon)
        } else {
            imagicon.setImageResource(R.drawable.boy)
        }
        studentNameTxt.setText(studentName)
        redirectLink.setOnClickListener(View.OnClickListener {
            val viewIntent = Intent(
                "android.intent.action.VIEW",
                Uri.parse(healthArrayList.get(foundPosition).health_form_link)
            )
            startActivity(viewIntent)

        })
    }

}




