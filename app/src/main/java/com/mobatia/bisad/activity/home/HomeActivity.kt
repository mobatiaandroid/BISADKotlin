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
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.*
import android.widget.AdapterView.OnItemLongClickListener
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.common.LoginActivity
import com.mobatia.bisad.activity.home.adapter.HomeListAdapter
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.apps.AppsFragment
import com.mobatia.bisad.fragment.attendance.AttendanceFragment
import com.mobatia.bisad.fragment.calendar.CalendarFragment
import com.mobatia.bisad.fragment.calendar_new.CalendarFragmentNew
import com.mobatia.bisad.fragment.communication.CommunicationFragment
import com.mobatia.bisad.fragment.contact_us.ContactUsFragment
import com.mobatia.bisad.fragment.curriculum.CurriculumFragment
import com.mobatia.bisad.fragment.home.HomescreenFragment
import com.mobatia.bisad.fragment.home.appController
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.messages.MessageFragment
import com.mobatia.bisad.fragment.report_absence.ReportAbsenceFragment
import com.mobatia.bisad.fragment.reports.ReportsFragment
import com.mobatia.bisad.fragment.settings.SettingsFragment
import com.mobatia.bisad.fragment.socialmedia.SocialMediaFragment
import com.mobatia.bisad.fragment.student_information.StudentInformationFragment
import com.mobatia.bisad.fragment.teacher_contact.TeacherContactFragment
import com.mobatia.bisad.fragment.termdates.TermDatesFragment
import com.mobatia.bisad.fragment.time_table.TimeTableFragment
import com.mobatia.bisad.manager.MyDragShadowBuilder
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessModel


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
    var mFragment: Fragment? = null
    var sPosition: Int = 0


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        );
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

    @SuppressLint("Recycle")
    @RequiresApi(Build.VERSION_CODES.M)
    private fun initializeUI() {

        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()
        context = this
        homelist = findViewById<ListView>(R.id.homelistview)
        drawer_layout = findViewById(R.id.drawer_layout)
        linear_layout = findViewById(R.id.linear_layout)
        var downarrow = findViewById<ImageView>(R.id.downarrow)

        mListItemArray =
            applicationContext.resources.getStringArray(R.array.navigation_items_guest)
        mListImgArray =
            applicationContext.resources.obtainTypedArray(R.array.navigation_icons_guest)


        val width = (resources.displayMetrics.widthPixels / 2).toInt()
        val params = linear_layout
            .getLayoutParams() as DrawerLayout.LayoutParams
        params.width = width
        linear_layout.setLayoutParams(params)
        val myListAdapter = HomeListAdapter(this, mListItemArray, mListImgArray!!)
        homelist.adapter = myListAdapter
        homelist.onItemLongClickListener = this


        homelist.setOnItemClickListener() { adapterView, view, position, id ->
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
                } else if (position == 9) {
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
                } else if (position == 12) {
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
                } else if (position == 9) {
                    sharedprefs.setStudentID(context, "")
                    sharedprefs.setStudentName(context, "")
                    sharedprefs.setStudentPhoto(context, "")
                    sharedprefs.setStudentClass(context, "")
                    mFragment = AttendanceFragment()
                    replaceFragmentsSelected(position)
                } else if (position == 10) {
                    sharedprefs.setStudentID(context, "")
                    sharedprefs.setStudentName(context, "")
                    sharedprefs.setStudentPhoto(context, "")
                    sharedprefs.setStudentClass(context, "")
                    mFragment = TimeTableFragment()
                    replaceFragmentsSelected(position)
                } else if (position == 11) {
                    mFragment = TermDatesFragment()
                    replaceFragmentsSelected(position)
                } else if (position == 12) {
                    sharedprefs.setStudentID(context, "")
                    sharedprefs.setStudentName(context, "")
                    sharedprefs.setStudentPhoto(context, "")
                    sharedprefs.setStudentClass(context, "")
                    mFragment = CurriculumFragment()
                    replaceFragmentsSelected(position)
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
                if (view.id == homelist.getId()) {
                    val currentFirstVisibleItem: Int = homelist.getLastVisiblePosition()

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
                settings_icon.setVisibility(View.GONE)

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
            val fragmentManager = getSupportFragmentManager()
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
        btn_Ok?.setOnClickListener()
        {
            dialog.dismiss()

        }
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        Intent.FLAG_ACTIVITY_CLEAR_TASK

    }

    override fun onRestart() {
        super.onRestart()

    }
}
