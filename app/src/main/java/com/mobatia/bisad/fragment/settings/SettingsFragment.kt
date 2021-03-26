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
    lateinit var progress: ProgressBar
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
                mSettingsArrayListRegistered.add("Logout")
            }

        }
        initializeUI()
    }

    private fun initializeUI() {

        mSettingsListView = view!!.findViewById(R.id.mSettingsListView) as RecyclerView
        titleTextView = view!!.findViewById(R.id.titleTextView) as TextView
        progress = view!!.findViewById(R.id.progress) as ProgressBar
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

    private fun callLogoutApi(dialog:Dialog)
    {
        progress.visibility = View.VISIBLE

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
                    progress.visibility = View.GONE

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
    private fun callChangePasswordApi(currentPassword:String,newPassword:String,confirmPassword:String,dialog:Dialog) {
        val token = sharedprefs.getaccesstoken(mContext)
        val changePasswordBody =
            ChangePasswordApiModel(newPassword, confirmPassword, currentPassword)
        val call: Call<ResponseBody> =
            ApiClient.getClient.changePassword(changePasswordBody, "Bearer " + token)
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
                        if (jsonObject.has(jsonConstans.STATUS)) {
                            val status: Int = jsonObject.optInt(jsonConstans.STATUS)
                            Log.e("STATUS LOGIN", status.toString())
                            if (status == 100) {
                                InternetCheckClass.showErrorAlert(
                                    mContext,
                                    "Password successfully changed",
                                    "Alert"
                                )
                                dialog.dismiss()

                            } else {
                                if (status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(com.mobatia.bisad.fragment.home.mContext)
                                    callChangePasswordApi(
                                        currentPassword,
                                        newPassword,
                                        confirmPassword,
                                        dialog
                                    )
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
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }

}




