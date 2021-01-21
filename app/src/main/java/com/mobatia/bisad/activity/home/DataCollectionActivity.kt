package com.mobatia.bisad.activity.home

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.gson.Gson
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.absence.model.RequestAbsenceApiModel
import com.mobatia.bisad.activity.home.model.DataCollectionSubmissionModel
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.home.model.StudentListDataCollection
import com.mobatia.bisad.fragment.home.model.datacollection.HealthInsuranceDetailModel
import com.mobatia.bisad.fragment.home.model.datacollection.KinDetailApiModel
import com.mobatia.bisad.fragment.home.model.datacollection.OwnContactModel
import com.mobatia.bisad.fragment.home.model.datacollection.PassportApiModel
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

lateinit var context: Context
lateinit var sharedprefs: PreferenceData
lateinit var jsonConstans: JsonConstants

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
class DataCollectionActivity : FragmentActivity(), OnPageChangeListener,
    RadioGroup.OnCheckedChangeListener {
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_data_collection)
        sharedprefs = PreferenceData()
        context = this
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
        if (sharedprefs.getTriggerType(context)==1)
        {
          bottomLinear.visibility= View.VISIBLE
          radioButton1.visibility= View.VISIBLE
          radioButton2.visibility= View.VISIBLE
          radioButton3.visibility= View.GONE
          submitBtn.visibility= View.VISIBLE
        }
        else if (sharedprefs.getTriggerType(context)==5|| sharedprefs.getTriggerType(context)==7)
        {
            bottomLinear.visibility= View.VISIBLE
            radioButton1.visibility= View.VISIBLE
            radioButton2.visibility= View.VISIBLE
            radioButton3.visibility= View.GONE
            submitBtn.visibility= View.VISIBLE
        }
        else if (sharedprefs.getTriggerType(context)==3|| sharedprefs.getTriggerType(context)==6)
        {
            bottomLinear.visibility= View.VISIBLE
            radioButton1.visibility= View.INVISIBLE
            radioButton2.visibility= View.INVISIBLE
            radioButton3.visibility= View.INVISIBLE
            submitBtn.visibility= View.VISIBLE
            nextBtn.visibility= View.INVISIBLE
            backBtn.visibility= View.INVISIBLE
        }
        else{
            bottomLinear.visibility= View.VISIBLE
            radioButton1.visibility= View.GONE
            radioButton2.visibility= View.GONE
            radioButton3.visibility= View.GONE
            submitBtn.visibility= View.VISIBLE
            nextBtn.visibility= View.INVISIBLE
            backBtn.visibility= View.INVISIBLE
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

        pager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) {

            }
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                if (sharedprefs.getTriggerType(context)==1)
                {

                    Log.e("CHECKING","1")
                    if (position==1)
                    {
                        Log.e("CHECKING","2")
                        if (!sharedprefs.getOwnContactDetailArrayList(context)!!.get(0).isConfirmed)
                        {
                            Log.e("CHECKING","3")
                            showSuccessAlert(context,"Please confirm all mandator fields in Own Details","Alert")
                        }
                        else
                        {
                            Log.e("CHECKING","4")
                          if (sharedprefs.getKinDetailArrayList(context)!!.size>0)
                          {
                              Log.e("CHECKING","5")
                              var isFound:Boolean=false
                              for (i in 0..sharedprefs.getKinDetailArrayList(context)!!.size-1)
                              {
                                  if (!sharedprefs.getKinDetailArrayList(context)!!.get(i).isConfirmed)
                                  {
                                      isFound=true
                                  }
                              }
                              if (isFound)
                              {
                                  showSuccessAlert(context,"Please confirm all mandator fields in Family Contacts","Alert")
                              }
                          }
                            else
                          {
                              showSuccessAlert(context,"Please Add atleast one Family Contacts","Alert")
                          }
                        }
                    }

                }
            }

            override fun onPageScrollStateChanged(state: Int) {
                //       Log.e("onPageState: ", String.valueOf(state));
            }
        })
        submitBtn.setOnClickListener(View.OnClickListener {

            Log.e("CHECKING","42")
            if (sharedprefs.getTriggerType(context)==1)
            {
                Log.e("CHECKING","4w")
               var currrentPage=pager.currentItem

                if (currrentPage==0)
                {
                    Log.e("CHECKING","4e")
                    if (!sharedprefs.getOwnContactDetailArrayList(context)!!.get(0).isConfirmed)
                    {
                       //Alert
                        Log.e("CHECKING","4d")
                        showSuccessAlert(context,"Please confirm all mandator fields in Own Details","Alert")
                    }
                    else
                    {
                        Log.e("CHECKING","4")
                        if (sharedprefs.getKinDetailArrayList(context)!!.size>0)
                        {
                            Log.e("CHECKING","5")
                            var isFound:Boolean=false
                            for (i in 0..sharedprefs.getKinDetailArrayList(context)!!.size-1)
                            {
                                if (!sharedprefs.getKinDetailArrayList(context)!!.get(i).isConfirmed)
                                {
                                    isFound=true
                                }
                            }
                            if (isFound)
                            {
                                showSuccessAlert(context,"Please confirm all mandator fields in Family Contacts","Alert")
                            }
                            else{

                                var triggerType = 3
                                var overallStatus =1
                                callSubmitAPI(triggerType, overallStatus)

                            }
                        }
                        else
                        {
                            showSuccessAlert(context,"Please Add atleast one Family Contacts","Alert")
                        }
                    }
                }
                else
                {
                    var isFound :Boolean=false
                    for (i in 0..sharedprefs.getStudentArrayList(context).size-1)
                    {
                        if(!sharedprefs.getStudentArrayList(context).get(i).isConfirmed)
                        {
                            isFound=true
                        }
                    }
                    if (isFound)
                    {
                        showSuccessAlert(context,"Please Confirm your student Passport emrirates details","Alert")
                    }
                    else
                    {
                        var triggerType = 1
                        var overallStatus =2
                        callSubmitAPI(triggerType, overallStatus)
                    }

                }
            }
            else{

                if (sharedprefs.getTriggerType(context)==2)
                {
                    if (!sharedprefs.getOwnContactDetailArrayList(context)!!.get(0).isConfirmed)
                    {
                        //Alert
                        Log.e("CHECKING","4d")
                        showSuccessAlert(context,"Please confirm all mandator fields in Own Details","Alert")
                    }
                    else
                    {
                        Log.e("CHECKING","4")
                        if (sharedprefs.getKinDetailArrayList(context)!!.size>0)
                        {
                            Log.e("CHECKING","5")
                            var isFound:Boolean=false
                            for (i in 0..sharedprefs.getKinDetailArrayList(context)!!.size-1)
                            {
                                if (!sharedprefs.getKinDetailArrayList(context)!!.get(i).isConfirmed)
                                {
                                    isFound=true
                                }
                            }
                            if (isFound)
                            {
                                showSuccessAlert(context,"Please confirm all mandator fields in Family Contacts","Alert")
                            }
                            else{

                                var triggerType = 1
                                var overallStatus =2
                                callSubmitAPI(triggerType, overallStatus)

                            }
                        }
                        else
                        {
                            showSuccessAlert(context,"Please Add atleast one Family Contacts","Alert")
                        }
                    }
                }
                else{
                    var isFound :Boolean=false
                    for (i in 0..sharedprefs.getStudentArrayList(context).size-1)
                    {
                        if(!sharedprefs.getStudentArrayList(context).get(i).isConfirmed)
                        {
                            isFound=true
                        }
                    }
                    if (isFound)
                    {
                        showSuccessAlert(context,"Please Confirm your student Passport emrirates details","Alert")
                    }
                    else
                    {
                        var triggerType = 1
                        var overallStatus =2
                        callSubmitAPI(triggerType, overallStatus)
                    }
                }

            }

        })
    }


    class MyPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm)
    {

        override fun getItem(position: Int): Fragment
        {
            return if (sharedprefs.getTriggerType(context)==1) {
                when (position) {
                    0 -> FirstScreenNewData()
                    1 -> SecondScreenNew()
                    else -> FirstScreenNewData()
                }
            } else if (sharedprefs.getTriggerType(context)==2)
            {
                when (position) {
                    0 -> FirstScreenNewData()
                    else -> FirstScreenNewData()
                }
            } else if (sharedprefs.getTriggerType(context)==3) {
                when (position) {
                    0 -> SecondScreenNew()
                    else -> SecondScreenNew()
                }
            } else if (sharedprefs.getTriggerType(context)==4) {
                when (position) {
                    0 -> SecondScreenNew()
                    else -> SecondScreenNew()
                }
            } else if (sharedprefs.getTriggerType(context)==5
            ) {
                when (position) {
                    0 -> FirstScreenNewData()
                    1 -> SecondScreenNew()
                    else -> FirstScreenNewData()
                }
            } else if (sharedprefs.getTriggerType(context)==6
            ) {
                when (position) {
                    0 -> SecondScreenNew()
                    else -> SecondScreenNew()
                }
            } else {
                when (position) {
                    0 -> FirstScreenNewData()
                    1 -> SecondScreenNew()
                    else -> SecondScreenNew()
                }
            }
        }

        override fun getCount(): Int {
            return if (sharedprefs.getTriggerType(context)==1)
            {
               return 2
            }
            else if (sharedprefs.getTriggerType(context)==5 ||sharedprefs.getTriggerType(context)==7)
            {
               return 2
            }
            else
            {
               return 1
            }
        }
    }

    override fun onPageScrollStateChanged(state: Int) {

    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

    }


    /**
     * When a new page becomes selected
     *
     * @param position
     */
    override fun onPageSelected(position: Int) {
        if (sharedprefs.getTriggerType(context)==1) {
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
                    submitBtn.visibility = View.VISIBLE
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
                    submitBtn.visibility = View.VISIBLE
                }
            }
        } else if (sharedprefs.getTriggerType(context)==2)
        {
            bottomLinear.visibility = View.GONE
        }
        else if (sharedprefs.getTriggerType(context)==3)
        {
            bottomLinear.visibility = View.GONE
        }
        else if (sharedprefs.getTriggerType(context)==4)
        {
            bottomLinear.visibility = View.GONE
        }
        else if (sharedprefs.getTriggerType(context)==5) {
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
        } else if (sharedprefs.getTriggerType(context)==6) {
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
            pager.setCurrentItem(0, true)
        }
        dialog.show()
    }
    fun showSuccessDataAlert(context: Context,message : String,msgHead : String,triggertype: Int,overallStatus: Int)
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
            if (triggertype==1 && overallStatus==2)
            {

                sharedprefs.getOwnContactDetailArrayList(context)!!.clear()
                var dummyOwn=ArrayList<OwnContactModel>()
                sharedprefs.setOwnContactDetailArrayList(context,dummyOwn)
                sharedprefs.getKinDetailPassArrayList(context)!!.clear()
                var dummyKinPass=ArrayList<KinDetailApiModel>()
                sharedprefs.setKinDetailPassArrayList(context,dummyKinPass)
                sharedprefs.getKinDetailArrayList(context)!!.clear()
                var dummyKinShow=ArrayList<KinDetailApiModel>()
                sharedprefs.setKinDetailArrayList(context,dummyKinShow)
                sharedprefs.getHealthDetailArrayList(context)!!.clear()
                var dummyHealth=ArrayList<HealthInsuranceDetailModel>()
                sharedprefs.setHealthDetailArrayList(context,dummyHealth)
                sharedprefs.getPassportDetailArrayList(context)!!.clear()
                var dummyPassport=ArrayList<PassportApiModel>()
                sharedprefs.setPassportDetailArrayList(context,dummyPassport)
                sharedprefs.getStudentArrayList(context)!!.clear()
                var dummyStudent=ArrayList<StudentListDataCollection>()
                sharedprefs.setStudentArrayList(context,dummyStudent)

            }
            else if(triggertype==3 && overallStatus==1)
            {
                sharedprefs.setDataCollection(context,1)
                sharedprefs.setTriggerType(context,3)
                finish()
                val intent = Intent(context, DataCollectionActivity::class.java)
                context.startActivity(intent)

            }

        }
        dialog.show()
    }


   fun callSubmitAPI(triggertype:Int,overallStatus:Int)
    {
       var OWNDATA:String=""
        var ownArray=ArrayList<OwnContactModel>()
        var kinArray=ArrayList<KinDetailApiModel>()
        ownArray=sharedprefs.getOwnContactDetailArrayList(context)!!
        var id=ownArray.get(0).id
        var user_id=ownArray.get(0).user_id
        var title=ownArray.get(0).title
        var name=ownArray.get(0).name
        var last_name=ownArray.get(0).last_name
        var relationship=ownArray.get(0).relationship
        var email=ownArray.get(0).email
        var phone=ownArray.get(0).phone
        var code=ownArray.get(0).code
        var user_mobile=ownArray.get(0).user_mobile
        var address1=ownArray.get(0).address1
        var address2=ownArray.get(0).address2
        var address3=ownArray.get(0).address3
        var town=ownArray.get(0).town
        var state=ownArray.get(0).state
        var status=ownArray.get(0).status
        OWNDATA="\"own_details\":{\"id\":"+id+",\"user_id\":"+user_id+",\"title\":\""+title+"\",\"name\":\""+name+"\",\"last_name\":\""+last_name+"\",\"relationship\":\""+relationship+"\",\"email\":\""+email+"\",\"phone\":\""+phone+"\",\"code\":\""+code+"\",\"user_mobile\":\""+user_mobile+"\",\"address1\":\""+address1+"\",\"address2\":\""+address2+"\",\"address3\":\""+address3+"\",\"town\":\""+town+"\",\"state\":\""+state+"\",\"status\":"+status+"}"
        Log.e("OWNDATA",OWNDATA)
        
        kinArray=sharedprefs.getKinDetailPassArrayList(context)!!
        var FirstArray=ArrayList<KinDetailApiModel>()
        FirstArray=kinArray
        Log.e("Array KIHN",kinArray.size.toString())
        var newGson=Gson()
        var FIRSTDATA:String=newGson.toJson(FirstArray)
        Log.e("FIRSTDATA",FIRSTDATA)

        var healthArray=ArrayList<HealthInsuranceDetailModel>()
        healthArray=sharedprefs.getHealthDetailArrayList(context)!!
        var HEALTHARRAY=ArrayList<HealthInsuranceDetailModel>()
        HEALTHARRAY=healthArray
        var newHGson=Gson()
        var HEALTHDATA:String=newHGson.toJson(HEALTHARRAY)
        Log.e("HEALTHDATA",HEALTHDATA)

        var passportArray=ArrayList<PassportApiModel>()
        passportArray=sharedprefs.getPassportDetailArrayList(context)!!
        var PASSPORTARRAY=ArrayList<PassportApiModel>()
        PASSPORTARRAY=passportArray
        var newPGson=Gson()
        var PASSPORTDATA:String=newPGson.toJson(PASSPORTARRAY)
        Log.e("PASSPORTDATA",PASSPORTDATA)
        var JSONSTRING:String=""

        if (sharedprefs.getTriggerType(context)==1)
        {
            if (triggertype==3 && overallStatus==1)
            {
                JSONSTRING="{"+OWNDATA+","+"\"kin_details\""+":"+FIRSTDATA+" }"
            }
            else
            {
                JSONSTRING="{"+OWNDATA+","+"\"kin_details\""+":"+FIRSTDATA+","+"\"health_details\""+":"+HEALTHDATA+","+"\"passport_details\""+":"+PASSPORTDATA+" }"
            }
        }
        else
        {
            if (sharedprefs.getTriggerType(context)==2)
            {
                JSONSTRING="{"+OWNDATA+","+"\"kin_details\""+":"+FIRSTDATA+" }"
            }
            else{
                if (sharedprefs.getTriggerType(context)==3)
                {
                    JSONSTRING="{"+"\"health_details\""+":"+HEALTHDATA+","+"\"passport_details\""+":"+PASSPORTDATA+" }"
                }
            }
        }

      JSONSTRING= JSONSTRING.replace("\\", "")
       callSubmitionAPI(triggertype,overallStatus,JSONSTRING)




    }

    fun callSubmitionAPI(triggertype:Int,overallStatus:Int,JSONSTRING:String)
    {
        val token = sharedprefs.getaccesstoken(context)
        val requestLeaveBody= DataCollectionSubmissionModel(overallStatus,JSONSTRING,triggertype)
        Log.e("DATACOLLECTION:", requestLeaveBody.toString())
        val call: Call<ResponseBody> = ApiClient.getClient.dataCollectionSubmittion(requestLeaveBody,"Bearer "+token)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Failed", t.localizedMessage)

            }
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val responsedata = response.body()
                Log.e("Response Signup", responsedata.toString())
                if (responsedata != null) {
                    try {

                        val jsonObject = JSONObject(responsedata.string())
                        if(jsonObject.has(jsonConstans.STATUS)) {
                            val status: Int = jsonObject.optInt(jsonConstans.STATUS)
                            Log.e("STATUS LOGIN", status.toString())
                            if (status == 100)
                            {
                                showSuccessDataAlert(context,"Thank you for updating your details, please wait 5 working days for the changes to take effect","Alert",triggertype,overallStatus)



                            } else {
                                if (status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                                    callSubmitionAPI(triggertype,overallStatus,JSONSTRING)
                                } else {
                                    if (status == 103) {
                                        //validation check error
                                    } else {
                                        //check status code checks
                                        Log.e("Login Status", "Code entered")
                                        InternetCheckClass.checkApiStatusError(status, context)
                                    }
                                }

                            }
                        }
                    }
                    catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }
}
