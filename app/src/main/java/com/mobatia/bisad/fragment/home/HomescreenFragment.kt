package com.mobatia.bisad.fragment.home

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.TypedArray
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.*
import android.widget.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.mobatia.bisad.BuildConfig
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.DataCollectionActivity
import com.mobatia.bisad.activity.home.HomeActivity
import com.mobatia.bisad.activity.home.PageView
import com.mobatia.bisad.activity.home.model.HealthInsuranceDetailAPIModel
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.constants.NaisClassNameConstants
import com.mobatia.bisad.constants.NaisTabConstants
import com.mobatia.bisad.fragment.apps.AppsFragment
import com.mobatia.bisad.fragment.attendance.AttendanceFragment
import com.mobatia.bisad.fragment.calendar_new.CalendarFragmentNew
import com.mobatia.bisad.fragment.communication.CommunicationFragment
import com.mobatia.bisad.fragment.contact_us.ContactUsFragment
import com.mobatia.bisad.fragment.curriculum.CurriculumFragment
import com.mobatia.bisad.fragment.home.model.*
import com.mobatia.bisad.fragment.home.model.datacollection.*
import com.mobatia.bisad.fragment.messages.MessageFragment
import com.mobatia.bisad.fragment.report_absence.ReportAbsenceFragment
import com.mobatia.bisad.fragment.reports.ReportsFragment
import com.mobatia.bisad.fragment.settings.adapter.TriggerAdapter
import com.mobatia.bisad.fragment.settings.model.TriggerDataModel
import com.mobatia.bisad.fragment.settings.model.TriggerUSer
import com.mobatia.bisad.fragment.socialmedia.SocialMediaFragment
import com.mobatia.bisad.fragment.student_information.StudentInformationFragment
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
import com.mobatia.bisad.fragment.teacher_contact.TeacherContactFragment
import com.mobatia.bisad.fragment.termdates.TermDatesFragment
import com.mobatia.bisad.fragment.time_table.TimeTableFragment
import com.mobatia.bisad.manager.AppController
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
import java.util.*
import kotlin.collections.ArrayList


lateinit var relone: RelativeLayout
lateinit var reltwo: RelativeLayout
lateinit var relthree: RelativeLayout
lateinit var relfour: RelativeLayout
lateinit var relfive: RelativeLayout
lateinit var relsix: RelativeLayout
lateinit var relseven: RelativeLayout
lateinit var releight: RelativeLayout
lateinit var relnine: RelativeLayout
lateinit var reltxtnine: TextView

lateinit var relTxtone: TextView
lateinit var relTxttwo: TextView
lateinit var relTxtfive: TextView
lateinit var relTxtsix: TextView
lateinit var relTxtseven: TextView
lateinit var relTxteight: TextView
lateinit var relTxtthree: TextView
lateinit var relTxtfour: TextView

lateinit var relImgone: ImageView
lateinit var relImgtwo: ImageView
lateinit var relImgthree: ImageView
lateinit var relImgfour: ImageView
lateinit var relImgfive: ImageView
lateinit var relImgsix: ImageView
lateinit var relImgseven: ImageView
lateinit var relImgeight: ImageView
lateinit var relImgnine: ImageView
var versionfromapi: String = ""
var currentversion: String = ""


lateinit var mSectionText: Array<String?>
lateinit var homeActivity: HomeActivity
lateinit var appController: AppController
lateinit var listitems: Array<String>
lateinit var studentListArrayList: ArrayList<StudentList>
lateinit var titlesListArrayList: ArrayList<TitlesArrayList>
lateinit var countryistArrayList: ArrayList<CountryiesDetailModel>
lateinit var relationshipArrayList: ArrayList<RelationShipDetailModel>
lateinit var ownContactArrayList: ArrayList<OwnDetailsModel>
lateinit var kinDetailArrayList: ArrayList<KinDetailsModel>
lateinit var passportArrayList: ArrayList<PassportDetailModel>
lateinit var healthDetailArrayList: ArrayList<HealthInsuranceDetailModel>
lateinit var ownContactDetailSaveArrayList: ArrayList<OwnContactModel>
lateinit var kinDetailSaveArrayList: ArrayList<KinDetailApiModel>
lateinit var passportSaveArrayList: ArrayList<PassportApiModel>
lateinit var healthSaveArrayList: ArrayList<HealthInsuranceDetailAPIModel>
lateinit var mListImgArrays: TypedArray
lateinit var TouchedView: View

lateinit var classNameConstants: NaisClassNameConstants

//lateinit var TAB_ID: String
private var TAB_ID: String = ""
private var CLICKED: String = ""
private var INTENT_TAB_ID: String = ""
private var TABIDfragment: String = ""
private var usertype: String = ""
private var USERDATA: String = ""
private var previousTriggerType: Int = 0


lateinit var naisTabConstants: NaisTabConstants
lateinit var sharedprefs: PreferenceData

lateinit var pager: ViewPager
var isDraggable: Boolean = false
lateinit var mContext: Context


@Suppress(
    "NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS", "CAST_NEVER_SUCCEEDS",
    "ControlFlowWithEmptyBody"
)
class HomescreenFragment : Fragment(), View.OnClickListener {


    var currentPage: Int = 0
    lateinit var jsonConstans: JsonConstants

