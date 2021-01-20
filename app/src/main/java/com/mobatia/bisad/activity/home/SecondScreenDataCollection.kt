package com.mobatia.bisad.activity.home

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager.widget.ViewPager
import com.mobatia.bisad.R
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.home.model.StudentListDataCollection
import com.mobatia.bisad.fragment.home.model.datacollection.PassportApiModel
import com.mobatia.bisad.fragment.home.sharedprefs
import com.mobatia.bisad.fragment.student_information.model.StudentInfoApiModel
import com.mobatia.bisad.manager.PreferenceData
import org.json.JSONArray
import org.json.JSONObject


lateinit var studentId:String
lateinit var uniqueID:String
lateinit var studentImage:String
lateinit var studentName:String
class SecondScreenDataCollection : FragmentActivity(), ViewPager.OnPageChangeListener,
    RadioGroup.OnCheckedChangeListener {

    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    lateinit var context: Context
    lateinit var radioGroup: RadioGroup
    lateinit var radioButton1: RadioButton
    lateinit var radioButton2: RadioButton
    lateinit var radioButton3: RadioButton
    lateinit var pager: ViewPager
    lateinit var backBtn: ImageView
    lateinit var nextBtn: ImageView
    lateinit var submitBtn: TextView
    lateinit var bottomLinear: LinearLayout
    var own_details: JSONArray? =null
    var kin_details: JSONArray? =null
    var JSONSTRING: String =""
    var  previousPage:Int=0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_data_collection_home)
        sharedprefs = PreferenceData()
        studentId=intent.extras!!.getString("studentId").toString()
        uniqueID=intent.extras!!.getString("uniqueID").toString()
        studentImage=intent.extras!!.getString("studentImage").toString()
        studentName=intent.extras!!.getString("studentName").toString()
        initializeUI()
    }


    private fun initializeUI() {
        jsonConstans = JsonConstants()
        context = this
        radioGroup=findViewById(R.id.radiogroup)
        pager=findViewById(R.id.viewPager)
        backBtn=findViewById(R.id.backImg)
        nextBtn=findViewById(R.id.nextImg)
        submitBtn=findViewById(R.id.submit)
        bottomLinear=findViewById(R.id.bottomLinear)
        radioButton1=findViewById(R.id.radioButton1)
        radioButton2=findViewById(R.id.radioButton2)
        radioButton3=findViewById(R.id.radioButton3)
        radioGroup.setOnCheckedChangeListener(this)
        pager.adapter=MyPagerAdapter(getSupportFragmentManager())
        pager.addOnPageChangeListener(this)
        pager.offscreenPageLimit=2
        if (sharedprefs.getTriggerType(mContext)==1)
        {
            bottomLinear.visibility= View.VISIBLE
            radioButton1.visibility= View.VISIBLE
            radioButton2.visibility= View.VISIBLE
            radioButton3.visibility= View.GONE
            submitBtn.visibility= View.INVISIBLE
        }
        else if (sharedprefs.getTriggerType(mContext)==5|| sharedprefs.getTriggerType(mContext)==7)
        {
            bottomLinear.visibility= View.VISIBLE
            radioButton1.visibility= View.VISIBLE
            radioButton2.visibility= View.VISIBLE
            radioButton3.visibility= View.GONE
            submitBtn.visibility= View.INVISIBLE
        }
        else if (sharedprefs.getTriggerType(mContext)==3|| sharedprefs.getTriggerType(mContext)==6)
        {
            bottomLinear.visibility= View.VISIBLE
            radioButton1.visibility= View.VISIBLE
            radioButton2.visibility= View.VISIBLE
            radioButton3.visibility= View.GONE
            submitBtn.visibility= View.INVISIBLE
        }
        else{
            bottomLinear.visibility= View.VISIBLE
            radioButton1.visibility= View.VISIBLE
            radioButton2.visibility= View.VISIBLE
            radioButton3.visibility= View.GONE
            submitBtn.visibility= View.INVISIBLE
        }
        nextBtn.setOnClickListener(View.OnClickListener {
            pager.setCurrentItem(pager.currentItem+1)
        })
        backBtn.setOnClickListener(View.OnClickListener {
            pager.setCurrentItem(pager.currentItem-1)
        })

        previousPage = 1

        val prefs = getSharedPreferences("BSKL", Context.MODE_PRIVATE)
        val data = prefs.getString("DATA_COLLECTION", null)
        try {
            val respObj = JSONObject(data)
            own_details = respObj.getJSONArray("own_details")
            kin_details = respObj.getJSONArray("kin_details")
        }
        catch (e: Exception) {
        }

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageSelected(position: Int) {

            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageScrollStateChanged(state: Int) {
                //       Log.e("onPageState: ", String.valueOf(state));
            }
        })
        submitBtn.setOnClickListener(View.OnClickListener {

            var passportArray=ArrayList<PassportApiModel>()
            passportArray=sharedprefs.getPassportDetailArrayList(mContext)!!
            var pos:Int=-1
            for (i in 0..passportArray.size-1)
            {
                if (uniqueID.equals(passportArray.get(i).student_unique_id))
                {
                    pos=i
                }
            }
            if (pos!=-1)
            {
                if(passportArray.get(pos).nationality.equals(""))
                {
                    showSuccessAlert(context,"Please Select your nationality","Alert")

                }
                else{
                    if(passportArray.get(pos).passport_number.equals(""))
                    {
                        showSuccessAlert(context,"Please enter your passport number","Alert")
                    }
                    else{
                        if(passportArray.get(pos).expiry_date.equals(""))
                        {
                            showSuccessAlert(context,"Please select your passport expiry date","Alert")
                        }
                        else{
                            if(passportArray.get(pos).is_date_changed.equals("1"))
                            {
                                if(!passportArray.get(pos).is_date_changed)
                                {
                                    showSuccessAlert(context,"Please select your passport expiry date since your passport expiry date is expired","Alert")
                                }
                                else{
                                    if(passportArray.get(pos).passport_image.equals(""))
                                    {
                                        showSuccessAlert(context,"Please upload you passport image","Alert")

                                    }
                                    else{
                                        if(passportArray.get(pos).emirates_id_no.equals(""))
                                        {
                                            showSuccessAlert(context,"Please enter your emirated id number","Alert")

                                        }
                                        else{
                                            if(passportArray.get(pos).emirates_id_image.equals(""))
                                            {
                                                showSuccessAlert(context,"Please upload you emirates ID image","Alert")
                                            }
                                            else
                                            {
                                                var model=PassportApiModel()
                                                model.id=passportArray.get(pos).id
                                                model.student_unique_id=passportArray.get(pos).student_unique_id
                                                model.student_id=passportArray.get(pos).student_id
                                                model.student_name=passportArray.get(pos).student_name
                                                model.passport_number=passportArray.get(pos).passport_number
                                                model.nationality=passportArray.get(pos).nationality
                                                model.passport_image=passportArray.get(pos).passport_image
                                                model.passport_image_name=passportArray.get(pos).passport_image_name
                                                model.passport_image_path=passportArray.get(pos).passport_image_path
                                                model.date_of_issue=passportArray.get(pos).date_of_issue
                                                model.expiry_date=passportArray.get(pos).expiry_date
                                                model.passport_expired=passportArray.get(pos).passport_expired
                                                model.emirates_id_no=passportArray.get(pos).emirates_id_no
                                                model.emirates_id_image=passportArray.get(pos).emirates_id_image
                                                model.emirates_id_image_name=passportArray.get(pos).emirates_id_image_name
                                                model.emirates_id_image_path=passportArray.get(pos).emirates_id_image_path
                                                model.created_at=passportArray.get(pos).created_at
                                                model.updated_at=passportArray.get(pos).updated_at
                                                passportArray.removeAt(pos)
                                                passportArray.add(pos,model)
                                                sharedprefs.getPassportDetailArrayList(mContext)!!.clear()
                                                sharedprefs.setPassportDetailArrayList(mContext,passportArray)
                                            }
                                        }
                                    }
                                }
                            }
                            else{
                                if(passportArray.get(pos).passport_image.equals(""))
                                {
                                    showSuccessAlert(context,"Please upload you passport image","Alert")
                                }
                                else{
                                    if(passportArray.get(pos).emirates_id_no.equals(""))
                                    {
                                        showSuccessAlert(context,"Please enter your emirates id number","Alert")
                                    }
                                    else{
                                        if(passportArray.get(pos).emirates_id_image.equals(""))
                                        {
                                            showSuccessAlert(context,"Please upload you emirates image","Alert")
                                        }
                                        else
                                        {
                                            var unique=passportArray.get(pos).student_unique_id
                                            var model=PassportApiModel()
                                            model.id=passportArray.get(pos).id
                                            model.student_unique_id=passportArray.get(pos).student_unique_id
                                            model.student_id=passportArray.get(pos).student_id
                                            model.student_name=passportArray.get(pos).student_name
                                            model.passport_number=passportArray.get(pos).passport_number
                                            model.nationality=passportArray.get(pos).nationality
                                            model.passport_image=passportArray.get(pos).passport_image
                                            model.passport_image_name=passportArray.get(pos).passport_image_name
                                            model.passport_image_path=passportArray.get(pos).passport_image_path
                                            model.date_of_issue=passportArray.get(pos).date_of_issue
                                            model.expiry_date=passportArray.get(pos).expiry_date
                                            model.passport_expired=passportArray.get(pos).passport_expired
                                            model.emirates_id_no=passportArray.get(pos).emirates_id_no
                                            model.emirates_id_image=passportArray.get(pos).emirates_id_image
                                            model.emirates_id_image_name=passportArray.get(pos).emirates_id_image_name
                                            model.emirates_id_image_path=passportArray.get(pos).emirates_id_image_path
                                            model.created_at=passportArray.get(pos).created_at
                                            model.updated_at=passportArray.get(pos).updated_at
                                            var isFound:Boolean=false
                                            var pos:Int=-1
                                            for(i in 0..sharedprefs.getStudentArrayList(mContext).size-1)
                                            {
                                                if (unique.equals(sharedprefs.getStudentArrayList(mContext).get(i).unique_id))
                                                {
                                                    isFound=true
                                                    pos=i
                                                }
                                            }
                                            if (pos!=-1)
                                            {
                                                var studentList=ArrayList<StudentListDataCollection>()
                                                studentList=sharedprefs.getStudentArrayList(context)
                                                var model= StudentListDataCollection()
                                                model.id=studentList.get(pos).id
                                                model.name=studentList.get(pos).name
                                                model.unique_id=studentList.get(pos).unique_id
                                                model.isConfirmed=true
                                                model.studentListClass=studentList.get(pos).studentListClass
                                                model.section=studentList.get(pos).section
                                                model.photo=studentList.get(pos).photo
                                                model.house=studentList.get(pos).house

                                                studentList.removeAt(pos)
                                                studentList.add(pos,model)
                                                sharedprefs.getStudentArrayList(context).clear()
                                                sharedprefs.setStudentArrayList(context,studentList)

                                            }

                                            passportArray.removeAt(pos)
                                            passportArray.add(pos,model)
                                            sharedprefs.getPassportDetailArrayList(mContext)!!.clear()
                                            sharedprefs.setPassportDetailArrayList(mContext,passportArray)
                                            finish()

                                        }
                                    }
                                }
                            }

                        }
                    }
                }
            }

        })
    }


    class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm)
    {

        override fun getItem(position: Int): Fragment
        {
            return if (sharedprefs.getTriggerType(mContext)==1) {
                when (position) {
                    0 -> HealthScreen(studentId, studentImage, studentName, uniqueID)
                    1 -> PassportEmiratesScreen(studentId, studentImage, studentName, uniqueID)
                    else -> HealthScreen(studentId, studentImage, studentName, uniqueID)
                }
            } else if (sharedprefs.getTriggerType(mContext)==2)
            {
                when (position) {
                    0 -> HealthScreen(studentId, studentImage, studentName, uniqueID)
                    1 -> PassportEmiratesScreen(studentId, studentImage, studentName, uniqueID)
                    else -> HealthScreen(studentId, studentImage, studentName, uniqueID)
                }
            } else if (sharedprefs.getTriggerType(mContext)==3) {
                when (position) {
                    0 -> HealthScreen(studentId, studentImage, studentName, uniqueID)
                    1 -> PassportEmiratesScreen(studentId, studentImage, studentName, uniqueID)
                    else -> HealthScreen(studentId, studentImage, studentName, uniqueID)
                }
            } else {
                when (position) {
                    0 -> HealthScreen(studentId, studentImage, studentName, uniqueID)
                    1 -> PassportEmiratesScreen(studentId, studentImage, studentName, uniqueID)
                    else -> HealthScreen(studentId, studentImage, studentName, uniqueID)
                }
            }
        }

        override fun getCount(): Int {
            return if (sharedprefs.getTriggerType(mContext)==1)
            {
                return 2
            }
            else if (sharedprefs.getTriggerType(mContext)==5 ||sharedprefs.getTriggerType(mContext)==7)
            {
                return 2
            }
            else
            {
                return 2
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {
        if (sharedprefs.getTriggerType(mContext)==1)
        {
        }
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        if (sharedprefs.getTriggerType(mContext)==1)
        {

        }
    }


    /**
     * When a new page becomes selected
     *
     * @param position
     */
    override fun onPageSelected(position: Int) {
        if (sharedprefs.getTriggerType(mContext)==1) {
            bottomLinear.visibility = View.VISIBLE
            radioButton3.visibility = View.VISIBLE
            when (position) {
                0 -> {
                    radioButton1.visibility = View.VISIBLE
                    radioButton2.visibility = View.VISIBLE
                    radioButton3.visibility = View.GONE
                    radioGroup.check(R.id.radioButton1)
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.INVISIBLE
                    submitBtn.visibility = View.INVISIBLE
                }
                1 -> {
                    radioButton1.visibility = View.VISIBLE
                    radioButton2.visibility = View.VISIBLE
                    radioButton3.visibility = View.GONE
                    radioGroup.check(R.id.radioButton2)
                    nextBtn.visibility = View.INVISIBLE
                    backBtn.visibility = View.VISIBLE
                    submitBtn.visibility = View.VISIBLE
                }
                else -> {
                    radioButton1.visibility = View.VISIBLE
                    radioButton2.visibility = View.VISIBLE
                    radioButton3.visibility = View.GONE
                    radioGroup.check(R.id.radioButton1)
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.INVISIBLE
                    submitBtn.visibility = View.INVISIBLE
                }
            }
        } else if (sharedprefs.getTriggerType(mContext)==2)
        {
           // bottomLinear.visibility = View.GONE
            bottomLinear.visibility = View.VISIBLE
            radioButton3.visibility = View.VISIBLE
            when (position) {
                0 -> {
                    radioButton1.visibility = View.VISIBLE
                    radioButton2.visibility = View.VISIBLE
                    radioButton3.visibility = View.GONE
                    radioGroup.check(R.id.radioButton1)
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.INVISIBLE
                    submitBtn.visibility = View.INVISIBLE
                }
                1 -> {
                    radioButton1.visibility = View.VISIBLE
                    radioButton2.visibility = View.VISIBLE
                    radioButton3.visibility = View.GONE
                    radioGroup.check(R.id.radioButton2)
                    nextBtn.visibility = View.INVISIBLE
                    backBtn.visibility = View.VISIBLE
                    submitBtn.visibility = View.VISIBLE
                }
                else -> {
                    radioButton1.visibility = View.VISIBLE
                    radioButton2.visibility = View.VISIBLE
                    radioButton3.visibility = View.GONE
                    radioGroup.check(R.id.radioButton1)
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.INVISIBLE
                    submitBtn.visibility = View.INVISIBLE
                }
            }
        }
        else if (sharedprefs.getTriggerType(mContext)==3)
        {
         //   bottomLinear.visibility = View.GONE
            bottomLinear.visibility = View.VISIBLE
            radioButton3.visibility = View.VISIBLE
            when (position) {
                0 -> {
                    radioButton1.visibility = View.VISIBLE
                    radioButton2.visibility = View.VISIBLE
                    radioButton3.visibility = View.GONE
                    radioGroup.check(R.id.radioButton1)
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.INVISIBLE
                    submitBtn.visibility = View.INVISIBLE
                }
                1 -> {
                    radioButton1.visibility = View.VISIBLE
                    radioButton2.visibility = View.VISIBLE
                    radioButton3.visibility = View.GONE
                    radioGroup.check(R.id.radioButton2)
                    nextBtn.visibility = View.INVISIBLE
                    backBtn.visibility = View.VISIBLE
                    submitBtn.visibility = View.VISIBLE
                }
                else -> {
                    radioButton1.visibility = View.VISIBLE
                    radioButton2.visibility = View.VISIBLE
                    radioButton3.visibility = View.GONE
                    radioGroup.check(R.id.radioButton1)
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.INVISIBLE
                    submitBtn.visibility = View.INVISIBLE
                }
            }
        }
        else if (sharedprefs.getTriggerType(mContext)==4)
        {
            //bottomLinear.visibility = View.GONE
            bottomLinear.visibility = View.VISIBLE
            radioButton3.visibility = View.VISIBLE
            when (position) {
                0 -> {
                    radioButton1.visibility = View.VISIBLE
                    radioButton2.visibility = View.VISIBLE
                    radioButton3.visibility = View.GONE
                    radioGroup.check(R.id.radioButton1)
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.INVISIBLE
                    submitBtn.visibility = View.INVISIBLE
                }
                1 -> {
                    radioButton1.visibility = View.VISIBLE
                    radioButton2.visibility = View.VISIBLE
                    radioButton3.visibility = View.GONE
                    radioGroup.check(R.id.radioButton2)
                    nextBtn.visibility = View.INVISIBLE
                    backBtn.visibility = View.VISIBLE
                    submitBtn.visibility = View.VISIBLE
                }
                else -> {
                    radioButton1.visibility = View.VISIBLE
                    radioButton2.visibility = View.VISIBLE
                    radioButton3.visibility = View.GONE
                    radioGroup.check(R.id.radioButton1)
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.INVISIBLE
                    submitBtn.visibility = View.INVISIBLE
                }
            }
        }
        else if (sharedprefs.getTriggerType(mContext)==5) {
            bottomLinear.visibility = View.VISIBLE
            radioButton3.visibility = View.GONE
            when (position) {
                0 -> {
                    radioGroup.check(R.id.radioButton1)
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.INVISIBLE
                    submitBtn.visibility = View.VISIBLE
                }
                1 -> {
                    radioGroup.check(R.id.radioButton2)
                    nextBtn.visibility = View.INVISIBLE
                    backBtn.visibility = View.VISIBLE
                    submitBtn.visibility = View.VISIBLE
                }
                else -> {
                    radioGroup.check(R.id.radioButton1)
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.INVISIBLE
                    submitBtn.visibility = View.VISIBLE
                }
            }
        } else if (sharedprefs.getTriggerType(mContext)==6) {
            bottomLinear.visibility = View.VISIBLE
            radioButton3.visibility = View.GONE
            when (position) {
                0 -> {
                    radioGroup.check(R.id.radioButton1)
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.INVISIBLE
                    submitBtn.visibility = View.VISIBLE
                }
                1 -> {
                    radioGroup.check(R.id.radioButton2)
                    nextBtn.visibility = View.INVISIBLE
                    backBtn.visibility = View.VISIBLE
                    submitBtn.visibility = View.VISIBLE
                }
                else -> {
                    radioGroup.check(R.id.radioButton1)
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.INVISIBLE
                    submitBtn.visibility = View.VISIBLE
                }
            }
        } else {
            bottomLinear.visibility = View.VISIBLE
            radioButton3.visibility = View.GONE
            when (position) {
                0 -> {
                    radioGroup.check(R.id.radioButton1)
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.INVISIBLE
                    submitBtn.visibility = View.VISIBLE
                }
                1 -> {
                    radioGroup.check(R.id.radioButton2)
                    nextBtn.visibility = View.INVISIBLE
                    backBtn.visibility = View.VISIBLE
                    submitBtn.visibility = View.VISIBLE
                }
                else -> {
                    radioGroup.check(R.id.radioButton1)
                    nextBtn.visibility = View.VISIBLE
                    backBtn.visibility = View.INVISIBLE
                    submitBtn.visibility = View.VISIBLE
                }
            }
        }


    }

    override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
        when (checkedId) {
            R.id.radioButton1 -> pager.currentItem = 0
            R.id.radioButton2 -> pager.currentItem = 1
        }


    }
    fun showSuccessAlert(context: Context,message : String,msgHead : String)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_ok_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        text_dialog.text = message
        alertHead.text = msgHead
        iconImageView.setImageResource(R.drawable.exclamationicon)
        btn_Ok?.setOnClickListener()
        {
            dialog.dismiss()

        }
        dialog.show()
    }
}
