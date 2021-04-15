package com.mobatia.bisad.activity.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.ClipData
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.adapter.HomeListAdapter
import com.mobatia.bisad.activity.home.model.HealthInsuranceDetailAPIModel
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.apps.AppsFragment
import com.mobatia.bisad.fragment.calendar_new.CalendarFragmentNew
import com.mobatia.bisad.fragment.communication.CommunicationFragment
import com.mobatia.bisad.fragment.contact_us.ContactUsFragment
import com.mobatia.bisad.fragment.curriculum.CurriculumFragment
import com.mobatia.bisad.fragment.home.*
import com.mobatia.bisad.fragment.home.model.BannerModel
import com.mobatia.bisad.fragment.home.model.datacollection.DataCollectionModel
import com.mobatia.bisad.fragment.home.model.datacollection.KinDetailApiModel
import com.mobatia.bisad.fragment.home.model.datacollection.OwnContactModel
import com.mobatia.bisad.fragment.home.model.datacollection.PassportApiModel
import com.mobatia.bisad.fragment.messages.MessageFragment
import com.mobatia.bisad.fragment.report_absence.ReportAbsenceFragment
import com.mobatia.bisad.fragment.reports.ReportsFragment
import com.mobatia.bisad.fragment.settings.SettingsFragment
import com.mobatia.bisad.fragment.settings.adapter.TriggerAdapter
import com.mobatia.bisad.fragment.settings.model.TriggerDataModel
import com.mobatia.bisad.fragment.settings.model.TriggerUSer
import com.mobatia.bisad.fragment.socialmedia.SocialMediaFragment
import com.mobatia.bisad.fragment.student_information.StudentInformationFragment
import com.mobatia.bisad.fragment.teacher_contact.TeacherContactFragment
import com.mobatia.bisad.fragment.termdates.TermDatesFragment
import com.mobatia.bisad.fragment.time_table.TimeTableFragment
import com.mobatia.bisad.manager.MyDragShadowBuilder
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


@Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "DEPRECATION")
class HomeActivity : AppCompatActivity(), OnItemLongClickListener {