    var bannerarray = ArrayList<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.fragment_homescreen, container, false)
    }

    @SuppressLint("UseRequireInsteadOfGet", "Recycle")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bannerarray = ArrayList()
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        homeActivity = activity as HomeActivity
        appController = AppController()
        CLICKED = homeActivity.sPosition.toString()
        naisTabConstants = NaisTabConstants()
        classNameConstants = NaisClassNameConstants()
        listitems = resources.getStringArray(R.array.navigation_items_guest)
        mListImgArrays = context!!.resources.obtainTypedArray(R.array.navigation_icons_guest)
        previousTriggerType = sharedprefs.getTriggerType(mContext)
        currentversion = BuildConfig.VERSION_NAME
        initializeUI()
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck) {
            getbannerimages()

        } else {
            InternetCheckClass.showSuccessInternetAlert(mContext)
        }
        if (sharedprefs.getUserCode(mContext).equals("")) {

        } else {
            if (internetCheck) {
                callStudentListApi()
                callTilesListApi()
                callCountryListApi()
                callRelationshipApi()
                callSettingsUserDetail()
            } else {
                InternetCheckClass.showSuccessInternetAlert(mContext)
            }

        }

        setListeners()
        setdraglisteners()
        getButtonBgAndTextImages()

    }

    private fun setListeners() {
        relone.setOnClickListener(this)
        reltwo.setOnClickListener(this)
        relthree.setOnClickListener(this)
        relfour.setOnClickListener(this)
        relfive.setOnClickListener(this)
        relsix.setOnClickListener(this)
        relseven.setOnClickListener(this)
        releight.setOnClickListener(this)
        relnine.setOnClickListener(this)

    }

    override fun onClick(v: View?) {
        if (v == relone) {

            INTENT_TAB_ID = sharedprefs.getbuttononetabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == reltwo) {

            INTENT_TAB_ID = sharedprefs.getbuttontwotabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == relthree) {

            INTENT_TAB_ID = sharedprefs.getbuttonthreetabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == relfour) {

            INTENT_TAB_ID = sharedprefs.getbuttonfourtabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == relfive) {

            INTENT_TAB_ID = sharedprefs.getbuttonfivetabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == relsix) {

            INTENT_TAB_ID = sharedprefs.getbuttonsixtabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == relseven) {

            INTENT_TAB_ID = sharedprefs.getbuttonseventabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == releight) {

            INTENT_TAB_ID = sharedprefs.getbuttoneighttabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }
        if (v == relnine) {

            INTENT_TAB_ID = sharedprefs.getbuttonninetabid(mContext).toString()
            CHECKINTENTVALUE(INTENT_TAB_ID)
        }

    }

    private fun getButtonBgAndTextImages() {

        if (sharedprefs
                .getbuttononetextimage(mContext)!!.toInt() != 0
        ) {
            relImgone.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttononetextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr =
                if (listitems[sharedprefs
                        .getbuttononetextimage(mContext)!!.toInt()].equals(
                        classNameConstants.CCAS,
                        ignoreCase = true
                    )
                ) {
                    classNameConstants.CCAS
                } else if (listitems[sharedprefs
                        .getbuttononetextimage(mContext)!!.toInt()].equals(
                        classNameConstants.STUDENT_INFORMATION,
                        ignoreCase = true
                    )
                ) {
                    classNameConstants.STUDENT
                } else {
                    listitems[sharedprefs
                        .getbuttononetextimage(mContext)!!.toInt()].toUpperCase()
                }
            relTxtone.text = relTwoStr
            relTxtone.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            relone.setBackgroundColor(
                sharedprefs
                    .getButtonOneGuestBg(mContext)
            )
        }
        if (sharedprefs.getbuttontwotextimage(mContext)!!.toInt() != 0
        ) {
            relImgtwo.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttontwotextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr =
                if (listitems[sharedprefs
                        .getbuttontwotextimage(mContext)!!.toInt()].equals(
                        classNameConstants.CCAS,
                        ignoreCase = true
                    )
                ) {
                    classNameConstants.CCAS
                } else if (listitems[sharedprefs
                        .getbuttontwotextimage(mContext)!!.toInt()].equals(
                        classNameConstants.STUDENT_INFORMATION,
                        ignoreCase = true
                    )
                ) {
                    classNameConstants.STUDENT
                } else {
                    listitems[sharedprefs
                        .getbuttontwotextimage(mContext)!!.toInt()].toUpperCase()
                }
            relTxttwo.text = relTwoStr
            relTxttwo.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            reltwo.setBackgroundColor(
                sharedprefs
                    .getButtontwoGuestBg(mContext)
            )
        }
        if (sharedprefs
                .getbuttonthreetextimage(mContext)!!.toInt() != 0
        ) {
            relImgthree.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttonthreetextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr = if (listitems[sharedprefs
                    .getbuttonthreetextimage(mContext)!!.toInt()].equals(
                    classNameConstants.CCAS,
                    ignoreCase = true
                )
            ) {
                classNameConstants.CCAS
            } else if (listitems[sharedprefs
                    .getbuttonthreetextimage(mContext)!!.toInt()].equals(
                    classNameConstants.STUDENT_INFORMATION,
                    ignoreCase = true
                )
            ) {
                classNameConstants.STUDENT
            } else {
                listitems[sharedprefs
                    .getbuttonthreetextimage(mContext)!!.toInt()].toUpperCase()
            }
            relTxtthree.text = relTwoStr
            relTxtthree.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            relthree.setBackgroundColor(
                sharedprefs
                    .getButtonthreeGuestBg(mContext)
            )
        }


        if (sharedprefs
                .getbuttonfourtextimage(mContext)!!.toInt() != 0
        ) {
            relImgfour.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttonfourtextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr = if (listitems[sharedprefs
                    .getbuttonfourtextimage(mContext)!!.toInt()].equals(
                    classNameConstants.CCAS,
                    ignoreCase = true
                )
            ) {
                classNameConstants.CCAS
            } else if (listitems[sharedprefs
                    .getbuttonfourtextimage(mContext)!!.toInt()].equals(
                    classNameConstants.STUDENT_INFORMATION,
                    ignoreCase = true
                )
            ) {
                classNameConstants.STUDENT
            } else {
                listitems[sharedprefs
                    .getbuttonfourtextimage(mContext)!!.toInt()].toUpperCase()
            }
            relTxtfour.text = relTwoStr
            relTxtfour.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            relfour.setBackgroundColor(
                sharedprefs
                    .getButtonfourGuestBg(mContext)
            )
        }


        if (sharedprefs
                .getbuttonfivetextimage(mContext)!!.toInt() != 0
        ) {
            relImgfive.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttonfivetextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr = if (listitems[sharedprefs
                    .getbuttonfivetextimage(mContext)!!.toInt()].equals(
                    classNameConstants.CCAS,
                    ignoreCase = true
                )
            ) {
                classNameConstants.CCAS
            } else if (listitems[sharedprefs
                    .getbuttonfivetextimage(mContext)!!.toInt()].equals(
                    classNameConstants.STUDENT_INFORMATION,
                    ignoreCase = true
                )
            ) {
                classNameConstants.STUDENT
            } else {
                listitems[sharedprefs
                    .getbuttonfivetextimage(mContext)!!.toInt()].toUpperCase()
            }
            relTxtfive.text = relTwoStr
            relTxtfive.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            relfive.setBackgroundColor(
                sharedprefs
                    .getButtonfiveGuestBg(mContext)
            )
        }
        if (sharedprefs.getbuttonsixtextimage(mContext)!!.toInt() != 0) {
            relImgsix.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttonsixtextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr = if (listitems[sharedprefs
                    .getbuttonsixtextimage(mContext)!!.toInt()].equals(
                    classNameConstants.CCAS,
                    ignoreCase = true
                )
            ) {
                classNameConstants.CCAS
            } else if (listitems[sharedprefs
                    .getbuttonsixtextimage(mContext)!!.toInt()].equals(
                    classNameConstants.STUDENT_INFORMATION,
                    ignoreCase = true
                )
            ) {
                classNameConstants.STUDENT
            } else {
                listitems[sharedprefs
                    .getbuttonsixtextimage(mContext)!!.toInt()].toUpperCase(Locale.ROOT)
            }
            relTxtsix.text = relTwoStr
            relTxtsix.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            relsix.setBackgroundColor(
                sharedprefs
                    .getButtonsixGuestBg(mContext)
            )
        }
        if (sharedprefs
                .getbuttonseventextimage(mContext)!!.toInt() != 0
        ) {
            relImgseven.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttonseventextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr = if (listitems[sharedprefs
                    .getbuttonseventextimage(mContext)!!.toInt()].equals(
                    classNameConstants.CCAS,
                    ignoreCase = true
                )
            ) {
                classNameConstants.CCAS
            } else {
                listitems[sharedprefs
                    .getbuttonseventextimage(mContext)!!.toInt()].toUpperCase()
            }
            relTxtseven.text = relTwoStr
            relTxtseven.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            relseven.setBackgroundColor(
                sharedprefs
                    .getButtonsevenGuestBg(mContext)
            )
        }
        if (sharedprefs
                .getbuttoneighttextimage(mContext)!!.toInt() != 0
        ) {
            relImgeight.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttoneighttextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr = if (listitems[sharedprefs
                    .getbuttoneighttextimage(mContext)!!.toInt()].equals(
                    classNameConstants.CCAS,
                    ignoreCase = true
                )
            ) {
                classNameConstants.CCAS
            } else {
                listitems[sharedprefs
                    .getbuttoneighttextimage(mContext)!!.toInt()].toUpperCase()
            }
            relTxteight.text = relTwoStr
            relTxteight.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            releight.setBackgroundColor(
                sharedprefs
                    .getButtoneightGuestBg(mContext)
            )
        }
        if (sharedprefs
                .getbuttonninetextimage(mContext)!!.toInt() != 0
        ) {
            relImgnine.setImageDrawable(
                mListImgArrays.getDrawable(
                    sharedprefs
                        .getbuttonninetextimage(mContext)!!.toInt()
                )
            )
            var relTwoStr: String? = ""
            relTwoStr = if (listitems[sharedprefs
                    .getbuttonninetextimage(mContext)!!.toInt()].equals(
                    classNameConstants.CCAS,
                    ignoreCase = true
                )
            ) {
                classNameConstants.CCAS
            } else {
                listitems[sharedprefs
                    .getbuttonninetextimage(mContext)!!.toInt()].toUpperCase(Locale.ROOT)
            }
            reltxtnine.text = relTwoStr
            reltxtnine.setTextColor(ContextCompat.getColor(mContext, R.color.white))
            relnine.setBackgroundColor(
                sharedprefs
                    .getButtonnineGuestBg(mContext)
            )
        }


    }

    private fun setdraglisteners() {
        relone.setOnDragListener(DropListener())
        reltwo.setOnDragListener(DropListener())
        relthree.setOnDragListener(DropListener())
        relfour.setOnDragListener(DropListener())
        relfive.setOnDragListener(DropListener())
        relsix.setOnDragListener(DropListener())
        relseven.setOnDragListener(DropListener())
        releight.setOnDragListener(DropListener())
        relnine.setOnDragListener(DropListener())
    }

    @Suppress("EqualsBetweenInconvertibleTypes")
    class DropListener : View.OnDragListener {
        override fun onDrag(v: View?, event: DragEvent?): Boolean {

            when (event?.action) {
                DragEvent.ACTION_DRAG_STARTED -> Log.d("DRAG", "START")
                DragEvent.ACTION_DRAG_ENTERED -> Log.d("DRAG", "ENTERED")
                DragEvent.ACTION_DRAG_EXITED -> Log.d("DRAG", "EXITED")
                DragEvent.ACTION_DROP -> {
                    val intArray = IntArray(2)
                    v?.getLocationInWindow(intArray)
                    val x = intArray[0]
                    val y = intArray[1]
                    val sPosition = homeActivity.sPosition
                    getButtonViewTouched(x.toFloat().toInt(), y.toFloat().toInt())
                    mSectionText[0] = relTxtone.text.toString().toUpperCase(Locale.ENGLISH)
                    mSectionText[1] = relTxttwo.text.toString().toUpperCase(Locale.ENGLISH)
                    mSectionText[2] = relTxtthree.text.toString().toUpperCase(Locale.ENGLISH)
                    mSectionText[3] = relTxtfour.text.toString().toUpperCase(Locale.ENGLISH)
                    mSectionText[4] = relTxtfive.text.toString().toUpperCase(Locale.ENGLISH)
                    mSectionText[5] = relTxtsix.text.toString().toUpperCase(Locale.ENGLISH)
                    mSectionText[6] = relTxtseven.text.toString().toUpperCase(Locale.ENGLISH)
                    mSectionText[7] = relTxteight.text.toString().toUpperCase(Locale.ENGLISH)
                    mSectionText[8] = reltxtnine.text.toString().toUpperCase(Locale.ENGLISH)

                    for (i in mSectionText.indices) {
                        isDraggable = true
                        if (mSectionText[i].equals(
                                listitems[homeActivity.sPosition],
                                ignoreCase = true
                            )
                        ) {
                            isDraggable = false
                            break
                        }
                    }
                    if (isDraggable) {
                        getButtonDrawablesAndText(TouchedView, homeActivity.sPosition)

                    } else {

                        Toast.makeText(mContext, "Item Already Exists !!!", Toast.LENGTH_SHORT)
                            .show()
                    }

                }
                DragEvent.ACTION_DRAG_ENDED -> Log.d("DRAG", "END")


            }


            return true

        }

        private fun getButtonDrawablesAndText(touchedView: View, sPosition: Int) {
            if (sPosition != 0) {
                if (touchedView == relone) {
                    relImgone.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].toUpperCase(Locale.getDefault())
                    }
                    relTxtone.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttononetabid(mContext, TAB_ID)
                    //setBackgroundColorForView(appController.listitemArrays[sPosition], relone)
                    setBackgroundColorForView(listitems[sPosition], relone)
                    sharedprefs.setbuttononetextimage(mContext, sPosition.toString())
                } else if (touchedView == reltwo) {
                    relImgtwo.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].toUpperCase(Locale.getDefault())
                    }
                    relTxttwo.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttontwotabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], reltwo)
                    sharedprefs.setbuttontwotextimage(mContext, sPosition.toString())
                } else if (touchedView == relthree) {
                    relImgthree.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].toUpperCase(Locale.getDefault())
                    }
                    relTxtthree.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttonthreetabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], relthree)
                    sharedprefs.setbuttonthreetextimage(mContext, sPosition.toString())
                } else if (touchedView == relfour) {
                    relImgfour.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].toUpperCase(Locale.getDefault())
                    }
                    relTxtfour.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttonfourtabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], relfour)
                    sharedprefs.setbuttonfourtextimage(mContext, sPosition.toString())
                } else if (touchedView == relfive) {
                    relImgfive.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].toUpperCase(Locale.getDefault())
                    }
                    relTxtfive.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttonfivetabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], relfive)
                    sharedprefs.setbuttonfivetextimage(mContext, sPosition.toString())
                } else if (touchedView == relsix) {
                    relImgsix.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].toUpperCase(Locale.getDefault())
                    }
                    relTxtsix.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttonsixtabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], relsix)
                    sharedprefs.setbuttonsixtextimage(mContext, sPosition.toString())
                } else if (touchedView == relseven) {
                    relImgseven.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].toUpperCase(Locale.getDefault())
                    }
                    relTxtseven.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttonseventabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], relseven)
                    sharedprefs.setbuttonseventextimage(mContext, sPosition.toString())
                } else if (touchedView == releight) {
                    relImgeight.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].toUpperCase(Locale.getDefault())
                    }
                    relTxteight.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttoneighttabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], releight)
                    sharedprefs.setbuttoneighttextimage(mContext, sPosition.toString())
                } else if (touchedView == relnine) {
                    relImgnine.setImageDrawable(mListImgArrays.getDrawable(sPosition))
                    val relstring: String
                    if (listitems[sPosition] == "CCAs") {
                        relstring = "CCAS"
                    } else if (listitems[sPosition] == "Student Information") {
                        relstring = "STUDENT INFORMATION"
                    } else {
                        relstring = listitems[sPosition].toUpperCase(Locale.getDefault())
                    }
                    reltxtnine.text = relstring
                    getTabId(listitems[sPosition])
                    sharedprefs.setbuttonninetabid(mContext, TAB_ID)
                    setBackgroundColorForView(listitems[sPosition], relnine)
                    sharedprefs.setbuttonninetextimage(mContext, sPosition.toString())
                }

            }
        }

        private fun setBackgroundColorForView(s: String, v: View) {
            if (v == relone) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == reltwo) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == relthree) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == relfour) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == relfive) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.rel_five))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == relsix) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == relseven) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == releight) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            } else if (v == relnine) {
                v.setBackgroundColor(ContextCompat.getColor(mContext, R.color.transparent))
                saveButtonBgColor(v, ContextCompat.getColor(mContext, R.color.transparent))
            }
        }

        private fun saveButtonBgColor(v: View, color: Int) {
            if (v == relone) {
                sharedprefs.setButtonOneGuestBg(mContext, color)
            } else if (v == reltwo) {
                sharedprefs.setButtontwoGuestBg(mContext, color)
            } else if (v == relthree) {
                sharedprefs.setButtonthreeGuestBg(mContext, color)
            } else if (v == relfour) {
                sharedprefs.setButtonfourGuestBg(mContext, color)
            } else if (v == relfive) {
                sharedprefs.setButtonfiveGuestBg(mContext, color)
            } else if (v == relsix) {
                sharedprefs.setButtonsixGuestBg(mContext, color)
            } else if (v == relseven) {
                sharedprefs.setButtonsevenGuestBg(mContext, color)
            } else if (v == releight) {
                sharedprefs.setButtoneightGuestBg(mContext, color)
            } else if (v == relnine) {
                sharedprefs.setButtonnineGuestBg(mContext, color)
            }

        }

        private fun getTabId(textdata: String) {
            when {

                textdata.equals(classNameConstants.STUDENT_INFORMATION) -> {
                    TAB_ID = naisTabConstants.TAB_STUDENT_INFORMATION

                }
                textdata.equals("Student Information") -> {
                    TAB_ID = naisTabConstants.TAB_STUDENT_INFORMATION

                }

                textdata.equals(classNameConstants.CALENDAR, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_CALENDAR

                }
                textdata.equals(classNameConstants.MESSAGES, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_MESSAGES
                }
                textdata.equals(classNameConstants.COMMUNICATION, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_COMMUNICATION
                }
                textdata.equals(classNameConstants.REPORT_ABSENCE, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_REPORT_ABSENCE
                }
                textdata.equals(classNameConstants.TEACHER_CONTACT, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_TEACHER_CONTACT
                }
                textdata.equals(classNameConstants.SOCIAL_MEDIA, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_SOCIAL_MEDIA
                }
                textdata.equals(classNameConstants.TIME_TABLE, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_TIME_TABLE
                }
                textdata.equals(classNameConstants.TERM_DATES, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_TERM_DATES
                }

                textdata.equals(classNameConstants.CONTACT_US, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_CONTACT_US

                }
                textdata.equals(classNameConstants.APPS, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_APPS

                }
                textdata.equals(classNameConstants.REPORTS, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_REPORTS

                }
//                textdata.equals(classNameConstants.ATTENDANCE, ignoreCase = true) -> {
//                    TAB_ID = naisTabConstants.TAB_ATTENDANCE
//
//                }
                textdata.equals(classNameConstants.CURRICULUM, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_CURRICULUM

                } textdata.equals(classNameConstants.UPDATE_ACCOUNT_DETAILS, ignoreCase = true) -> {
                    TAB_ID = naisTabConstants.TAB_UPDATE

                }

            }

        }

        private fun getButtonViewTouched(centerX: Int, centerY: Int) {
            val array1 = IntArray(2)
            relone.getLocationInWindow(array1)
            val x1: Int = array1[0]
            val x2 = x1 + relone.width
            val y1: Int = array1[1]
            val y2 = y1 + relone.height

            val array2 = IntArray(2)
            reltwo.getLocationInWindow(array2)
            val x3: Int = array2[0]
            val x4 = x3 + reltwo.width
            val y3: Int = array2[1]
            val y4 = y3 + reltwo.height

            val array3 = IntArray(2)
            relthree.getLocationInWindow(array3)
            val x5: Int = array3[0]
            val x6 = x5 + relthree.width
            val y5: Int = array3[1]
            val y6 = y5 + relthree.height

            val array4 = IntArray(2)
            relfour.getLocationInWindow(array4)
            val x7: Int = array4[0]
            val x8 = x7 + relfour.width
            val y7: Int = array4[1]
            val y8 = y7 + relfour.height

            val array5 = IntArray(2)
            relfive.getLocationInWindow(array5)
            val x9: Int = array5[0]
            val x10 = x9 + relfive.width
            val y9: Int = array5[1]
            val y10 = y9 + relfive.height

            val array6 = IntArray(2)
            relsix.getLocationInWindow(array6)
            val x11: Int = array6[0]
            val x12 = x11 + relsix.width
            val y11: Int = array6[1]
            val y12 = y11 + relsix.height

            val array7 = IntArray(2)
            relseven.getLocationInWindow(array7)
            val x13: Int = array7[0]
            val x14 = x13 + relseven.width
            val y13: Int = array7[1]
            val y14 = y13 + relseven.height

            val array8 = IntArray(2)
            releight.getLocationInWindow(array8)
            val x15: Int = array8[0]
            val x16 = x15 + releight.width
            val y15: Int = array8[1]
            val y16 = y15 + releight.height

            val array9 = IntArray(2)
            relnine.getLocationInWindow(array9)
            val x17: Int = array9[0]
            val x18 = x17 + relnine.width
            val y17: Int = array9[1]
            val y18 = y17 + relnine.height

            if (centerX in x1..x2 && y1 <= centerY && centerY <= y2) {
                TouchedView = relone
            } else if (centerX in x3..x4 && y3 <= centerY && centerY <= y4) {
                TouchedView = reltwo
            } else if (centerX in x5..x6 && y5 <= centerY && centerY <= y6) {
                TouchedView = relthree
            } else if (centerX in x7..x8 && y7 <= centerY && centerY <= y8) {
                TouchedView = relfour
            } else if (centerX in x9..x10 && y9 <= centerY && centerY <= y10) {
                TouchedView = relfive
            } else if (centerX in x11..x12 && y11 <= centerY && centerY <= y12) {
                TouchedView = relsix
            } else if (centerX in x13..x14 && y13 <= centerY && centerY <= y14) {
                TouchedView = relseven
            } else if (centerX in x15..x16 && y15 <= centerY && centerY <= y16) {
                TouchedView = releight
            } else if (centerX in x17..x18 && y17 <= centerY && centerY <= y18) {
                TouchedView = relnine
            }

        }

    }

    @SuppressLint("UseRequireInsteadOfGet")
    private fun initializeUI() {
        pager = view!!.findViewById<ViewPager>(R.id.bannerImagePager)
        relone = view!!.findViewById(R.id.relOne) as RelativeLayout
        reltwo = view!!.findViewById(R.id.relTwo) as RelativeLayout
        relthree = view!!.findViewById(R.id.relThree) as RelativeLayout
        relfour = view!!.findViewById(R.id.relFour) as RelativeLayout
        relfive = view!!.findViewById(R.id.relFive) as RelativeLayout
        relsix = view!!.findViewById(R.id.relSix) as RelativeLayout
        relseven = view!!.findViewById(R.id.relSeven) as RelativeLayout
        releight = view!!.findViewById(R.id.relEight) as RelativeLayout
        relnine = view!!.findViewById(R.id.relNine) as RelativeLayout

        relTxtone = view!!.findViewById(R.id.relTxtOne) as TextView
        relTxttwo = view!!.findViewById(R.id.relTxtTwo) as TextView
        relTxtthree = view!!.findViewById(R.id.relTxtThree) as TextView
        relTxtfour = view!!.findViewById(R.id.relTxtFour) as TextView
        relTxtfive = view!!.findViewById(R.id.relTxtFive) as TextView
        relTxtsix = view!!.findViewById(R.id.relTxtSix) as TextView
        relTxtseven = view!!.findViewById(R.id.relTxtSeven) as TextView
        relTxteight = view!!.findViewById(R.id.relTxtEight) as TextView
        reltxtnine = view!!.findViewById(R.id.relTxtNine) as TextView

        relImgone = view!!.findViewById(R.id.relImgOne) as ImageView
        relImgtwo = view!!.findViewById(R.id.relImgTwo) as ImageView
        relImgthree = view!!.findViewById(R.id.relImgThree) as ImageView
        relImgfour = view!!.findViewById(R.id.relImgFour) as ImageView
        relImgfive = view!!.findViewById(R.id.relImgFive) as ImageView
        relImgsix = view!!.findViewById(R.id.relImgSix) as ImageView
        relImgseven = view!!.findViewById(R.id.relImgSeven) as ImageView
        relImgeight = view!!.findViewById(R.id.relImgEight) as ImageView
        relImgnine = view!!.findViewById(R.id.relImgNine) as ImageView
        mSectionText = arrayOfNulls(9)
        updatedata()

        pager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                currentPage = position
            }

        })

    }

    fun updatedata() {
        val handler = Handler()


        val update = Runnable {
            if (currentPage == bannerarray.size) {
                currentPage = 0
                pager.setCurrentItem(
                    currentPage,
                    true
                )
            } else {
                pager
                    .setCurrentItem(currentPage++, true)
            }
        }
        val swipetimer = Timer()

        swipetimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(update)
            }
        }, 100, 6000)

    }


    private fun CHECKINTENTVALUE(intentTabId: String) {
        TAB_ID = intentTabId
        var mFragment: Fragment? = null
        if (sharedprefs.getUserCode(mContext).equals("")) {
            when (intentTabId) {
                naisTabConstants.TAB_STUDENT_INFORMATION -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_CALENDAR -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }

                naisTabConstants.TAB_MESSAGES -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_COMMUNICATION -> {
                    mFragment = CommunicationFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_REPORT_ABSENCE -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_TEACHER_CONTACT -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_SOCIAL_MEDIA -> {
                    mFragment = SocialMediaFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_TIME_TABLE -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_REPORTS -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
//                naisTabConstants.TAB_ATTENDANCE -> {
//                    showSuccessAlert(
//                        mContext,
//                        "This feature is only available for registered users.",
//                        "Alert"
//                    )
//                }
                naisTabConstants.TAB_CURRICULUM -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_TERM_DATES -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_UPDATE -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
                naisTabConstants.TAB_CONTACT_US -> {
                    if (ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        checkpermission()


                    } else {
                        mFragment = ContactUsFragment()
                        fragmentIntent(mFragment)
                    }
                }
                naisTabConstants.TAB_APPS -> {
                    showSuccessAlert(
                        mContext,
                        "This feature is only available for registered users.",
                        "Alert"
                    )
                }
            }
        } else {
            when (intentTabId) {


                naisTabConstants.TAB_STUDENT_INFORMATION -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = StudentInformationFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_CALENDAR -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = CalendarFragmentNew()
                    fragmentIntent(mFragment)
                }

                naisTabConstants.TAB_MESSAGES -> {
                    mFragment = MessageFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_COMMUNICATION -> {
                    mFragment = CommunicationFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_REPORT_ABSENCE -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = ReportAbsenceFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_TEACHER_CONTACT -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = TeacherContactFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_SOCIAL_MEDIA -> {
                    mFragment = SocialMediaFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_TIME_TABLE -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = TimeTableFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_TERM_DATES -> {
                    mFragment = TermDatesFragment()
                    fragmentIntent(mFragment)
                }
//                naisTabConstants.TAB_ATTENDANCE -> {
//                    sharedprefs.setStudentID(mContext, "")
//                    sharedprefs.setStudentName(mContext, "")
//                    sharedprefs.setStudentPhoto(mContext, "")
//                    sharedprefs.setStudentClass(mContext, "")
//                    mFragment = AttendanceFragment()
//                    fragmentIntent(mFragment)
//                }
                naisTabConstants.TAB_REPORTS -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = ReportsFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_CURRICULUM -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = CurriculumFragment()
                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_UPDATE -> {
                    if (sharedprefs.getDataCollection(mContext)==1)
                    {
                        if(sharedprefs.getDataCollectionShown(mContext)==0)
                        {
                            sharedprefs.setSuspendTrigger(mContext,"2")
                            sharedprefs.setDataCollectionShown(mContext,1)
                            callSettingsUserDetail()

                        }

                    }
                    else{
                        showTriggerDataCollection(mContext,"Confirm?", "Select one or more areas to update", R.drawable.questionmark_icon, R.drawable.round)

                    }
//                    mFragment = CurriculumFragment()
//                    fragmentIntent(mFragment)
                }
                naisTabConstants.TAB_CONTACT_US -> {
                    if (ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_FINE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                            mContext,
                            Manifest.permission.CALL_PHONE
                        ) != PackageManager.PERMISSION_GRANTED
                    ) {
                        checkpermission()


                    } else {
                        mFragment = ContactUsFragment()
                        fragmentIntent(mFragment)
                    }
                    //permissioncheck()


                }
                naisTabConstants.TAB_APPS -> {
                    sharedprefs.setStudentID(mContext, "")
                    sharedprefs.setStudentName(mContext, "")
                    sharedprefs.setStudentPhoto(mContext, "")
                    sharedprefs.setStudentClass(mContext, "")
                    mFragment = AppsFragment()
                    fragmentIntent(mFragment)
                }
            }
        }

    }

    fun getbannerimages() {
        val version = BuildConfig.VERSION_NAME
        val bannerModel = BannerModel(version, 2)
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<ResponseBody> =
            ApiClient.getClient.bannerimages(bannerModel, "Bearer " + token)
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
                                val dataArray = responseObj.getJSONArray("banner_images")
                                val appVersion = responseObj.optString("android_app_version")
                                sharedprefs.setAppVersion(mContext, appVersion)
                                versionfromapi =
                                    sharedprefs.getAppVersion(mContext)!!.replace(".", "")
                                currentversion = currentversion.replace(".", "")

                                Log.e("APPVERSIONAPI:", versionfromapi)
                                Log.e("CURRENTVERSION:", currentversion)

                                if (!sharedprefs.getAppVersion(mContext).equals("", true)) {
                                    if (versionfromapi > currentversion) {
                                        showforceupdate(mContext)

                                    }
                                }

                                if (dataArray.length() > 0) {
                                    for (i in 0..dataArray.length()) {
                                        bannerarray.add(dataArray.optString(i))
                                    }
                                    pager.adapter = activity?.let { PageView(it, bannerarray) }
                                } else {
                                    pager.setBackgroundResource(R.drawable.aboutbanner)
                                }
                            } else {
                                if (status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(mContext)
                                    getbannerimages()

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

     fun showforceupdate(mContext: Context) {
        val dialog = Dialog(mContext)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialog_updateversion)
        val btnUpdate =
            dialog.findViewById<View>(R.id.btnUpdate) as Button

        btnUpdate.setOnClickListener {
            dialog.dismiss()
            val appPackageName =
                mContext.packageName
            try {
                mContext.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=$appPackageName")
                    )
                )

            } catch (e: android.content.ActivityNotFoundException) {
                mContext.startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }

        }
        dialog.show()
    }


    fun fragmentIntent(mFragment: Fragment?) {
        if (mFragment != null) {
            val fragmentManager = activity!!.supportFragmentManager
            fragmentManager.beginTransaction()
                .add(R.id.fragment_holder, mFragment, appController.mTitles)
                .addToBackStack(appController.mTitles).commitAllowingStateLoss() //commit
            //.commit()
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

    fun callSettingsUserDetail() {
        val bannerModel = BannerModel("1.0.0", 2)
        val token = sharedprefs.getaccesstoken(mContext)
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
                                sharedprefs.setAppVersion(mContext, appVersion)
                                sharedprefs.setDataCollection(mContext, data_collection)
                                sharedprefs.setTriggerType(mContext, trigger_type)
                                sharedprefs.setAlreadyTriggered(mContext, already_triggered)

                                if (sharedprefs.getDataCollection(mContext) == 1)
                                {

                                    if (sharedprefs.getAlreadyTriggered(mContext) == 0) {
                                        callDataCollectionAPI()

                                    } else {
                                        if (previousTriggerType == sharedprefs.getTriggerType(
                                                mContext
                                            )
                                        ) {
                                            if (!sharedprefs.getSuspendTrigger(mContext)
                                                    .equals("1")
                                            ) {
                                                val intent = Intent(
                                                    activity,
                                                    DataCollectionActivity::class.java
                                                )
                                                activity?.startActivity(intent)
                                            }

                                        } else
                                        {
                                            callDataCollectionAPI()
                                        }
                                    }

                                }

                            } else {
                                if (status == 116) {
                                    //call Token Expired
                                    AccessTokenClass.getAccessToken(mContext)
                                    callSettingsUserDetail()

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


    fun callStudentListApi() {
        studentListArrayList = ArrayList()
        var studentSaveArrayList = ArrayList<StudentListDataCollection>()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<StudentListModel> = ApiClient.getClient.studentList("Bearer " + token)
        call.enqueue(object : Callback<StudentListModel> {
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<StudentListModel>,
                response: Response<StudentListModel>
            ) {

                if (response.body()!!.status == 100) {
                    studentListArrayList.addAll(response.body()!!.responseArray.studentList)
                    for (i in 0..studentListArrayList.size - 1) {
                        var model = StudentListDataCollection()
                        model.id = studentListArrayList.get(i).id
                        model.name = studentListArrayList.get(i).name
                        model.unique_id = studentListArrayList.get(i).unique_id
                        model.house = studentListArrayList.get(i).house
                        model.photo = studentListArrayList.get(i).photo
                        model.section = studentListArrayList.get(i).section
                        model.studentListClass = studentListArrayList.get(i).studentClass
                        model.isConfirmed = false
                        studentSaveArrayList.add(model)
                    }
                    sharedprefs.setStudentArrayList(mContext, studentSaveArrayList)


                } else {
                    if (response.body()!!.status == 116) {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(mContext)
                        callStudentListApi()

                    } else {
                        if (response.body()!!.status == 103) {
                            //validation check error

                        } else {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(
                                response.body()!!.status,
                                mContext
                            )
                        }
                    }

                }


            }

        })
    }

    fun callTilesListApi() {
        titlesListArrayList = ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<TilesListModel> = ApiClient.getClient.titlesList("Bearer " + token)
        call.enqueue(object : Callback<TilesListModel> {
            override fun onFailure(call: Call<TilesListModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<TilesListModel>,
                response: Response<TilesListModel>
            ) {

                if (response.body()!!.status == 100) {
                    titlesListArrayList.addAll(response.body()!!.responseArray.titlesList)
                    sharedprefs.setTitleArrayList(mContext, titlesListArrayList)

                } else {
                    if (response.body()!!.status == 116) {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(mContext)
                        callTilesListApi()

                    } else {
                        if (response.body()!!.status == 103) {
                            //validation check error

                        } else {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(
                                response.body()!!.status,
                                mContext
                            )
                        }
                    }

                }


            }

        })
    }

    fun callCountryListApi() {
        countryistArrayList = ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<CountryListModel> = ApiClient.getClient.countryList("Bearer " + token)
        call.enqueue(object : Callback<CountryListModel> {
            override fun onFailure(call: Call<CountryListModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<CountryListModel>,
                response: Response<CountryListModel>
            ) {
                if (response.body()!!.status == 100) {
                    countryistArrayList.addAll(response.body()!!.responseArray.countriesList)
                    sharedprefs.setCountryArrayList(mContext, countryistArrayList)
                    // Log.e("Country List Size", sharedprefs.getCountryArrayList(mContext).size.toString() )

                } else {
                    if (response.body()!!.status == 116) {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(mContext)
                        callCountryListApi()

                    } else {
                        if (response.body()!!.status == 103) {
                            //validation check error

                        } else {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(
                                response.body()!!.status,
                                mContext
                            )
                        }
                    }

                }


            }

        })
    }

    fun callRelationshipApi() {
        relationshipArrayList = ArrayList()
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<RelationShipListModel> =
            ApiClient.getClient.relationshipList("Bearer " + token)
        call.enqueue(object : Callback<RelationShipListModel> {
            override fun onFailure(call: Call<RelationShipListModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
            }

            override fun onResponse(
                call: Call<RelationShipListModel>,
                response: Response<RelationShipListModel>
            ) {
                if (response.body()!!.status == 100) {
                    relationshipArrayList.addAll(response.body()!!.responseArray.contactTypesList)
                    sharedprefs.setRelationShipArrayList(mContext, relationshipArrayList)

                } else {
                    if (response.body()!!.status == 116) {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(mContext)
                        callRelationshipApi()

                    } else {
                        if (response.body()!!.status == 103) {
                            //validation check error
                            InternetCheckClass.checkApiStatusError(
                                response.body()!!.status,
                                mContext
                            )
                        } else {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(
                                response.body()!!.status,
                                mContext
                            )
                        }
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
        val token = sharedprefs.getaccesstoken(mContext)
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
                        mContext,
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

                        if (sharedprefs.getOwnContactDetailArrayList(mContext) == null || sharedprefs.getOwnContactDetailArrayList(
                                mContext
                            )!!.size == 0
                        ) {
                            sharedprefs.setIsAlreadyEnteredOwn(mContext, true)
                            sharedprefs.setOwnContactDetailArrayList(
                                mContext,
                                ownContactDetailSaveArrayList
                            )
                        } else {
                            if (!sharedprefs.getIsAlreadyEnteredOwn(mContext)) {
                                sharedprefs.setIsAlreadyEnteredOwn(mContext, true)
                                sharedprefs.setOwnContactDetailArrayList(
                                    mContext,
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
                        if (sharedprefs.getPassportDetailArrayList(mContext) == null || sharedprefs.getPassportDetailArrayList(
                                mContext
                            )!!.size == 0
                        ) {
                            sharedprefs.setPassportDetailArrayList(mContext, passportSaveArrayList)
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
                        if (sharedprefs.getHealthDetailArrayList(mContext) == null || sharedprefs.getHealthDetailArrayList(
                                mContext
                            )!!.size == 0
                        ) {
                            sharedprefs.setHealthDetailArrayList(mContext, healthSaveArrayList)
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
                        if (sharedprefs.getKinDetailArrayList(mContext) == null || sharedprefs.getKinDetailArrayList(
                                mContext
                            )!!.size == 0
                        ) {
                            Log.e("DATA COLLECTION", "ENTERS2")
                            sharedprefs.setIsAlreadyEnteredKin(mContext, true)
                            sharedprefs.setKinDetailArrayList(mContext, kinDetailSaveArrayList)
                            sharedprefs.setKinDetailPassArrayList(mContext, kinDetailSaveArrayList)
                        } else {
                            Log.e("DATA COLLECTION", "ENTERS3")
                            if (!sharedprefs.getIsAlreadyEnteredKin(mContext)) {
                                Log.e("DATA COLLECTION", "ENTERS4")
                                sharedprefs.setIsAlreadyEnteredKin(mContext, true)
                                sharedprefs.setKinDetailArrayList(mContext, kinDetailSaveArrayList)
                                sharedprefs.setKinDetailPassArrayList(
                                    mContext,
                                    kinDetailSaveArrayList
                                )
                            }
                        }
                    }

                    //Intent
                    Log.e("DATA COLLECTION", "ENTERS5")
                    if (!sharedprefs.getSuspendTrigger(mContext).equals("1")) {
                        val intent = Intent(mContext, DataCollectionActivity::class.java)
                        activity?.startActivity(intent)
                    }

                } else {
                    if (response.body()!!.status == 116) {
                        //call Token Expired
                        AccessTokenClass.getAccessToken(mContext)
                        callDataCollectionAPI()

                    } else {
                        if (response.body()!!.status == 103) {
                            //validation check error

                        } else {
                            //check status code checks
                            InternetCheckClass.checkApiStatusError(
                                response.body()!!.status,
                                mContext
                            )
                        }
                    }

                }
            }

        })
    }


    private fun checkpermission() {
        if (ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                mContext,
                Manifest.permission.CALL_PHONE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this.activity!!,
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.CALL_PHONE
                ),
                123
            )
        }
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
        var linearLayoutManagerM : LinearLayoutManager = LinearLayoutManager(mContext)
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
}




