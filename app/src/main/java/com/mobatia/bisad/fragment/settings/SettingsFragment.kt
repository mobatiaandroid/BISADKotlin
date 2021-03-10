package com.mobatia.bisad.fragment.settings

import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import android.view.animation.RotateAnimation
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.common.LoginActivity
import com.mobatia.bisad.activity.home.DataCollectionActivity
import com.mobatia.bisad.activity.home.model.HealthInsuranceDetailAPIModel
import com.mobatia.bisad.activity.settings.termsofservice.TermsOfServiceActivity
import com.mobatia.bisad.activity.settings.tutorial.TutorialActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.home.model.BannerModel
import com.mobatia.bisad.fragment.home.model.StudentListDataCollection
import com.mobatia.bisad.fragment.home.model.datacollection.*
import com.mobatia.bisad.fragment.settings.adapter.SettingsRecyclerAdapter
import com.mobatia.bisad.fragment.settings.adapter.TriggerAdapter
import com.mobatia.bisad.fragment.settings.model.ChangePasswordApiModel
import com.mobatia.bisad.fragment.settings.model.TriggerDataModel
import com.mobatia.bisad.fragment.settings.model.TriggerUSer
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsFragment : Fragment(){
    lateinit var jsonConstans: JsonConstants
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var sharedprefs: PreferenceData
    lateinit var mSettingsListView: RecyclerView
    lateinit var mContext: Context
    lateinit var titleTextView: TextView
    lateinit var mSettingsArrayListRegistered : ArrayList<String>
    lateinit var mSettingsArrayListGuest: ArrayList<String>
    var start:Int=0
    var limit:Int=15
    lateinit var ownContactArrayList: ArrayList<OwnDetailsModel>
    lateinit var kinDetailArrayList: ArrayList<KinDetailsModel>
    lateinit var passportArrayList: ArrayList<PassportDetailModel>
    lateinit var healthDetailArrayList: ArrayList<HealthInsuranceDetailModel>
    lateinit var ownContactDetailSaveArrayList: ArrayList<OwnContactModel>
    lateinit var kinDetailSaveArrayList: ArrayList<KinDetailApiModel>
    lateinit var passportSaveArrayList: ArrayList<PassportApiModel>
    lateinit var healthSaveArrayList: ArrayList<HealthInsuranceDetailAPIModel>

    private var previousTriggerType: Int = 0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        mSettingsArrayListRegistered= ArrayList()
        mSettingsArrayListGuest=ArrayList()
        if(sharedprefs.getUserCode(mContext).equals(""))
        {
            mSettingsArrayListGuest.add("Change App Settings")
            mSettingsArrayListGuest.add("Terms of Service")
            mSettingsArrayListGuest.add("Email Help")
            mSettingsArrayListGuest.add("Tutorial")
            mSettingsArrayListGuest.add("Logout")
        }
        else{
            if (sharedprefs.getDataCollection(mContext)==1)
            {
                mSettingsArrayListRegistered.add("Change App Settings")
                mSettingsArrayListRegistered.add("Terms of Service")
                mSettingsArrayListRegistered.add("Email Help")
                mSettingsArrayListRegistered.add("Tutorial")
                mSettingsArrayListRegistered.add("Change Password")
                mSettingsArrayListRegistered.add("Logout")
            }
            else
            {
                mSettingsArrayListRegistered.add("Change App Settings")
                mSettingsArrayListRegistered.add("Terms of Service")
                mSettingsArrayListRegistered.add("Email Help")
                mSettingsArrayListRegistered.add("Tutorial")
                mSettingsArrayListRegistered.add("Change Password")
                mSettingsArrayListRegistered.add("Update Account Details")
                mSettingsArrayListRegistered.add("Logout")
            }

        }
        initializeUI()
    }

    private fun initializeUI() {

        mSettingsListView = view!!.findViewById(R.id.mSettingsListView) as RecyclerView
        titleTextView = view!!.findViewById(R.id.titleTextView) as TextView
        titleTextView.text = "Settings"
        linearLayoutManager = LinearLayoutManager(mContext)
        mSettingsListView.layoutManager = linearLayoutManager
        mSettingsListView.itemAnimator = DefaultItemAnimator()
        mSettingsListView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                 if (sharedprefs.getUserCode(mContext).equals(""))
                 {
                     if(position==0)
                     {
                         val intent = Intent()
                         intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                         val uri = Uri.fromParts("package", activity!!.packageName, null)
                         intent.data = uri
                         mContext.startActivity(intent)
                     }
                    else if(position==1)
                     {
                         val intent =Intent(activity, TermsOfServiceActivity::class.java)
                         activity?.startActivity(intent)
                     }
                     else if (position==2)
                     {
                         val deliveryAddress =
                             arrayOf("communications@bisaddubai.com")
                         val emailIntent = Intent(Intent.ACTION_SEND)
                         emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress)
                         emailIntent.type = "text/plain"
                         emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                         val pm: PackageManager = mContext.packageManager
                         val activityList = pm.queryIntentActivities(
                             emailIntent, 0
                         )
                         println("packge size" + activityList.size)
                         for (app in activityList) {
                             println("packge name" + app.activityInfo.name)
                             if (app.activityInfo.name.contains("com.google.android.gm")) {
                                 val activity = app.activityInfo
                                 val name = ComponentName(
                                     activity.applicationInfo.packageName, activity.name
                                 )
                                 emailIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                                 emailIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                                         or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                                 emailIntent.component = name
                                 mContext.startActivity(emailIntent)
                                 break
                             }
                         }
//                         val to:String="communications@bisaddubai.com"
//                         val email = Intent(Intent.ACTION_SEND)
//                         email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
//                         email.type = "message/rfc822"
//                         startActivity(Intent.createChooser(email, "Choose an Email client :"))
                     }
                     else if (position==3)
                     {
                         val intent =Intent(activity, TutorialActivity::class.java)
                         activity?.startActivity(intent)
                     }
                    else if (position==4)
                     {
                         sharedprefs.setUserCode(mContext,"")
                         sharedprefs.setUserID(mContext,"")
                         val mIntent = Intent(activity, LoginActivity::class.java)
                         mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                         activity!!.startActivity(mIntent)
                     }
                 }
                else{
                     if (sharedprefs.getDataCollection(mContext)==1)
                     {
                         if(position==0)
                         {
                             val intent = Intent()
                             intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                             val uri = Uri.fromParts("package", activity!!.packageName, null)
                             intent.data = uri
                             mContext.startActivity(intent)
                         }
                         else if(position==1)
                         {
                             val intent =Intent(activity, TermsOfServiceActivity::class.java)
                             activity?.startActivity(intent)
                         }
                         else if (position==2)
                         {
                             val deliveryAddress =
                                 arrayOf("communications@bisaddubai.com")
                             val emailIntent = Intent(Intent.ACTION_SEND)
                             emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress)
                             emailIntent.type = "text/plain"
                             emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                             val pm: PackageManager = mContext.packageManager
                             val activityList = pm.queryIntentActivities(
                                 emailIntent, 0
                             )
                             println("packge size" + activityList.size)
                             for (app in activityList) {
                                 println("packge name" + app.activityInfo.name)
                                 if (app.activityInfo.name.contains("com.google.android.gm")) {
                                     val activity = app.activityInfo
                                     val name = ComponentName(
                                         activity.applicationInfo.packageName, activity.name
                                     )
                                     emailIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                                     emailIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                                             or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                                     emailIntent.component = name
                                     mContext.startActivity(emailIntent)
                                     break
                                 }
                             }

//                             val to:String="communications@bisaddubai.com"
//                             val email = Intent(Intent.ACTION_SEND)
//                             email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
//                             email.type = "message/rfc822"
//                             startActivity(Intent.createChooser(email, "Choose an Email client :"))
                         }
                         else if (position==3)
                         {
                             val intent =Intent(activity, TutorialActivity::class.java)
                             activity?.startActivity(intent)
                         }
                         else if (position==4)
                         {
                             changePassword(mContext)
                         }
//                         else if (position==5)
//                         {
//                             showTriggerDataCollection(mContext,"Confirm?", "Select one or more areas to update", R.drawable.questionmark_icon, R.drawable.round)
//                         }
                         else if (position==5)
                         {
                             showSuccessAlert(mContext, "Confirm?", "Do you want to Logout?", R.drawable.questionmark_icon, R.drawable.round)

                         }
                     }
                     else{
                         if(position==0)
                         {
                             val intent = Intent()
                             intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                             val uri = Uri.fromParts("package", activity!!.packageName, null)
                             intent.data = uri
                             mContext.startActivity(intent)
                         }
                         else if(position==1)
                         {
                             val intent =Intent(activity, TermsOfServiceActivity::class.java)
                             activity?.startActivity(intent)
                         }
                         else if (position==2)
                         {
                             val deliveryAddress =
                                 arrayOf("communications@bisaddubai.com")
                             val emailIntent = Intent(Intent.ACTION_SEND)
                             emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress)
                             emailIntent.type = "text/plain"
                             emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                             val pm: PackageManager = mContext.packageManager
                             val activityList = pm.queryIntentActivities(
                                 emailIntent, 0
                             )
                             println("packge size" + activityList.size)
                             for (app in activityList) {
                                 println("packge name" + app.activityInfo.name)
                                 if (app.activityInfo.name.contains("com.google.android.gm")) {
                                     val activity = app.activityInfo
                                     val name = ComponentName(
                                         activity.applicationInfo.packageName, activity.name
                                     )
                                     emailIntent.addCategory(Intent.CATEGORY_LAUNCHER)
                                     emailIntent.flags = (Intent.FLAG_ACTIVITY_NEW_TASK
                                             or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
                                     emailIntent.component = name
                                     mContext.startActivity(emailIntent)
                                     break
                                 }
                             }
//                             val to:String="communications@bisaddubai.com"
//                             val email = Intent(Intent.ACTION_SEND)
//                             email.putExtra(Intent.EXTRA_EMAIL, arrayOf(to))
//                             email.type = "message/rfc822"
//                             startActivity(Intent.createChooser(email, "Choose an Email client :"))
                         }
                         else if (position==3)
                         {
                             val intent =Intent(activity, TutorialActivity::class.java)
                             activity?.startActivity(intent)
                         }
                         else if (position==4)
                         {
                             changePassword(mContext)
                         }
                         else if (position==5)
                         {
                             showTriggerDataCollection(mContext,"Confirm?", "Select one or more areas to update", R.drawable.questionmark_icon, R.drawable.round)
                         }
                         else if (position==6)
                         {
                             showSuccessAlert(mContext, "Confirm?", "Do you want to Logout?", R.drawable.questionmark_icon, R.drawable.round)

                         }
                     }

                 }
            }
        })
        if (sharedprefs.getUserCode(mContext).equals(""))
        {
            val settingsAdapter = SettingsRecyclerAdapter(mContext,mSettingsArrayListGuest)
            mSettingsListView.adapter = settingsAdapter
        }
        else{
            val settingsAdapter = SettingsRecyclerAdapter(mContext,mSettingsArrayListRegistered)
            mSettingsListView.adapter = settingsAdapter
        }

    }


    fun showSuccessDataAlert(context: Context,msgHead:String,msg:String,ico:Int,bgIcon:Int)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        iconImageView.setBackgroundResource(bgIcon)
        iconImageView.setImageResource(ico)
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        text_dialog.text = msg
        alertHead.text = msgHead
        btn_Ok.setOnClickListener()
        {

            dialog.dismiss()


        }
        btn_Cancel.setOnClickListener()
        {
            dialog.dismiss()

        }
        dialog.show()
    }

    fun showSuccessAlert(context: Context,msgHead:String,msg:String,ico:Int,bgIcon:Int)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_dialogue_layout)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        iconImageView.setBackgroundResource(bgIcon)
        iconImageView.setImageResource(ico)
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        text_dialog.text = msg
        alertHead.text = msgHead
        btn_Ok.setOnClickListener()
        {
            if (sharedprefs.getUserCode(mContext).equals("")) {
                sharedprefs.setUserCode(mContext,"")
                sharedprefs.setUserID(mContext,"")
                val mIntent = Intent(activity, LoginActivity::class.java)
                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                activity!!.startActivity(mIntent)

            } else{
                callLogoutApi(dialog)
            }
            dialog.dismiss()
        }
        btn_Cancel.setOnClickListener()
        {
            dialog.dismiss()
        }
        dialog.show()
    }

    fun showTriggerDataCollection(context: Context,msgHead:String,msg:String,ico:Int,bgIcon:Int)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_trigger_data_collection)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var checkRecycler = dialog.findViewById(R.id.checkRecycler) as RecyclerView
       var linearLayoutManagerM :LinearLayoutManager = LinearLayoutManager(mContext)
        checkRecycler.layoutManager = linearLayoutManagerM
        checkRecycler.itemAnimator = DefaultItemAnimator()
        iconImageView.setBackgroundResource(bgIcon)
        iconImageView.setImageResource(ico)
        var alertHead = dialog.findViewById(R.id.alertHead) as TextView
        var text_dialog = dialog.findViewById(R.id.text_dialog) as TextView
        var btn_Ok = dialog.findViewById(R.id.btn_Ok) as Button
        var btn_Cancel = dialog.findViewById(R.id.btn_Cancel) as Button
        var progressDialog = dialog.findViewById(R.id.progress) as ProgressBar

        text_dialog.text = msg
        alertHead.text = msgHead
        var categoryList= ArrayList<String>()
        categoryList.add("All")
        categoryList.add("Student - Contact Details")
        categoryList.add("Student - Passport & Emirates ID")

        val mTriggerModelArrayList=ArrayList<TriggerDataModel>()
        for (i in 0..categoryList.size-1)
        {
           var model=TriggerDataModel()
            model.categoryName=categoryList.get(i)
            model.checkedCategory=false
            mTriggerModelArrayList.add(model)

        }
        var triggerAdapter=TriggerAdapter(mTriggerModelArrayList)
        checkRecycler.adapter = triggerAdapter
        checkRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                if (position==0)
                {
                   mTriggerModelArrayList.get(0).checkedCategory=true
                   mTriggerModelArrayList.get(1).checkedCategory=false
                   mTriggerModelArrayList.get(2).checkedCategory=false
                }
                else if (position==1)
                {
                    mTriggerModelArrayList.get(0).checkedCategory=false
                    mTriggerModelArrayList.get(1).checkedCategory=true
                    mTriggerModelArrayList.get(2).checkedCategory=false
                }
                else{
                    mTriggerModelArrayList.get(0).checkedCategory=false
                    mTriggerModelArrayList.get(1).checkedCategory=false
                    mTriggerModelArrayList.get(2).checkedCategory=true
                }

                var triggerAdapter=TriggerAdapter(mTriggerModelArrayList)
                checkRecycler.adapter = triggerAdapter
            }
        })
        btn_Ok.setOnClickListener()
        {
            var valueTrigger:String="0"
            if (mTriggerModelArrayList.get(0).checkedCategory) {
                valueTrigger="1"
            } else if (mTriggerModelArrayList.get(1).checkedCategory) {
                valueTrigger="2"
            } else if (mTriggerModelArrayList.get(2).checkedCategory) {
                valueTrigger="3"
            }

            if (valueTrigger.equals("0")) {
                Toast.makeText(
                    mContext,
                    "Please select any trigger type before confiming",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                progressDialog.visibility=View.VISIBLE
               callDataTriggerApi(valueTrigger,dialog,progressDialog)
            }

            // dialog.dismiss()
        }
        btn_Cancel.setOnClickListener()
        {
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun callLogoutApi(dialog:Dialog)
    {
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<ResponseBody> = ApiClient.getClient.logout("Bearer "+token)
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
                            if (status == 100) {
                                sharedprefs.setUserCode(mContext,"")
                                sharedprefs.setUserID(mContext,"")

                                var dummyOwn=ArrayList<OwnContactModel>()
                                sharedprefs.setOwnContactDetailArrayList(mContext,dummyOwn)
                                var dummyKinPass=ArrayList<KinDetailApiModel>()
                                sharedprefs.setKinDetailPassArrayList(mContext,dummyKinPass)
                                var dummyKinShow=ArrayList<KinDetailApiModel>()
                                sharedprefs.setKinDetailArrayList(mContext,dummyKinShow)
                                var dummyHealth=ArrayList<HealthInsuranceDetailAPIModel>()
                                sharedprefs.setHealthDetailArrayList(mContext,dummyHealth)
                                var dummyPassport=ArrayList<PassportApiModel>()
                                sharedprefs.setPassportDetailArrayList(mContext,dummyPassport)
                                var dummyStudent=ArrayList<StudentListDataCollection>()
                                sharedprefs.setStudentArrayList(mContext,dummyStudent)
                                sharedprefs.setUserEmail(mContext,"")
                                sharedprefs.setUserCode(mContext,"")
                                sharedprefs.setUserID(mContext,"")
                                val mIntent = Intent(activity, LoginActivity::class.java)
                                mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                                activity!!.startActivity(mIntent)
                                dialog.dismiss()

                            } else {
                                if (status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                                    callLogoutApi(dialog)
                                } else {
                                    if (status == 103) {
                                        //validation check error
                                    } else {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status, mContext)
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

    fun changePassword(context: Context)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(true)
        dialog.setContentView(R.layout.custom_dialog_changepassword)
        var text_currentpassword = dialog.findViewById(R.id.text_currentpassword) as EditText
        var text_currentnewpassword = dialog.findViewById(R.id.text_currentnewpassword) as EditText
        var text_confirmpassword = dialog.findViewById(R.id.text_confirmpassword) as EditText
        var btn_changepassword = dialog.findViewById(R.id.btn_changepassword) as Button
        var btn_cancel = dialog.findViewById(R.id.btn_cancel) as Button
        btn_cancel.isClickable=true
        btn_cancel.setOnClickListener()
        {

            dialog.dismiss()
        }

        btn_changepassword.setOnClickListener()
        {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(text_currentpassword.windowToken, 0)
            val imn= context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imn.hideSoftInputFromWindow(text_currentnewpassword.windowToken, 0)
            val imo= context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imo.hideSoftInputFromWindow(text_confirmpassword.windowToken, 0)
            if (text_currentpassword.text.toString().trim().equals("")) {
                InternetCheckClass. showErrorAlert(context,"Please enter Current Password","Alert")
            } else{
                if (text_currentnewpassword.text.toString().trim().equals("")) {
                    InternetCheckClass. showErrorAlert(context,"Please enter New Password","Alert")
                } else{
                    if (text_confirmpassword.text.toString().trim().equals("")) {
                        InternetCheckClass. showErrorAlert(context,"Please enter Confirm Password","Alert")
                    } else{
                        callChangePasswordApi(text_currentpassword.text.toString().trim(),text_currentnewpassword.text.toString().trim(),text_confirmpassword.text.toString(),dialog)
                    }
                }
            }
            //dialog.dismiss()
        }


        dialog.show()
    }
    private fun callChangePasswordApi(currentPassword:String,newPassword:String,confirmPassword:String,dialog:Dialog)
    {
        val token = sharedprefs.getaccesstoken(mContext)
        val changePasswordBody= ChangePasswordApiModel(newPassword,confirmPassword,currentPassword)
        val call: Call<ResponseBody> = ApiClient.getClient.changePassword(changePasswordBody,"Bearer "+token)
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
                            if (status == 100) {
                                InternetCheckClass. showErrorAlert(mContext,"Password successfully changed","Alert")
                                dialog.dismiss()

                            } else {
                                if (status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                                    callChangePasswordApi(currentPassword,newPassword,confirmPassword,dialog)
                                } else {
                                    if (status == 103) {
                                        //validation check error
                                    } else {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status, mContext)
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
fun callDataTriggerApi(value:String,triggerDialog:Dialog,progress:ProgressBar)
{
    val token = sharedprefs.getaccesstoken(mContext)
    val requestLeaveBody= TriggerUSer(value)
    val call: Call<ResponseBody> = ApiClient.getClient.triggerUser(requestLeaveBody,"Bearer "+token)
    call.enqueue(object : Callback<ResponseBody> {
        override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
            Log.e("Failed", t.localizedMessage)
            progress.visibility=View.GONE

        }
        override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
            val responsedata = response.body()
            Log.e("Response Signup", responsedata.toString())
            progress.visibility=View.GONE
            if (responsedata != null) {
                try {

                    val jsonObject = JSONObject(responsedata.string())
                    if(jsonObject.has(jsonConstans.STATUS)) {
                        val status: Int = jsonObject.optInt(jsonConstans.STATUS)
                        Log.e("STATUS LOGIN", status.toString())
                        if (status == 100) {
                            progress.visibility=View.GONE
                            triggerDialog.dismiss()
                            callSettingsUserDetail()
                           // showSuccessDataAlert(mContext,"Alert","\"Update Account Details\" will start next time the Parent App is opened.", R.drawable.questionmark_icon, R.drawable.round)

                        } else {
                            if (status == 116) {
                                //call Token Expired
                                AccessTokenClass.getAccessToken(mContext)
                                callDataTriggerApi(value,triggerDialog,progress)
                            } else {
                                if (status == 103) {
                                    //validation check error
                                } else {
                                    //check status code checks
                                    InternetCheckClass.checkApiStatusError(status, mContext)
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

    fun callSettingsUserDetail()
    {
        val bannerModel= BannerModel("1.0.0",2)
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<ResponseBody> = ApiClient.getClient.settingsUserDetail(bannerModel,"Bearer "+token)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val bannerresponse = response.body()
                if (bannerresponse != null) {
                    try {
                        val jsonObject = JSONObject(bannerresponse.string())
                        if(jsonObject.has(jsonConstans.STATUS))
                        {
                            val status : Int=jsonObject.optInt(jsonConstans.STATUS)
                            if (status==100)
                            {
                                val responseObj =jsonObject.getJSONObject("responseArray")
                                val appVersion=responseObj.optString("android_app_version")
                                val data_collection=responseObj.optInt("data_collection")
                                val trigger_type=responseObj.optInt("trigger_type")
                                val already_triggered=responseObj.optInt("already_triggered")
                                sharedprefs.setAppVersion(mContext,appVersion)
                                sharedprefs.setDataCollection(mContext,data_collection)
                               sharedprefs.setTriggerType(mContext,trigger_type)
                                sharedprefs.setAlreadyTriggered(mContext,already_triggered)

                                if (sharedprefs.getDataCollection(mContext
                                    )==1)
                                {
                                    Handler().postDelayed({

                                        if (sharedprefs.getAlreadyTriggered(
                                                mContext
                                            )==0)
                                        {
                                            callDataCollectionAPI()

                                        }
                                        else
                                        {
                                            if (previousTriggerType == sharedprefs.getTriggerType(mContext
                                                ))
                                            {
                                                if(!sharedprefs.getSuspendTrigger(
                                                        mContext
                                                    ).equals("1"))
                                                {
                                                    val intent =Intent(activity, DataCollectionActivity::class.java)
                                                    activity?.startActivity(intent)
                                                }

                                            }
                                            else{
                                                callDataCollectionAPI()
                                            }
                                        }
                                    }, 3000)
                                }

                            }

                            else{
                                if (status==116)
                                {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(mContext)
                                    callSettingsUserDetail()

                                }
                                else
                                {
                                    if (status==103)
                                    {
                                        //validation check error

                                    }
                                    else
                                    {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status, mContext
                                        )
                                    }
                                }

                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }
    fun callDataCollectionAPI()
    {
        ownContactArrayList= ArrayList()
        kinDetailArrayList = ArrayList()
        passportArrayList = ArrayList()
        healthDetailArrayList = ArrayList()
        ownContactDetailSaveArrayList = ArrayList()
        passportSaveArrayList = ArrayList()
        healthSaveArrayList = ArrayList()
        kinDetailSaveArrayList = ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<DataCollectionModel> = ApiClient.getClient.dataCollectionDetail("Bearer "+token)
        call.enqueue(object : Callback<DataCollectionModel>{
            override fun onFailure(call: Call<DataCollectionModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<DataCollectionModel>, response: Response<DataCollectionModel>) {
                if (response.body()!!.status==100)
                {

                   sharedprefs.setDisplayMessage(mContext,response.body()!!.responseArray.display_message)
                    ownContactArrayList =response.body()!!.responseArray.ownDetailsList
                    kinDetailArrayList =response.body()!!.responseArray.kinDetailsList
                    passportArrayList =response.body()!!.responseArray.passportDetailsList
                    healthDetailArrayList =response.body()!!.responseArray.healthInsurenceList
                    if (ownContactArrayList.size>0)
                    {
                        for (i in 0..ownContactArrayList.size-1)
                        {
                            var model=OwnContactModel()
                            model.id= ownContactArrayList.get(i).id
                            model.user_id= ownContactArrayList.get(i).user_id
                            model.title= ownContactArrayList.get(i).title
                            model.name= ownContactArrayList.get(i).name
                            model.last_name= ownContactArrayList.get(i).last_name
                            model.relationship= ownContactArrayList.get(i).relationship
                            model.email= ownContactArrayList.get(i).email
                            model.phone= ownContactArrayList.get(i).phone
                            model.code= ownContactArrayList.get(i).code
                            model.user_mobile= ownContactArrayList.get(i).user_mobile
                            model.address1= ownContactArrayList.get(i).address1
                            model.address2= ownContactArrayList.get(i).address2
                            model.address3= ownContactArrayList.get(i).address3
                            model.town= ownContactArrayList.get(i).town
                            model.state= ownContactArrayList.get(i).state
                            model.country= ownContactArrayList.get(i).country
                            model.pincode= ownContactArrayList.get(i).pincode
                            model.status= ownContactArrayList.get(i).status
                            model.created_at= ownContactArrayList.get(i).created_at
                            model.updated_at= ownContactArrayList.get(i).updated_at
                            model.isUpdated= false
                            model.isConfirmed= false
                            ownContactDetailSaveArrayList.add(model)

                        }

                        if(sharedprefs.getOwnContactDetailArrayList(mContext
                            )==null|| sharedprefs.getOwnContactDetailArrayList(mContext
                            )!!.size==0)
                        {
                            sharedprefs.setIsAlreadyEnteredOwn(mContext,true)
                            sharedprefs.setOwnContactDetailArrayList(
                                mContext, ownContactDetailSaveArrayList
                            )
                        }
                        else{
                            if (!sharedprefs.getIsAlreadyEnteredOwn(
                                    mContext
                                ))
                            {
                               sharedprefs.setIsAlreadyEnteredOwn(
                                   mContext,true)
                             sharedprefs.setOwnContactDetailArrayList(
                                    mContext, ownContactDetailSaveArrayList
                                )
                            }
                        }
                    }

                    if(passportArrayList.size>0)
                    {
                        for (i in 0..passportArrayList.size-1)
                        {
                            var mModel=PassportApiModel()
                            mModel.id= passportArrayList.get(i).id
                            mModel.student_unique_id= passportArrayList.get(i).student_unique_id
                            mModel.student_id= passportArrayList.get(i).student_id
                            mModel.student_name= passportArrayList.get(i).student_name
                            mModel.passport_number= passportArrayList.get(i).passport_number
                            mModel.nationality= passportArrayList.get(i).nationality
                            mModel.passport_image= passportArrayList.get(i).passport_image
                            mModel.date_of_issue= passportArrayList.get(i).date_of_issue
                            mModel.expiry_date= passportArrayList.get(i).expiry_date
                            mModel.passport_expired= passportArrayList.get(i).passport_expired
                            mModel.emirates_id_no= passportArrayList.get(i).emirates_id_no
                            mModel.emirates_id_image= passportArrayList.get(i).emirates_id_image
                            mModel.passport_image_name= ""
                            mModel.emirates_id_image_path= ""
                            mModel.emirates_id_image_name=""
                            mModel.emirates_id_image_path=""
                            mModel.status= passportArrayList.get(i).status
                            mModel.request= passportArrayList.get(i).request
                            mModel.created_at= passportArrayList.get(i).created_at
                            mModel.updated_at= passportArrayList.get(i).updated_at
                            mModel.is_date_changed= false
                            passportSaveArrayList.add(mModel)
                        }
                        if (sharedprefs.getPassportDetailArrayList(
                               mContext
                            )==null ||sharedprefs.getPassportDetailArrayList(
                               mContext
                            )!!.size==0)
                        {
                           sharedprefs.setPassportDetailArrayList(
                               mContext, passportSaveArrayList
                            )
                        }

                    }

                    if(healthDetailArrayList.size>0)
                    {
                        for (i in 0..healthDetailArrayList.size-1)
                        {
                            var hModel=HealthInsuranceDetailAPIModel()
                            hModel.id= healthDetailArrayList.get(i).id
                            hModel.student_unique_id= healthDetailArrayList.get(i).student_unique_id
                            hModel.student_id= healthDetailArrayList.get(i).student_id
                            hModel.student_name= healthDetailArrayList.get(i).student_name
                            hModel.health_detail= healthDetailArrayList.get(i).health_detail
                            hModel.health_form_link= healthDetailArrayList.get(i).health_form_link
                            hModel.status= healthDetailArrayList.get(i).status
                            hModel.request= healthDetailArrayList.get(i).request
                            hModel.created_at= healthDetailArrayList.get(i).created_at
                            hModel.updated_at= healthDetailArrayList.get(i).updated_at
                            healthSaveArrayList.add(hModel)

                        }
                        if (sharedprefs.getHealthDetailArrayList(mContext)==null || sharedprefs.getHealthDetailArrayList(
                                mContext
                            )!!.size==0)
                        {
                            sharedprefs.setHealthDetailArrayList(mContext, healthSaveArrayList)
                        }
                    }
                    if (kinDetailArrayList.size>0)
                    {
                        for(i in 0..kinDetailArrayList.size-1)
                        {
                            var mModel=KinDetailApiModel()
                            mModel.id= kinDetailArrayList.get(i).id
                            mModel.user_id= kinDetailArrayList.get(i).user_id
                            mModel.kin_id= kinDetailArrayList.get(i).kin_id
                            mModel.title= kinDetailArrayList.get(i).title
                            mModel.name= kinDetailArrayList.get(i).name
                            mModel.last_name= kinDetailArrayList.get(i).last_name
                            mModel.relationship= kinDetailArrayList.get(i).relationship
                            mModel.email= kinDetailArrayList.get(i).email
                            mModel.phone= kinDetailArrayList.get(i).phone
                            mModel.code= kinDetailArrayList.get(i).code
                            mModel.user_mobile= kinDetailArrayList.get(i).user_mobile
                            mModel.status= kinDetailArrayList.get(i).status
                            mModel.request= kinDetailArrayList.get(i).request
                            mModel.created_at= kinDetailArrayList.get(i).created_at
                            mModel.updated_at= kinDetailArrayList.get(i).updated_at
                            mModel.NewData=false
                            mModel.Newdata="NO"
                            mModel.isConfirmed=false
                            kinDetailSaveArrayList.add(mModel)

                        }
                        if(sharedprefs.getKinDetailArrayList(mContext)==null|| sharedprefs.getKinDetailArrayList(
                                mContext
                            )!!.size==0)
                        {
                            Log.e("DATA COLLECTION","ENTERS2")
                            sharedprefs.setIsAlreadyEnteredKin(mContext,true)
                           sharedprefs.setKinDetailArrayList(mContext, kinDetailSaveArrayList)
                            sharedprefs.setKinDetailPassArrayList(
                                mContext, kinDetailSaveArrayList
                            )
                        }
                        else{
                            Log.e("DATA COLLECTION","ENTERS3")
                            if (!sharedprefs.getIsAlreadyEnteredKin(
                                   mContext
                                ))
                            {
                                Log.e("DATA COLLECTION","ENTERS4")
                                sharedprefs.setIsAlreadyEnteredKin(
                                    mContext,true)
                                sharedprefs.setKinDetailArrayList(
                                    mContext, kinDetailSaveArrayList
                                )
                                sharedprefs.setKinDetailPassArrayList(mContext, kinDetailSaveArrayList
                                )
                            }
                        }
                    }

                    //Intent
                    Log.e("DATA COLLECTION","ENTERS5")
                    if(!sharedprefs.getSuspendTrigger(mContext).equals("1"))
                    {
                        val intent =Intent(mContext, DataCollectionActivity::class.java)
                        activity?.startActivity(intent)
                    }

                }
                else{
                    if (response.body()!!.status==116)
                    {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(mContext)
                        callDataCollectionAPI()

                    }
                    else
                    {
                        if (response.body()!!.status==103)
                        {
                            //validation check error

                        }
                        else
                        {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(response.body()!!.status,
                                mContext
                            )
                        }
                    }

                }
            }

        })
    }
}