    val manager = supportFragmentManager
    lateinit var sharedprefs: PreferenceData
    lateinit var navigation_menu: ImageView
    lateinit var settings_icon: ImageView
    lateinit var shadowBuilder: MyDragShadowBuilder
    lateinit var jsonConstans: JsonConstants
    lateinit var context: Context
    lateinit var clipData: ClipData
    lateinit var mListItemArray: Array<String>
    var mListImgArray: TypedArray? = null
    lateinit var linear_layout: LinearLayout
    lateinit var drawer_layout: DrawerLayout
    lateinit var toolbar: Toolbar
    lateinit var logoClickImgView: ImageView
    lateinit var homelist: ListView
    lateinit var homeprogress:ProgressBar
    var mFragment: Fragment? = null
    var sPosition: Int = 0
    var previousTriggerTypeNew: Int = 0

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        setContentView(R.layout.activity_main)
        Intent.FLAG_ACTIVITY_CLEAR_TASK
        initializeUI()
        showfragmenthome()
    }

    fun showfragmenthome() {
        val transaction = manager.beginTransaction()
        val fragment = HomescreenFragment()
        transaction.replace(R.id.fragment_holder, fragment)
        transaction.commit()
    }

    @SuppressLint("Recycle", "WrongViewCast")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initializeUI() {

        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()
        context = this
        previousTriggerTypeNew = sharedprefs.getTriggerType(context)
        homelist = findViewById<ListView>(R.id.homelistview)
        drawer_layout = findViewById(R.id.drawer_layout)
        linear_layout = findViewById(R.id.linear_layout)
        homeprogress = findViewById(R.id.homeprogress)
        var downarrow = findViewById<ImageView>(R.id.downarrow)

        mListItemArray =
            applicationContext.resources.getStringArray(R.array.navigation_items_guest)
        mListImgArray =
            applicationContext.resources.obtainTypedArray(R.array.navigation_icons_guest)


        val width = (resources.displayMetrics.widthPixels / 1.7).toInt()
        val params = linear_layout
            .layoutParams as DrawerLayout.LayoutParams
        params.width = width
        linear_layout.layoutParams = params
        val myListAdapter = HomeListAdapter(this, mListItemArray, mListImgArray!!)
        homelist.adapter = myListAdapter
        homelist.onItemLongClickListener = this


        homelist.setOnItemClickListener { adapterView, view, position, id ->
            val itemAtPos = adapterView.getItemAtPosition(position)
            val itemIdAtPos = adapterView.getItemIdAtPosition(position)
            if (sharedprefs.getUserCode(context).equals("")) {
                if (position == 0) {
                    mFragment = HomescreenFragment()
                    replaceFragmentsSelected(position)
                } else if (position == 1) {
                    showSuccessAlert(
                        context,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                } else if (position == 2) {
                    showSuccessAlert(
                        context,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                } else if (position == 3) {
                    showSuccessAlert(
                        context,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                } else if (position == 4) {
                    mFragment = CommunicationFragment()
                    replaceFragmentsSelected(position)
                } else if (position == 5) {
                    showSuccessAlert(
                        context,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                } else if (position == 6) {
                    showSuccessAlert(
                        context,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                } else if (position == 7) {
                    mFragment = SocialMediaFragment()
                    replaceFragmentsSelected(position)
                } else if (position == 8) {
                    showSuccessAlert(
                        context,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
//                else if (position == 9) {
//                    showSuccessAlert(
//                        context,
//                        "This feature is only available for registered users.",
//                        "Alert"
//                    )
//                }
                else if (position == 9) {
                    showSuccessAlert(
                        context,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                } else if (position == 10) {
                    showSuccessAlert(
                        context,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                } else if (position == 11) {
                    showSuccessAlert(
                        context,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                else if (position == 12) {
                    showSuccessAlert(
                        context,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                } else if (position == 13) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        checkpermission()


                    } else {
                        mFragment = ContactUsFragment()
                        replaceFragmentsSelected(position)
                    }

                } else if (position == 14) {
                    showSuccessAlert(
                        context,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
            } else {
                if (position == 0) {
                    mFragment = HomescreenFragment()
                    replaceFragmentsSelected(position)
                } else if (position == 1) {
                    sharedprefs.setStudentID(context, "")
                    sharedprefs.setStudentName(context, "")
                    sharedprefs.setStudentPhoto(context, "")
                    sharedprefs.setStudentClass(context, "")
                    mFragment = StudentInformationFragment()
                    replaceFragmentsSelected(position)
                } else if (position == 2) {
                    sharedprefs.setStudentID(context, "")
                    sharedprefs.setStudentName(context, "")
                    sharedprefs.setStudentPhoto(context, "")
                    sharedprefs.setStudentClass(context, "")
                    mFragment = CalendarFragmentNew()
                    replaceFragmentsSelected(position)
                } else if (position == 3) {
                    mFragment = MessageFragment()
                    replaceFragmentsSelected(position)
                } else if (position == 4) {
                    mFragment = CommunicationFragment()
                    replaceFragmentsSelected(position)
                } else if (position == 5) {
                    sharedprefs.setStudentID(context, "")
                    sharedprefs.setStudentName(context, "")
                    sharedprefs.setStudentPhoto(context, "")
                    sharedprefs.setStudentClass(context, "")
                    mFragment = ReportAbsenceFragment()
                    replaceFragmentsSelected(position)
                } else if (position == 6) {
                    sharedprefs.setStudentID(context, "")
                    sharedprefs.setStudentName(context, "")
                    sharedprefs.setStudentPhoto(context, "")
                    sharedprefs.setStudentClass(context, "")
                    mFragment = TeacherContactFragment()
                    replaceFragmentsSelected(position)
                } else if (position == 7) {
                    mFragment = SocialMediaFragment()
                    replaceFragmentsSelected(position)
                } else if (position == 8) {
                    sharedprefs.setStudentID(context, "")
                    sharedprefs.setStudentName(context, "")
                    sharedprefs.setStudentPhoto(context, "")
                    sharedprefs.setStudentClass(context, "")
                    mFragment = ReportsFragment()
                    replaceFragmentsSelected(position)
                }

//              else if (position == 9) {
//                    sharedprefs.setStudentID(context, "")
//                    sharedprefs.setStudentName(context, "")
//                    sharedprefs.setStudentPhoto(context, "")
//                    sharedprefs.setStudentClass(context, "")
//                    mFragment = AttendanceFragment()
//                    replaceFragmentsSelected(position)
//                }
                else if (position == 9) {
                    sharedprefs.setStudentID(context, "")
                    sharedprefs.setStudentName(context, "")
                    sharedprefs.setStudentPhoto(context, "")
                    sharedprefs.setStudentClass(context, "")
                    mFragment = TimeTableFragment()
                    replaceFragmentsSelected(position)
                } else if (position == 10) {
                    mFragment = TermDatesFragment()
                    replaceFragmentsSelected(position)
                }
                else if (position == 11) {
                    sharedprefs.setStudentID(context, "")
                    sharedprefs.setStudentName(context, "")
                    sharedprefs.setStudentPhoto(context, "")
                    sharedprefs.setStudentClass(context, "")
                    mFragment = CurriculumFragment()
                    replaceFragmentsSelected(position)
                }
                /*else if (position == 12) {
                    if (drawer_layout.isDrawerOpen(linear_layout)) {
                        drawer_layout.closeDrawer(linear_layout)
                    }
                    val li: LayoutInflater = layoutInflater
                    val layout: View = li.inflate(
                        R.layout.homecustom_progress,
                        findViewById<View>(R.id.customlayout) as? ViewGroup
                    )

                    val toast = Toast(applicationContext)
                    toast.duration = Toast.LENGTH_SHORT
                    toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0)
                    toast.view = layout

                    toast.show()


                    if (sharedprefs.getDataCollection(context)==1)
                    {
                        if(sharedprefs.getDataCollectionShown(context)==0)
                        {
                            sharedprefs.setSuspendTrigger(context,"2")
                            sharedprefs.setDataCollectionShown(context,1)
                            callSettingsUserDetail()
                        }


                    }
                    else{
                        showTriggerDataCollection(context,"Confirm?", "Select one or more areas to update", R.drawable.questionmark_icon, R.drawable.round)

                    }
                } */
                else if (position == 12) {
                    if (ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            context,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        checkpermission()


                    } else {
                        mFragment = ContactUsFragment()
                        replaceFragmentsSelected(position)
                    }

                } else if (position == 13) {
                    sharedprefs.setStudentID(context, "")
                    sharedprefs.setStudentName(context, "")
                    sharedprefs.setStudentPhoto(context, "")
                    sharedprefs.setStudentClass(context, "")
                    mFragment = AppsFragment()
                    replaceFragmentsSelected(position)
                }
            }

        }

        supportActionBar!!.displayOptions = ActionBar.DISPLAY_SHOW_CUSTOM
        supportActionBar!!.setDisplayShowCustomEnabled(true)
        supportActionBar!!.setCustomView(R.layout.custom_titlebar)
        supportActionBar!!.elevation = 0F

        var view = supportActionBar!!.customView
        toolbar = view.parent as Toolbar
        toolbar.setBackgroundColor(resources.getColor(R.color.white))
        toolbar.setContentInsetsAbsolute(0, 0)

        navigation_menu = view.findViewById(R.id.action_bar_back)
        settings_icon = view.findViewById(R.id.action_bar_forward)
        logoClickImgView = view.findViewById(R.id.logoClickImgView)
        settings_icon.visibility = View.VISIBLE
        homelist.setBackgroundColor(getColor(R.color.split_bg))
        homelist.setOnScrollListener(object : AbsListView.OnScrollListener {
            override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
            override fun onScroll(
                view: AbsListView,
                firstVisibleItem: Int,
                visibleItemCount: Int,
                totalItemCount: Int
            ) {
                if (view.id == homelist.id) {
                    val currentFirstVisibleItem: Int = homelist.lastVisiblePosition

                    if (currentFirstVisibleItem == totalItemCount - 1) {
                        downarrow.visibility = View.INVISIBLE
                    } else {
                        downarrow.visibility = View.VISIBLE
                    }
                }
            }
        })
        mListItemArray = context.resources.getStringArray(R.array.navigation_items_guest)
        mListImgArray = context.resources.obtainTypedArray(R.array.navigation_icons_guest)
        navigation_menu.setOnClickListener {
            if (drawer_layout.isDrawerOpen(linear_layout)) {
                drawer_layout.closeDrawer(linear_layout)
            } else {
                drawer_layout.openDrawer(linear_layout)
            }
        }

        logoClickImgView.setOnClickListener(View.OnClickListener {
            settings_icon.visibility = View.VISIBLE
            if (drawer_layout.isDrawerOpen(linear_layout)) {
                drawer_layout.closeDrawer(linear_layout)
            }
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            mFragment = HomescreenFragment()
            supportFragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE)
        })

        settings_icon.setOnClickListener {
            val fm = supportFragmentManager
            val currentFragment =
                fm.findFragmentById(R.id.fragment_holder)
            if (drawer_layout.isDrawerOpen(linear_layout)) {
                drawer_layout.closeDrawer(linear_layout)
            }
            mFragment = SettingsFragment()
            if (mFragment != null) {
                val fragmentManager =
                    supportFragmentManager
                fragmentManager.beginTransaction()
                    .add(R.id.fragment_holder, mFragment!!, "Settings")
                    .addToBackStack("Settings").commit()

                supportActionBar!!.setTitle(R.string.null_value)
                settings_icon.visibility = View.INVISIBLE

            }
        }

    }


    override fun onItemLongClick(
        parent: AdapterView<*>?,
        view: View?,
        position: Int,
        id: Long
    ): Boolean {

        shadowBuilder = MyDragShadowBuilder(view)
        sPosition = position
        val selecteditem = parent?.getItemIdAtPosition(position)
        view?.setBackgroundColor(Color.parseColor("#47C2D1"))
        val data = ClipData.newPlainText("", "")
        view?.startDrag(data, shadowBuilder, view, 0)
        view!!.visibility = View.VISIBLE
        drawer_layout.closeDrawer(linear_layout)
        return false
    }

    private fun replaceFragmentsSelected(position: Int) {
        settings_icon.visibility = View.VISIBLE
        if (mFragment != null) {
            val fragmentManager = supportFragmentManager
            fragmentManager
                .beginTransaction()
                .replace(
                    R.id.fragment_holder, mFragment!!,
                    mListItemArray[position]
                )
                .addToBackStack(mListItemArray[position]).commitAllowingStateLoss()
            homelist.setItemChecked(position, true)
            homelist.setSelection(position)
            supportActionBar!!.setTitle(R.string.null_value)
            if (drawer_layout.isDrawerOpen(linear_layout)) {
                drawer_layout.closeDrawer(linear_layout)
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (drawer_layout.isDrawerOpen(linear_layout)) {
            drawer_layout.closeDrawer(linear_layout)
        }
        settings_icon.visibility = View.VISIBLE

    }

    fun fragmentIntent(mFragment: Fragment?) {
        if (mFragment != null) {
            val fragmentManager = supportFragmentManager
            fragmentManager.beginTransaction()
                .add(R.id.fragment_holder, mFragment, appController.mTitles)
                .addToBackStack(appController.mTitles).commitAllowingStateLoss() //commit
        }
    }


    private fun checkpermission() {
        if (ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CALL_PHONE
                ),
                123
            )
        }
    }

    fun showSuccessAlert(context: Context, message: String, msgHead: String) {
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
        btn_Ok.setOnClickListener()
        {
            dialog.dismiss()

        }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        Intent.FLAG_ACTIVITY_CLEAR_TASK

    }
    fun callSettingsUserDetail() {
        val bannerModel = BannerModel("1.0.0", 2)
        val token = sharedprefs.getaccesstoken(context)
        val call: Call<ResponseBody> =
            ApiClient.getClient.settingsUserDetail(bannerModel, "Bearer " + token)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                val bannerresponse = response.body()
                if (bannerresponse != null) {
                    try {
                        val jsonObject = JSONObject(bannerresponse.string())
                        if (jsonObject.has(jsonConstans.STATUS)) {
                            val status: Int = jsonObject.optInt(jsonConstans.STATUS)
                            if (status == 100) {
                                val responseObj = jsonObject.getJSONObject("responseArray")
                                val appVersion = responseObj.optString("android_app_version")
                                val data_collection = responseObj.optInt("data_collection")
                                val trigger_type = responseObj.optInt("trigger_type")
                                val already_triggered = responseObj.optInt("already_triggered")
                                sharedprefs.setAppVersion(context, appVersion)
                                sharedprefs.setDataCollection(context, data_collection)
                                sharedprefs.setTriggerType(context, trigger_type)
                                sharedprefs.setAlreadyTriggered(context, already_triggered)

                                if (sharedprefs.getDataCollection(context) == 1) {

                                    if (sharedprefs.getAlreadyTriggered(context) == 0) {
                                        callDataCollectionAPI()

                                    } else {
                                        if (previousTriggerTypeNew == sharedprefs.getTriggerType(
                                                context
                                            )
                                        ) {
                                            if (!sharedprefs.getSuspendTrigger(context)
                                                    .equals("1")
                                            ) {
                                                val intent = Intent(
                                                    context,
                                                    DataCollectionActivity::class.java
                                                )
                                                context.startActivity(intent)
                                            }

                                        } else {
                                            callDataCollectionAPI()
                                        }
                                    }

                                }

                            } else {
                                if (status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(context)
                                    callSettingsUserDetail()

                                } else {
                                    if (status == 103) {
                                        //validation check error

                                    } else {
                                        //check status code checks
                                        InternetCheckClass.checkApiStatusError(status, context)
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
    fun showTriggerDataCollection(context: Context,msgHead:String,msg:String,ico:Int,bgIcon:Int)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_trigger_data_collection)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var checkRecycler = dialog.findViewById(R.id.checkRecycler) as RecyclerView
        var linearLayoutManagerM : LinearLayoutManager = LinearLayoutManager(context)
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
            var model= TriggerDataModel()
            model.categoryName=categoryList.get(i)
            model.checkedCategory=false
            mTriggerModelArrayList.add(model)

        }
        var triggerAdapter= TriggerAdapter(mTriggerModelArrayList)
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

                var triggerAdapter= TriggerAdapter(mTriggerModelArrayList)
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
                    context,
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

    fun callDataTriggerApi(value:String,triggerDialog:Dialog,progress:ProgressBar)
    {
        val token = sharedprefs.getaccesstoken(context)
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
                                    AccessTokenClass.getAccessToken(context)
                                    callDataTriggerApi(value,triggerDialog,progress)
                                } else {
                                    if (status == 103) {
                                        //validation check error
                                    } else {
                                        //check status code checks
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

    fun callDataCollectionAPI() {
        ownContactArrayList = ArrayList()
        kinDetailArrayList = ArrayList()
        passportArrayList = ArrayList()
        healthDetailArrayList = ArrayList()
        ownContactDetailSaveArrayList = ArrayList()
        passportSaveArrayList = ArrayList()
        healthSaveArrayList = ArrayList()
        kinDetailSaveArrayList = ArrayList()
        val token = sharedprefs.getaccesstoken(context)
        val call: Call<DataCollectionModel> =
            ApiClient.getClient.dataCollectionDetail("Bearer " + token)
        call.enqueue(object : Callback<DataCollectionModel> {
            override fun onFailure(call: Call<DataCollectionModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<DataCollectionModel>,
                response: Response<DataCollectionModel>
            ) {
                if (response.body()!!.status == 100) {

                    sharedprefs.setDisplayMessage(
                        context,
                        response.body()!!.responseArray.display_message
                    )
                    ownContactArrayList = response.body()!!.responseArray.ownDetailsList
                    kinDetailArrayList = response.body()!!.responseArray.kinDetailsList
                    passportArrayList = response.body()!!.responseArray.passportDetailsList
                    healthDetailArrayList = response.body()!!.responseArray.healthInsurenceList
                    if (ownContactArrayList.size > 0) {
                        for (i in 0..ownContactArrayList.size - 1) {
                            var model = OwnContactModel()
                            model.id = ownContactArrayList.get(i).id
                            model.user_id = ownContactArrayList.get(i).user_id
                            model.title = ownContactArrayList.get(i).title
                            model.name = ownContactArrayList.get(i).name
                            model.last_name = ownContactArrayList.get(i).last_name
                            model.relationship = ownContactArrayList.get(i).relationship
                            model.email = ownContactArrayList.get(i).email
                            model.phone = ownContactArrayList.get(i).phone
                            model.code = ownContactArrayList.get(i).code
                            model.user_mobile = ownContactArrayList.get(i).user_mobile
                            model.address1 = ownContactArrayList.get(i).address1
                            model.address2 = ownContactArrayList.get(i).address2
                            model.address3 = ownContactArrayList.get(i).address3
                            model.town = ownContactArrayList.get(i).town
                            model.state = ownContactArrayList.get(i).state
                            model.country = ownContactArrayList.get(i).country
                            model.pincode = ownContactArrayList.get(i).pincode
                            model.status = ownContactArrayList.get(i).status
                            model.created_at = ownContactArrayList.get(i).created_at
                            model.updated_at = ownContactArrayList.get(i).updated_at
                            model.isUpdated = false
                            model.isConfirmed = false
                            ownContactDetailSaveArrayList.add(model)

                        }

                        if (sharedprefs.getOwnContactDetailArrayList(context) == null || sharedprefs.getOwnContactDetailArrayList(
                                context
                            )!!.size == 0
                        ) {
                            sharedprefs.setIsAlreadyEnteredOwn(context, true)
                            sharedprefs.setOwnContactDetailArrayList(
                                context,
                                ownContactDetailSaveArrayList
                            )
                        } else {
                            if (!sharedprefs.getIsAlreadyEnteredOwn(context)) {
                                sharedprefs.setIsAlreadyEnteredOwn(context, true)
                               sharedprefs.setOwnContactDetailArrayList(
                                   context,
                                    ownContactDetailSaveArrayList
                                )
                            }
                        }
                    }

                    if (passportArrayList.size > 0) {
                        for (i in 0..passportArrayList.size - 1) {
                            var mModel = PassportApiModel()
                            mModel.id = passportArrayList.get(i).id
                            mModel.student_unique_id = passportArrayList.get(i).student_unique_id
                            mModel.student_id = passportArrayList.get(i).student_id
                            mModel.student_name = passportArrayList.get(i).student_name
                            mModel.passport_number = passportArrayList.get(i).passport_number
                            mModel.nationality = passportArrayList.get(i).nationality
                            mModel.passport_image = passportArrayList.get(i).passport_image
                            mModel.date_of_issue = passportArrayList.get(i).date_of_issue
                            mModel.expiry_date = passportArrayList.get(i).expiry_date
                            mModel.passport_expired = passportArrayList.get(i).passport_expired
                            mModel.emirates_id_no = passportArrayList.get(i).emirates_id_no
                            mModel.emirates_id_image = passportArrayList.get(i).emirates_id_image
                            mModel.passport_image_name = ""
                            mModel.emirates_id_image_path = ""
                            mModel.emirates_id_image_name = ""
                            mModel.emirates_id_image_path = ""
                            mModel.status = passportArrayList.get(i).status
                            mModel.request = passportArrayList.get(i).request
                            mModel.created_at = passportArrayList.get(i).created_at
                            mModel.updated_at = passportArrayList.get(i).updated_at
                            mModel.is_date_changed = false
                            passportSaveArrayList.add(mModel)
                        }
                        if (sharedprefs.getPassportDetailArrayList(context) == null || sharedprefs.getPassportDetailArrayList(
                                context
                            )!!.size == 0
                        ) {
                            sharedprefs.setPassportDetailArrayList(context, passportSaveArrayList)
                        }

                    }

                    if (healthDetailArrayList.size > 0) {
                        for (i in 0..healthDetailArrayList.size - 1) {
                            var hModel = HealthInsuranceDetailAPIModel()
                            hModel.id = healthDetailArrayList.get(i).id
                            hModel.student_unique_id =
                                healthDetailArrayList.get(i).student_unique_id
                            hModel.student_id = healthDetailArrayList.get(i).student_id
                            hModel.student_name = healthDetailArrayList.get(i).student_name
                            hModel.health_detail = healthDetailArrayList.get(i).health_detail
                            hModel.health_form_link = healthDetailArrayList.get(i).health_form_link
                            hModel.status = healthDetailArrayList.get(i).status
                            hModel.request = healthDetailArrayList.get(i).request
                            hModel.created_at = healthDetailArrayList.get(i).created_at
                            hModel.updated_at = healthDetailArrayList.get(i).updated_at
                            healthSaveArrayList.add(hModel)

                        }
                        if (sharedprefs.getHealthDetailArrayList(context) == null || sharedprefs.getHealthDetailArrayList(
                                context
                            )!!.size == 0
                        ) {
                            sharedprefs.setHealthDetailArrayList(context, healthSaveArrayList)
                        }
                    }
                    if (kinDetailArrayList.size > 0) {
                        for (i in 0..kinDetailArrayList.size - 1) {
                            var mModel = KinDetailApiModel()
                            mModel.id = kinDetailArrayList.get(i).id
                            mModel.user_id = kinDetailArrayList.get(i).user_id
                            mModel.kin_id = kinDetailArrayList.get(i).kin_id
                            mModel.title = kinDetailArrayList.get(i).title
                            mModel.name = kinDetailArrayList.get(i).name
                            mModel.last_name = kinDetailArrayList.get(i).last_name
                            mModel.relationship = kinDetailArrayList.get(i).relationship
                            mModel.email = kinDetailArrayList.get(i).email
                            mModel.phone = kinDetailArrayList.get(i).phone
                            mModel.code = kinDetailArrayList.get(i).code
                            mModel.user_mobile = kinDetailArrayList.get(i).user_mobile
                            mModel.status = kinDetailArrayList.get(i).status
                            mModel.request = kinDetailArrayList.get(i).request
                            mModel.created_at = kinDetailArrayList.get(i).created_at
                            mModel.updated_at = kinDetailArrayList.get(i).updated_at
                            mModel.NewData = false
                            mModel.Newdata = "NO"
                            mModel.isConfirmed = false
                            kinDetailSaveArrayList.add(mModel)

                        }
                        if (sharedprefs.getKinDetailArrayList(context) == null || sharedprefs.getKinDetailArrayList(
                                context
                            )!!.size == 0
                        ) {
                            Log.e("DATA COLLECTION", "ENTERS2")
                            sharedprefs.setIsAlreadyEnteredKin(context, true)
                            sharedprefs.setKinDetailArrayList(context, kinDetailSaveArrayList)
                            sharedprefs.setKinDetailPassArrayList(context, kinDetailSaveArrayList)
                        } else {
                            Log.e("DATA COLLECTION", "ENTERS3")
                            if (!sharedprefs.getIsAlreadyEnteredKin(context)) {
                                Log.e("DATA COLLECTION", "ENTERS4")
                                sharedprefs.setIsAlreadyEnteredKin(context, true)
                                sharedprefs.setKinDetailArrayList(context, kinDetailSaveArrayList)
                                sharedprefs.setKinDetailPassArrayList(
                                    context,
                                    kinDetailSaveArrayList
                                )
                            }
                        }
                    }

                    //Intent
                    Log.e("DATA COLLECTION", "ENTERS5")
                    if (!sharedprefs.getSuspendTrigger(context).equals("1")) {
                        val intent = Intent(context, DataCollectionActivity::class.java)
                        context.startActivity(intent)
                    }

                } else {
                    if (response.body()!!.status == 116) {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(context)
                        callDataCollectionAPI()

                    } else {
                        if (response.body()!!.status == 103) {
                            //validation check error

                        } else {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(
                                response.body()!!.status,
                                context
                            )
                        }
                    }

                }
            }

        })
    }
}
