package com.mobatia.bisad.activity.absence

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.ListView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.manager.PreferenceData
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class AbsenceDetailActivity : AppCompatActivity(){
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    lateinit var stnameValue: TextView
    lateinit var studClassValue: TextView
    lateinit var leaveDateFromValue: TextView
    lateinit var leaveDateToValue: TextView
    lateinit var reasonValue: TextView
    lateinit var extras:Bundle
     var reason:String?=""
     var studentName:String?=""
     var studentClass:String?=""
     var fromDate:String?=""
     var toDate:String?=""
    private lateinit var relativeHeader: RelativeLayout
    private lateinit var backRelative: RelativeLayout
    private lateinit var logoClickImgView: ImageView
    private lateinit var btn_left: ImageView
    private lateinit var heading: TextView
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_absence_detail)
        mContext=this
         reason=intent.getStringExtra("reason")
        studentName=intent.getStringExtra("studentName")
        studentClass=intent.getStringExtra("studentClass")
        fromDate=intent.getStringExtra("fromDate")
        relativeHeader = findViewById(R.id.relativeHeader)
        toDate=intent.getStringExtra("toDate")
        Log.e("Values get",reason +" "+fromDate+" "+toDate)
        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()
        initUI()

    }
    fun initUI() {
        stnameValue = findViewById<TextView>(R.id.stnameValue)
        studClassValue = findViewById<TextView>(R.id.studClassValue)
        leaveDateFromValue = findViewById<TextView>(R.id.leaveDateFromValue)
        leaveDateToValue = findViewById<TextView>(R.id.leaveDateToValue)
        reasonValue = findViewById<TextView>(R.id.reasonValue)
        relativeHeader = findViewById(R.id.relativeHeader)
        backRelative = findViewById(R.id.backRelative)
        heading = findViewById(R.id.heading)
        btn_left = findViewById(R.id.btn_left)
        logoClickImgView = findViewById(R.id.logoClickImgView)
        heading.text = "Absence"
        btn_left.setOnClickListener(View.OnClickListener {
            finish()
        })
        backRelative.setOnClickListener(View.OnClickListener {
            finish()
        })
        logoClickImgView.setOnClickListener(View.OnClickListener {
            val intent = Intent(mContext, HomeActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        })

        stnameValue.text = studentName
        studClassValue.text = studentClass
        val fromdate=fromDate
        val todate=toDate
        val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
        val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
        val inputDateStr = fromdate
        val date: Date = inputFormat.parse(inputDateStr)
        val outputDateStr: String = outputFormat.format(date)




        if (todate!=""){
            val inputFormat1: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat1: DateFormat = SimpleDateFormat("dd MMMM yyyy")
            val inputDateStr1 = todate
            val date1: Date = inputFormat1.parse(inputDateStr1)
            val outputDateStr1: String = outputFormat1.format(date1)
            leaveDateToValue.text = outputDateStr1
            leaveDateFromValue.text = outputDateStr
            reasonValue.text = reason

        }else{
            leaveDateFromValue.text = outputDateStr
            reasonValue.text = reason
            leaveDateToValue.text = "-"

        }







    }

}