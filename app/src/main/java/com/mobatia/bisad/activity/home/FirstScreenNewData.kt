package com.mobatia.bisad.activity.home


import `in`.galaxyofandroid.spinerdialog.OnSpinerItemClick
import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.app.Dialog
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.home.adapter.FamilyContactRecyclerAdapter
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.home.model.CountryiesDetailModel
import com.mobatia.bisad.fragment.home.model.RelationShipDetailModel
import com.mobatia.bisad.fragment.home.model.TitlesArrayList
import com.mobatia.bisad.fragment.home.model.datacollection.KinDetailApiModel
import com.mobatia.bisad.fragment.home.model.datacollection.OwnContactModel
import com.mobatia.bisad.fragment.home.relationshipArrayList
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener


class FirstScreenNewData:Fragment() {
    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var closeImg: ImageView
    lateinit var plusImgNoContent: ImageView
    lateinit var RecyclerPlusBtn: ImageView
    lateinit var messageTxt: TextView
    lateinit var nameOwnDetailTxt: TextView
    lateinit var contactTypeOwnDetailTxt: TextView
    lateinit var helpView: TextView
    lateinit var ownDetailViewRelative: RelativeLayout
    lateinit var NoDataLinearLayout: LinearLayout
    lateinit var RecyclerLinearLayout: LinearLayout
    lateinit var confirmBtn: Button
    lateinit var mContext: Context
    lateinit var ownContactDetailArrayList:ArrayList<OwnContactModel>
    lateinit var kinDetailArrayList:ArrayList<KinDetailApiModel>
    lateinit var nameValue:String
    lateinit var relationshipValue:String
    lateinit var familyContactRecycler:RecyclerView
    lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var familyKinRecyclerAdapter: FamilyContactRecyclerAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_first_screen, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        initializeUI()
    }

    private fun initializeUI() {
        ownContactDetailArrayList= ArrayList()
        ownContactDetailArrayList= sharedprefs.getOwnContactDetailArrayList(mContext)!!
        kinDetailArrayList= sharedprefs.getKinDetailArrayList(mContext)!!
        closeImg=view!!.findViewById(R.id.closeImg)
        messageTxt=view!!.findViewById(R.id.messageTxt)
        nameOwnDetailTxt=view!!.findViewById(R.id.nameOwnDetailTxt)
        contactTypeOwnDetailTxt=view!!.findViewById(R.id.contactTypeOwnDetailTxt)
        ownDetailViewRelative=view!!.findViewById(R.id.ownDetailViewRelative)
        confirmBtn=view!!.findViewById(R.id.confirmBtn)
        helpView=view!!.findViewById(R.id.helpView)
        NoDataLinearLayout=view!!.findViewById(R.id.NoDataLinearLayout)
        RecyclerLinearLayout=view!!.findViewById(R.id.RecyclerLinearLayout)
        familyContactRecycler=view!!.findViewById(R.id.familyContactRecycler)
        RecyclerPlusBtn=view!!.findViewById(R.id.RecyclerPlusBtn)
        plusImgNoContent=view!!.findViewById(R.id.plusImgNoContent)
        linearLayoutManager = LinearLayoutManager(mContext)
        familyContactRecycler.layoutManager = linearLayoutManager
        familyContactRecycler.itemAnimator = DefaultItemAnimator()
        nameValue=ownContactDetailArrayList.get(0).name
        relationshipValue=ownContactDetailArrayList.get(0).relationship
        if (sharedprefs.getDisplayMessage(mContext).equals(""))
        {
          //  messageTxt.visibility=View.GONE
        }
        else{
            messageTxt.visibility=View.VISIBLE
          //  messageTxt.setText(sharedprefs.getDisplayMessage(mContext))
        }
        helpView.setOnClickListener(View.OnClickListener {
            ShowHelpDialog(activity!!, "Help", R.drawable.questionmark_icon, R.drawable.round)
        })

        Log.e("KINARRAY",kinDetailArrayList.size.toString())

        if (kinDetailArrayList.size>0)
        {
            Log.e("KINARRAY INSIDE",kinDetailArrayList.size.toString())
            RecyclerLinearLayout.visibility=View.VISIBLE
            NoDataLinearLayout.visibility=View.GONE
            familyKinRecyclerAdapter= FamilyContactRecyclerAdapter(sharedprefs.getKinDetailArrayList(mContext)!!)
            familyContactRecycler.setAdapter(familyKinRecyclerAdapter)
        }
        else{
            RecyclerLinearLayout.visibility=View.GONE
            NoDataLinearLayout.visibility=View.VISIBLE
        }

        familyContactRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {

                showKinDetail(position,activity!!)
            }
        })
        plusImgNoContent.setOnClickListener(View.OnClickListener {

            showKinDetailAdd(activity!!)
        })
        RecyclerPlusBtn.setOnClickListener(View.OnClickListener {

            showKinDetailAdd(activity!!)
        })
        nameOwnDetailTxt.setText(nameValue)
        contactTypeOwnDetailTxt.setText(relationshipValue)
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

        if (ownContactDetailArrayList.get(0).isConfirmed)
        {
            ownDetailViewRelative.setBackgroundResource(R.drawable.rect_background_grey)
            confirmBtn.visibility=View.GONE
        }
        else{
            ownDetailViewRelative.setBackgroundResource(R.drawable.rect_data_collection_red)
            confirmBtn.visibility=View.VISIBLE
        }
        ownDetailViewRelative.setOnClickListener(View.OnClickListener {

            showOwnContactDetail(mContext,sharedprefs.getOwnContactDetailArrayList(mContext)!!)

        })
    }



    fun showOwnContactDetail(context:Context,ownconatctArrayList:ArrayList<OwnContactModel>)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.data_collection_confirm)

        var DropEdt=dialog.findViewById(R.id.DropEdt)as AutoCompleteTextView
      //  var relationshipSpinner=dialog.findViewById(R.id.relationshipSpinner)as AutoCompleteTextView
        var firstNameTxt=dialog.findViewById(R.id.firstNameTxt)as EditText
        var lastNameTxt=dialog.findViewById(R.id.lastNameTxt)as EditText
        var dataCollect_Code=dialog.findViewById(R.id.dataCollect_Code)as EditText
        var emailTxt=dialog.findViewById(R.id.emailTxt)as EditText
        var dataCollect_Phone=dialog.findViewById(R.id.dataCollect_Phone)as EditText
        var addressLine1=dialog.findViewById(R.id.addressLine1)as EditText
        var addressLine2=dialog.findViewById(R.id.addressLine2)as EditText
        var addressLine3=dialog.findViewById(R.id.addressLine3)as EditText
        var townTxt=dialog.findViewById(R.id.townTxt)as EditText
        var stateTxt=dialog.findViewById(R.id.stateTxt)as EditText
        var pinTxt=dialog.findViewById(R.id.pinTxt)as EditText
        var countryTxt=dialog.findViewById(R.id.countryTxt)as TextView
        var communicationPreferenceInfoTxt=dialog.findViewById(R.id.communicationPreferenceInfoTxt)as TextView
        var relationshipTxt=dialog.findViewById(R.id.relationshipTxt)as TextView
        var passportLinear=dialog.findViewById(R.id.passportLinear)as LinearLayout
        var relationShipLinear=dialog.findViewById(R.id.relationShipLinear)as LinearLayout
        var backImg=dialog.findViewById(R.id.backImg)as ImageView
        var confirmBtnPop=dialog.findViewById(R.id.confirmBtn)as Button
        var spinnerDialog:SpinnerDialog
        var spinnerRelationShipDialog:SpinnerDialog
        var id=sharedprefs.getOwnContactDetailArrayList(context)!!.get(0).id
        var userID=sharedprefs.getOwnContactDetailArrayList(context)!!.get(0).user_id
        var createdAt=sharedprefs.getOwnContactDetailArrayList(context)!!.get(0).created_at
        var updatedAt=sharedprefs.getOwnContactDetailArrayList(context)!!.get(0).updated_at
        firstNameTxt.setImeOptions(EditorInfo.IME_ACTION_DONE)
        lastNameTxt.setImeOptions(EditorInfo.IME_ACTION_DONE)
        emailTxt.setImeOptions(EditorInfo.IME_ACTION_DONE)
        dataCollect_Phone.setImeOptions(EditorInfo.IME_ACTION_DONE)
        dataCollect_Code.setImeOptions(EditorInfo.IME_ACTION_DONE)
        addressLine1.setImeOptions(EditorInfo.IME_ACTION_DONE)
        addressLine2.setImeOptions(EditorInfo.IME_ACTION_DONE)
        addressLine3.setImeOptions(EditorInfo.IME_ACTION_DONE)
        townTxt.setImeOptions(EditorInfo.IME_ACTION_DONE)
        pinTxt.setImeOptions(EditorInfo.IME_ACTION_DONE)

        firstNameTxt.isFocusable=true
        lastNameTxt.isFocusable=true
        emailTxt.isFocusable=true
        dataCollect_Code.isFocusable=true
        dataCollect_Phone.isFocusable=true
        addressLine1.isFocusable=true
        addressLine2.isFocusable=true
        addressLine3.isFocusable=true
        townTxt.isFocusable=true
        stateTxt.isFocusable=true
        pinTxt.isFocusable=true

        firstNameTxt.isFocusableInTouchMode=true
        lastNameTxt.isFocusableInTouchMode=true
        emailTxt.isFocusableInTouchMode=true
        dataCollect_Code.isFocusableInTouchMode=true
        dataCollect_Phone.isFocusableInTouchMode=true
        addressLine1.isFocusableInTouchMode=true
        addressLine2.isFocusableInTouchMode=true
        addressLine3.isFocusableInTouchMode=true
        townTxt.isFocusableInTouchMode=true
        stateTxt.isFocusableInTouchMode=true
        pinTxt.isFocusableInTouchMode=true

        var isChanged:Boolean=false

        var countryArrayList=ArrayList<CountryiesDetailModel>()

        var titleArrayList=ArrayList<TitlesArrayList>()
        titleArrayList=sharedprefs.getTitleArrayList(context)
        countryArrayList=sharedprefs.getCountryArrayList(context)
        var relationShipArrayList=ArrayList<RelationShipDetailModel>()
        relationShipArrayList=sharedprefs.getRelationShipArrayList(context)

        communicationPreferenceInfoTxt.setOnClickListener(View.OnClickListener {

            val deliveryAddress =
                arrayOf("mobileapp@bisad.ae")
            val emailIntent = Intent(Intent.ACTION_SEND)
            emailIntent.putExtra(Intent.EXTRA_EMAIL, deliveryAddress)
            emailIntent.setType("text/plain")
            emailIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            val pm: PackageManager = communicationPreferenceInfoTxt.getContext().getPackageManager()
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
                    communicationPreferenceInfoTxt.getContext().startActivity(emailIntent)
                    break
                }
            }
        })
        var countryData=ArrayList<String>()
        for (i in 0..countryArrayList.size-1)
        {
            countryData.add(countryArrayList.get(i).name)
        }
        spinnerDialog = SpinnerDialog(activity, countryData, "Select Country", "Close") // With No Animation
        spinnerDialog = SpinnerDialog(activity, countryData, "Select Country", R.style.DialogAnimations_SmileWindow, "Close") // With 	Animation
        spinnerDialog.setCancellable(true)
        spinnerDialog.setShowKeyboard(false)
        spinnerDialog.bindOnSpinerListener(object : OnSpinerItemClick {
            override fun onClick(item: String, position: Int) {
                countryTxt.setText(item)
                isChanged=true
            }
        })
        var relationshipData=ArrayList<String>()
        for(i in 0..relationShipArrayList.size-1)
        {
            relationshipData.add(relationshipArrayList.get(i).name)
        }
        spinnerRelationShipDialog = SpinnerDialog(activity, relationshipData, "Select RelationShip", "Close") // With No Animation
        spinnerRelationShipDialog = SpinnerDialog(activity, relationshipData, "Select RelationShip", R.style.DialogAnimations_SmileWindow, "Close") // With 	Animation
        spinnerRelationShipDialog.setCancellable(true)
        spinnerRelationShipDialog.setShowKeyboard(false)
        spinnerRelationShipDialog.bindOnSpinerListener(object : OnSpinerItemClick {
            override fun onClick(item: String, position: Int) {
                relationshipTxt.setText(item)

                isChanged=true
            }
        })
        passportLinear.setOnClickListener(View.OnClickListener {
            spinnerDialog.showSpinerDialog()
        })

        relationShipLinear.setOnClickListener(View.OnClickListener {
            spinnerRelationShipDialog.showSpinerDialog()
        })


        var relationShipData=ArrayList<String>()
        var SpinnerData=ArrayList<String>()
        for (i in 0..titleArrayList.size-1)
        {
            SpinnerData.add(titleArrayList.get(i).name)
        }

        if (ownconatctArrayList.get(0).title.equals(""))
        {
            DropEdt.setText(SpinnerData.get(0).toString())
        }
        else
        {
            DropEdt.setText(ownconatctArrayList.get(0).title)
        }
        val DROP = ArrayAdapter(context, android.R.layout.simple_list_item_1, SpinnerData)
        DropEdt.isCursorVisible = false
        DropEdt.isFocusable = false
        DropEdt.setAdapter(DROP)
        DropEdt.setOnClickListener {
            DropEdt.showDropDown()
            isChanged=true
        }
        if (ownconatctArrayList.get(0).name.equals(""))
        {
            firstNameTxt.setHint("First name")
        }
        else{
            firstNameTxt.setText(ownconatctArrayList.get(0).name)
        }
        if (ownconatctArrayList.get(0).last_name.equals(""))
        {
            lastNameTxt.setHint("Last name")
        }
        else{
            lastNameTxt.setText(ownconatctArrayList.get(0).last_name)
        }
        if (ownconatctArrayList.get(0).email.equals(""))
        {
            emailTxt.setHint("Email ID")
        }
        else{
            emailTxt.setText(ownconatctArrayList.get(0).email)
        }
        if (ownconatctArrayList.get(0).code.equals(""))
        {
            dataCollect_Code.setText("+971")
        }
        else{
            dataCollect_Code.setText(ownconatctArrayList.get(0).code)
        }
        if (ownconatctArrayList.get(0).user_mobile.equals(""))
        {
            dataCollect_Phone.setText(resources.getString(R.string.AST_PHONE))
        }
        else{
            dataCollect_Phone.setText(ownconatctArrayList.get(0).user_mobile)
        }
        if (ownconatctArrayList.get(0).address1.equals(""))
        {
//            addressLine1.setText(resources.getString(R.string.AST_ADD_1))
        }
        else{
            addressLine1.setText(ownconatctArrayList.get(0).address1)
        }
        if (ownconatctArrayList.get(0).address2.equals(""))
        {
            //addressLine1.setText(resources.getString(R.string.AST_ADD_1))
        }
        else{
            addressLine2.setText(ownconatctArrayList.get(0).address2)
        }
        if (ownconatctArrayList.get(0).address3.equals(""))
        {
            //addressLine1.setText(resources.getString(R.string.AST_ADD_1))
        }
        else{
            addressLine3.setText(ownconatctArrayList.get(0).address3)
        }
        if (ownconatctArrayList.get(0).code.equals(""))
        {
          //  pinTxt.setHint(resources.getString(R.string.AST_POST))
        }
        else{
            pinTxt.setText(ownconatctArrayList.get(0).code)
        }
        if (ownconatctArrayList.get(0).country.equals(""))
        {
            countryTxt.setText(countryArrayList.get(0).name)
        }
        else{
            countryTxt.setText(ownconatctArrayList.get(0).country)
        }
        if (ownconatctArrayList.get(0).state.equals(""))
        {
            //stateTxt.setHint(resources.getString(R.string.AST_STATE))
        }
        else
        {
            stateTxt.setText(ownconatctArrayList.get(0).state)
        }
        if (ownconatctArrayList.get(0).town.equals(""))
        {
            //stateTxt.setHint(resources.getString(R.string.AST_STATE))
        }
        else
        {
            townTxt.setText(ownconatctArrayList.get(0).town)
        }
        if (ownconatctArrayList.get(0).address2.equals(""))
        {
            //stateTxt.setHint(resources.getString(R.string.AST_STATE))
        }
        else
        {
            addressLine2.setText(ownconatctArrayList.get(0).address2)
        }
        if (ownconatctArrayList.get(0).relationship.equals(""))
        {
            relationshipTxt.setText(relationShipArrayList.get(0).name)
        }
        else
        {
            relationshipTxt.setText(ownconatctArrayList.get(0).relationship)
        }


        firstNameTxt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                 isChanged=true
            }
        })
        lastNameTxt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                 isChanged=true
            }
        })
        emailTxt.addTextChangedListener(object : TextWatcher
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                 isChanged=true
            }
        })
        dataCollect_Code.addTextChangedListener(object : TextWatcher
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                 isChanged=true
            }
        })

        dataCollect_Phone.addTextChangedListener(object : TextWatcher
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                 isChanged=true
            }
        })
        addressLine1.addTextChangedListener(object : TextWatcher
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                 isChanged=true
            }
        })
        addressLine2.addTextChangedListener(object : TextWatcher
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                 isChanged=true
            }
        })
        addressLine3.addTextChangedListener(object : TextWatcher
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                 isChanged=true
            }
        })
        townTxt.addTextChangedListener(object : TextWatcher
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                 isChanged=true
            }
        })
        stateTxt.addTextChangedListener(object : TextWatcher
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                 isChanged=true
            }
        })
        pinTxt.addTextChangedListener(object : TextWatcher
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                 isChanged=true
            }
        })
        stateTxt.addTextChangedListener(object : TextWatcher
        {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                 isChanged=true
            }
        })


        backImg.setOnClickListener(View.OnClickListener {

            if (isChanged)
            {
                Log.e("Changed","not works")
                sharedprefs.getOwnContactDetailArrayList(context)!!.clear()
                var mDataArrayList=ArrayList<OwnContactModel>()
                var model=OwnContactModel()
                model.id=id
                model.user_id=userID
                model.title=DropEdt.text.toString().trim()
                model.name=firstNameTxt.text.toString().trim()
                model.last_name=lastNameTxt.text.toString().trim()
                model.relationship=relationshipTxt.text.toString().trim()
                model.email=emailTxt.text.toString().trim()
                model.phone=dataCollect_Code.text.toString().trim()+dataCollect_Phone.text.toString().trim()
                model.code=dataCollect_Code.text.toString().trim()
                model.user_mobile=dataCollect_Phone.text.toString().trim()
                model.address1=addressLine1.text.toString().trim()
                model.address2=addressLine2.text.toString().trim()
                model.address3=addressLine2.text.toString().trim()
                model.town=townTxt.text.toString().trim()
                model.state=stateTxt.text.toString().trim()
                model.country=countryTxt.text.toString().trim()
                model.pincode=pinTxt.text.toString().trim()
                if (isChanged)
                {
                    model.status=1
                }
                else
                {
                    model.status=5
                }
                model.created_at=createdAt
                model.updated_at=updatedAt
                model.isUpdated=false
                model.isConfirmed=false
                mDataArrayList.add(model)
                sharedprefs.setOwnContactDetailArrayList(context,mDataArrayList)
                ownDetailViewRelative.setBackgroundResource(R.drawable.rect_data_collection_red)
                confirmBtn.visibility=View.VISIBLE
                dialog.dismiss()
                familyKinRecyclerAdapter.notifyDataSetChanged()
            }
            else{
                Log.e("Changed","not works")
                dialog.dismiss()
            }
        })
        confirmBtnPop.setOnClickListener(View.OnClickListener {
            if (firstNameTxt.text.toString().trim().equals(""))
            {
                showSuccessAlert(context,"Please Enter the First Name","Alert")
            }
            else
            {
                if (emailTxt.text.toString().trim().equals(""))
                {
                    showSuccessAlert(context,"Please Enter Email ID","Alert")
                }
                else{
                    if (dataCollect_Code.text.toString().trim().equals(""))
                    {
                        showSuccessAlert(context,"Please Enter the Country Code","Alert")
                    }
                    else{
                        if (dataCollect_Phone.text.toString().trim().equals(""))
                        {
                            showSuccessAlert(context,"Please Enter the Phone Number","Alert")
                        }
                        else{
                            if (addressLine1.text.toString().trim().equals(""))
                            {
                                showSuccessAlert(context,"Please Enter the Address1","Alert")
                            }
                            else{
                                if (addressLine2.text.toString().trim().equals(""))
                                {
                                    showSuccessAlert(context,"Please Enter the Address2","Alert")
                                }
                                else{
                                    if (townTxt.text.toString().trim().equals(""))
                                    {
                                        showSuccessAlert(context,"Please Enter the Town","Alert")
                                    }
                                    else{
                                        if (stateTxt.text.toString().trim().equals(""))
                                        {
                                            showSuccessAlert(context,"Please Enter the State","Alert")
                                        }
                                        else{
                                            var emailPattern = InternetCheckClass.isEmailValid(emailTxt.text.toString().trim())
                                            if (!emailPattern)
                                            {
                                                showSuccessAlert(context,"Please Enter a valid Email","Alert")
                                            }
                                            else{

                                                var mDataArrayList=ArrayList<OwnContactModel>()
                                                var model=OwnContactModel()
                                                model.id=id
                                                model.user_id=userID
                                                model.title=DropEdt.text.toString().trim()
                                                model.name=firstNameTxt.text.toString().trim()
                                                model.last_name=lastNameTxt.text.toString().trim()
                                                model.relationship=relationshipTxt.text.toString().trim()
                                                model.email=emailTxt.text.toString().trim()
                                                model.phone=dataCollect_Code.text.toString().trim()+dataCollect_Phone.text.toString().trim()
                                                model.code=dataCollect_Code.text.toString().trim()
                                                model.user_mobile=dataCollect_Phone.text.toString().trim()
                                                model.address1=addressLine1.text.toString().trim()
                                                model.address2=addressLine2.text.toString().trim()
                                                model.address3=addressLine3.text.toString().trim()
                                                model.town=townTxt.text.toString().trim()
                                                model.state=stateTxt.text.toString().trim()
                                                model.country=countryTxt.text.toString().trim()
                                                model.pincode=pinTxt.text.toString().trim()
                                                if (isChanged)
                                                {
                                                    model.status=1
                                                }
                                                else
                                                {
                                                    model.status=5
                                                }
                                                model.created_at=createdAt
                                                model.updated_at=updatedAt
                                                model.isUpdated=true
                                                model.isConfirmed=true

                                                mDataArrayList.add(model)
                                                sharedprefs.getOwnContactDetailArrayList(context)!!.clear()
                                                sharedprefs.setOwnContactDetailArrayList(context,mDataArrayList)
                                                sharedprefs.getOwnContactDetailArrayList(context)!!.get(0).isConfirmed=true
                                                ownDetailViewRelative.setBackgroundResource(R.drawable.rect_background_grey)
                                                confirmBtn.visibility=View.GONE
                                                dialog.dismiss()

                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        })
        dialog.show()
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

    fun ShowHelpDialog(activity:FragmentActivity,help:String,questionmark_icon:Int,round:Int)
    {
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.alert_help_layout)
        val icon =
            dialog.findViewById<ImageView>(R.id.iconImageView)
        icon.setBackgroundResource(round)
        icon.setImageResource(questionmark_icon)
        val textHead = dialog.findViewById<TextView>(R.id.alertHead)
        textHead.text = help
        val dialogButton =
            dialog.findViewById<Button>(R.id.btn_Ok)
        dialogButton.setOnClickListener { dialog.dismiss() }
        dialog.show()
    }


    fun showKinDetail(positionClicked:Int,activity: FragmentActivity)
    {
        var isKinChanged:Boolean=false
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialoge_kin_detail)
        var imageView4=dialog.findViewById(R.id.imageView4)as ImageView
        var remove_kin=dialog.findViewById(R.id.remove_kin)as ImageView
        var ContactDetails_Spinnertype=dialog.findViewById(R.id.ContactDetails_Spinnertype)as AutoCompleteTextView
        var relationShipLinear=dialog.findViewById(R.id.relationShipLinear)as LinearLayout
        var contactDetails_fname=dialog.findViewById(R.id.contactDetails_fname)as EditText
        var ContactDetails_Lastname=dialog.findViewById(R.id.ContactDetails_Lastname)as EditText
        var ContactDetails_Email=dialog.findViewById(R.id.ContactDetails_Email)as EditText
        var ContactDetails_Phone=dialog.findViewById(R.id.ContactDetails_Phone)as EditText
        var relationshipTxt=dialog.findViewById(R.id.relationshipTxt)as TextView
        var spinnerCode=dialog.findViewById(R.id.spinnerCode)as EditText
        var ContactDetails_Submit=dialog.findViewById(R.id.ContactDetails_Submit)as Button
        var spinnerRelationShipDialog:SpinnerDialog

        ContactDetails_Lastname.setImeOptions(EditorInfo.IME_ACTION_DONE)
        ContactDetails_Email.setImeOptions(EditorInfo.IME_ACTION_DONE)
        ContactDetails_Phone.setImeOptions(EditorInfo.IME_ACTION_DONE)
        spinnerCode.setImeOptions(EditorInfo.IME_ACTION_DONE)
        contactDetails_fname.setImeOptions(EditorInfo.IME_ACTION_DONE)
        contactDetails_fname.isFocusable=true
        contactDetails_fname.isFocusableInTouchMode=true

        ContactDetails_Lastname.isFocusable=true
        ContactDetails_Email.isFocusable=true
        ContactDetails_Phone.isFocusable=true
        spinnerCode.isFocusable=true


        ContactDetails_Lastname.isFocusableInTouchMode=true
        ContactDetails_Email.isFocusableInTouchMode=true
        ContactDetails_Phone.isFocusableInTouchMode=true
        spinnerCode.isFocusableInTouchMode=true

        var kinDetailArrayList=ArrayList<KinDetailApiModel>()
        var kinDetailPassArrayList=ArrayList<KinDetailApiModel>()
        kinDetailArrayList=sharedprefs.getKinDetailArrayList(activity)!!
        kinDetailPassArrayList=sharedprefs.getKinDetailArrayList(activity)!!

        var relationShipArrayList=ArrayList<RelationShipDetailModel>()
        relationShipArrayList=sharedprefs.getRelationShipArrayList(activity)
        var titleArrayList=ArrayList<TitlesArrayList>()
        titleArrayList=sharedprefs.getTitleArrayList(activity)
        var SpinnerData=ArrayList<String>()
        for (i in 0..titleArrayList.size-1)
        {
            SpinnerData.add(titleArrayList.get(i).name)
        }
        if (kinDetailArrayList.get(positionClicked).title.equals(""))
        {
            ContactDetails_Spinnertype.setText(SpinnerData.get(0).toString())
        }
        else
        {
            ContactDetails_Spinnertype.setText(kinDetailArrayList.get(positionClicked).title)
        }
        val DROP = ArrayAdapter(activity, android.R.layout.simple_list_item_1, SpinnerData)
        ContactDetails_Spinnertype.isCursorVisible = false
        ContactDetails_Spinnertype.isFocusable = false
        ContactDetails_Spinnertype.setAdapter(DROP)
        ContactDetails_Spinnertype.setOnClickListener {
            ContactDetails_Spinnertype.showDropDown()
            isKinChanged=true
            var dataArrayList=ArrayList<KinDetailApiModel>()
            var model=KinDetailApiModel()
            model.id=kinDetailArrayList.get(positionClicked).id
            model.kin_id=kinDetailArrayList.get(positionClicked).kin_id
            model.user_id=kinDetailArrayList.get(positionClicked).user_id
            model.title=ContactDetails_Spinnertype.text.toString().trim()
            model.name=contactDetails_fname.text.toString().trim()
            model.last_name=ContactDetails_Lastname.text.toString().trim()
            model.relationship=relationshipTxt.text.toString().trim()
            model.email=ContactDetails_Email.text.toString().trim()
            model.phone=spinnerCode.text.toString().trim()+ContactDetails_Phone.text.toString().trim()
            model.code=spinnerCode.text.toString().trim()
            model.user_mobile=ContactDetails_Phone.text.toString().trim()
            if (kinDetailArrayList.get(positionClicked).NewData)
            {
                model.status=0
                model.request=1
            }
            else{
                if (isKinChanged)
                {
                    model.status=1
                    model.request=0
                }
                else
                {
                    model.status=5
                    model.request=0
                }
            }
            model.created_at=kinDetailArrayList.get(positionClicked).created_at
            model.updated_at=kinDetailArrayList.get(positionClicked).updated_at
            model.NewData=kinDetailArrayList.get(positionClicked).NewData
            model.Newdata=kinDetailArrayList.get(positionClicked).Newdata
            model.isConfirmed=false

            var isFound:Boolean=false
            var pos:Int=-1
            for (i in 0..kinDetailPassArrayList.size-1)
            {
                if (kinDetailArrayList.get(positionClicked).kin_id==kinDetailPassArrayList.get(positionClicked).kin_id)
                {
                    isFound=true
                    pos=i
                }
            }
            kinDetailPassArrayList.removeAt(pos)
            kinDetailPassArrayList.add(pos,model)
            sharedprefs.getKinDetailPassArrayList(mContext)!!.clear()
            sharedprefs.setKinDetailPassArrayList(mContext,kinDetailPassArrayList)

            kinDetailArrayList.removeAt(positionClicked)
            kinDetailArrayList.add(positionClicked,model)
            sharedprefs.getKinDetailArrayList(mContext)!!.clear()
            sharedprefs.setKinDetailArrayList(mContext,kinDetailArrayList)

        }

        var relationshipData=ArrayList<String>()
        for(i in 0..relationShipArrayList.size-1)
        {
            relationshipData.add(relationshipArrayList.get(i).name)
        }
        spinnerRelationShipDialog = SpinnerDialog(activity, relationshipData, "Select RelationShip", "Close") // With No Animation
        spinnerRelationShipDialog = SpinnerDialog(activity, relationshipData, "Select RelationShip", R.style.DialogAnimations_SmileWindow, "Close") // With 	Animation
        spinnerRelationShipDialog.setCancellable(true)
        spinnerRelationShipDialog.setShowKeyboard(false)
        spinnerRelationShipDialog.bindOnSpinerListener(object : OnSpinerItemClick {
            override fun onClick(item: String, position: Int) {
                relationshipTxt.setText(item)
                isKinChanged=true
                var dataArrayList=ArrayList<KinDetailApiModel>()
                var model=KinDetailApiModel()
                model.id=kinDetailArrayList.get(positionClicked).id
                model.kin_id=kinDetailArrayList.get(positionClicked).kin_id
                model.user_id=kinDetailArrayList.get(positionClicked).user_id
                model.title=ContactDetails_Spinnertype.text.toString().trim()
                model.name=contactDetails_fname.text.toString().trim()
                model.last_name=ContactDetails_Lastname.text.toString().trim()
                model.relationship=relationshipTxt.text.toString().trim()
                model.email=ContactDetails_Email.text.toString().trim()
                model.phone=spinnerCode.text.toString().trim()+ContactDetails_Phone.text.toString().trim()
                model.code=spinnerCode.text.toString().trim()
                model.user_mobile=ContactDetails_Phone.text.toString().trim()
                if (kinDetailArrayList.get(positionClicked).NewData)
                {
                    model.status=0
                    model.request=1
                }
                else{
                    if (isKinChanged)
                    {
                        model.status=1
                        model.request=0
                    }
                    else
                    {
                        model.status=5
                        model.request=0
                    }
                }
                model.created_at=kinDetailArrayList.get(positionClicked).created_at
                model.updated_at=kinDetailArrayList.get(positionClicked).updated_at
                model.NewData=kinDetailArrayList.get(positionClicked).NewData
                model.Newdata=kinDetailArrayList.get(positionClicked).Newdata
                model.isConfirmed=false
                var isFound:Boolean=false
                var pos:Int=-1
                for (i in 0..kinDetailPassArrayList.size-1)
                {
                    if (kinDetailArrayList.get(positionClicked).kin_id==kinDetailPassArrayList.get(positionClicked).kin_id)
                    {
                        isFound=true
                        pos=i
                    }
                }
                kinDetailPassArrayList.removeAt(pos)
                kinDetailPassArrayList.add(pos,model)
                sharedprefs.getKinDetailPassArrayList(mContext)!!.clear()
                sharedprefs.setKinDetailPassArrayList(mContext,kinDetailPassArrayList)

                kinDetailArrayList.removeAt(positionClicked)
                kinDetailArrayList.add(positionClicked,model)
                sharedprefs.getKinDetailArrayList(mContext)!!.clear()
                sharedprefs.setKinDetailArrayList(mContext,kinDetailArrayList)
            }
        })
        relationShipLinear.setOnClickListener(View.OnClickListener {
            spinnerRelationShipDialog.showSpinerDialog()
        })


        if (kinDetailArrayList.get(0).relationship.equals(""))
        {
            relationshipTxt.setText(relationShipArrayList.get(0).name)
        }
        else
        {
            relationshipTxt.setText(kinDetailArrayList.get(0).relationship)
        }
        if (kinDetailArrayList.get(positionClicked).name.equals(""))
        {

        }
        else{
            contactDetails_fname.setText(kinDetailArrayList.get(positionClicked).name)
        }
        if (kinDetailArrayList.get(positionClicked).last_name.equals(""))
        {

        }
        else{
            ContactDetails_Lastname.setText(kinDetailArrayList.get(positionClicked).last_name)
        }
      if (kinDetailArrayList.get(positionClicked).email.equals(""))
        {

        }
        else{
          ContactDetails_Email.setText(kinDetailArrayList.get(positionClicked).email)
        }
        if (kinDetailArrayList.get(positionClicked).user_mobile.equals(""))
        {

        }
        else{
            ContactDetails_Phone.setText(kinDetailArrayList.get(positionClicked).user_mobile)
        }
        if (kinDetailArrayList.get(positionClicked).code.equals(""))
        {
           spinnerCode.setText("+971")
        }
        else{
            spinnerCode.setText(kinDetailArrayList.get(positionClicked).code)
        }

        contactDetails_fname.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                isKinChanged=true
                var dataArrayList=ArrayList<KinDetailApiModel>()
                var model=KinDetailApiModel()
                model.id=kinDetailArrayList.get(positionClicked).id
                model.kin_id=kinDetailArrayList.get(positionClicked).kin_id
                model.user_id=kinDetailArrayList.get(positionClicked).user_id
                model.title=ContactDetails_Spinnertype.text.toString().trim()
                model.name=contactDetails_fname.text.toString().trim()
                model.last_name=ContactDetails_Lastname.text.toString().trim()
                model.relationship=relationshipTxt.text.toString().trim()
                model.email=ContactDetails_Email.text.toString().trim()
                model.phone=spinnerCode.text.toString().trim()+ContactDetails_Phone.text.toString().trim()
                model.code=spinnerCode.text.toString().trim()
                model.user_mobile=ContactDetails_Phone.text.toString().trim()
                if (kinDetailArrayList.get(positionClicked).NewData)
                {
                    model.status=0
                    model.request=1
                }
                else{
                    if (isKinChanged)
                    {
                        model.status=1
                        model.request=0
                    }
                    else
                    {
                        model.status=5
                        model.request=0
                    }
                }
                model.created_at=kinDetailArrayList.get(positionClicked).created_at
                model.updated_at=kinDetailArrayList.get(positionClicked).updated_at
                model.NewData=kinDetailArrayList.get(positionClicked).NewData
                model.Newdata=kinDetailArrayList.get(positionClicked).Newdata
                model.isConfirmed=false
                var isFound:Boolean=false
                var pos:Int=-1
                for (i in 0..kinDetailPassArrayList.size-1)
                {
                    if (kinDetailArrayList.get(positionClicked).kin_id==kinDetailPassArrayList.get(positionClicked).kin_id)
                    {
                        isFound=true
                        pos=i
                    }
                }
                kinDetailPassArrayList.removeAt(pos)
                kinDetailPassArrayList.add(pos,model)
                sharedprefs.getKinDetailPassArrayList(mContext)!!.clear()
                sharedprefs.setKinDetailPassArrayList(mContext,kinDetailPassArrayList)

                kinDetailArrayList.removeAt(positionClicked)
                kinDetailArrayList.add(positionClicked,model)
                sharedprefs.getKinDetailArrayList(mContext)!!.clear()
                sharedprefs.setKinDetailArrayList(mContext,kinDetailArrayList)
            }

        })
        ContactDetails_Lastname.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                isKinChanged = true
                var dataArrayList=ArrayList<KinDetailApiModel>()
                var model=KinDetailApiModel()
                model.id=kinDetailArrayList.get(positionClicked).id
                model.kin_id=kinDetailArrayList.get(positionClicked).kin_id
                model.user_id=kinDetailArrayList.get(positionClicked).user_id
                model.title=ContactDetails_Spinnertype.text.toString().trim()
                model.name=contactDetails_fname.text.toString().trim()
                model.last_name=ContactDetails_Lastname.text.toString().trim()
                model.relationship=relationshipTxt.text.toString().trim()
                model.email=ContactDetails_Email.text.toString().trim()
                model.phone=spinnerCode.text.toString().trim()+ContactDetails_Phone.text.toString().trim()
                model.code=spinnerCode.text.toString().trim()
                model.user_mobile=ContactDetails_Phone.text.toString().trim()
                if (kinDetailArrayList.get(positionClicked).NewData)
                {
                    model.status=0
                    model.request=1
                }
                else{
                    if (isKinChanged)
                    {
                        model.status=1
                        model.request=0
                    }
                    else
                    {
                        model.status=5
                        model.request=0
                    }
                }
                model.created_at=kinDetailArrayList.get(positionClicked).created_at
                model.updated_at=kinDetailArrayList.get(positionClicked).updated_at
                model.NewData=kinDetailArrayList.get(positionClicked).NewData
                model.Newdata=kinDetailArrayList.get(positionClicked).Newdata
                model.isConfirmed=false
                var isFound:Boolean=false
                var pos:Int=-1
                for (i in 0..kinDetailPassArrayList.size-1)
                {
                    if (kinDetailArrayList.get(positionClicked).kin_id==kinDetailPassArrayList.get(positionClicked).kin_id)
                    {
                        isFound=true
                        pos=i
                    }
                }
                kinDetailPassArrayList.removeAt(pos)
                kinDetailPassArrayList.add(pos,model)
                sharedprefs.getKinDetailPassArrayList(mContext)!!.clear()
                sharedprefs.setKinDetailPassArrayList(mContext,kinDetailPassArrayList)

                kinDetailArrayList.removeAt(positionClicked)
                kinDetailArrayList.add(positionClicked,model)
                sharedprefs.getKinDetailArrayList(mContext)!!.clear()
                sharedprefs.setKinDetailArrayList(mContext,kinDetailArrayList)

            }
        })
        ContactDetails_Email.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                isKinChanged = true
                var dataArrayList=ArrayList<KinDetailApiModel>()
                var model=KinDetailApiModel()
                model.id=kinDetailArrayList.get(positionClicked).id
                model.kin_id=kinDetailArrayList.get(positionClicked).kin_id
                model.user_id=kinDetailArrayList.get(positionClicked).user_id
                model.title=ContactDetails_Spinnertype.text.toString().trim()
                model.name=contactDetails_fname.text.toString().trim()
                model.last_name=ContactDetails_Lastname.text.toString().trim()
                model.relationship=relationshipTxt.text.toString().trim()
                model.email=ContactDetails_Email.text.toString().trim()
                model.phone=spinnerCode.text.toString().trim()+ContactDetails_Phone.text.toString().trim()
                model.code=spinnerCode.text.toString().trim()
                model.user_mobile=ContactDetails_Phone.text.toString().trim()
                if (kinDetailArrayList.get(positionClicked).NewData)
                {
                    model.status=0
                    model.request=1
                }
                else{
                    if (isKinChanged)
                    {
                        model.status=1
                        model.request=0
                    }
                    else
                    {
                        model.status=5
                        model.request=0
                    }
                }
                model.created_at=kinDetailArrayList.get(positionClicked).created_at
                model.updated_at=kinDetailArrayList.get(positionClicked).updated_at
                model.NewData=kinDetailArrayList.get(positionClicked).NewData
                model.Newdata=kinDetailArrayList.get(positionClicked).Newdata
                model.isConfirmed=false
                var isFound:Boolean=false
                var pos:Int=-1
                for (i in 0..kinDetailPassArrayList.size-1)
                {
                    if (kinDetailArrayList.get(positionClicked).kin_id==kinDetailPassArrayList.get(positionClicked).kin_id)
                    {
                        isFound=true
                        pos=i
                    }
                }
                kinDetailPassArrayList.removeAt(pos)
                kinDetailPassArrayList.add(pos,model)
                sharedprefs.getKinDetailPassArrayList(mContext)!!.clear()
                sharedprefs.setKinDetailPassArrayList(mContext,kinDetailPassArrayList)

                kinDetailArrayList.removeAt(positionClicked)
                kinDetailArrayList.add(positionClicked,model)
                sharedprefs.getKinDetailArrayList(mContext)!!.clear()
                sharedprefs.setKinDetailArrayList(mContext,kinDetailArrayList)


            }

        })
        ContactDetails_Phone.addTextChangedListener(object : TextWatcher {
                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {

                }

                override fun beforeTextChanged(
                    s: CharSequence,
                    start: Int,
                    count: Int,
                    after: Int
                ) {

                }

                override fun afterTextChanged(s: Editable) {
                    isKinChanged = true
                    var dataArrayList=ArrayList<KinDetailApiModel>()
                    var model=KinDetailApiModel()
                    model.id=kinDetailArrayList.get(positionClicked).id
                    model.kin_id=kinDetailArrayList.get(positionClicked).kin_id
                    model.user_id=kinDetailArrayList.get(positionClicked).user_id
                    model.title=ContactDetails_Spinnertype.text.toString().trim()
                    model.name=contactDetails_fname.text.toString().trim()
                    model.last_name=ContactDetails_Lastname.text.toString().trim()
                    model.relationship=relationshipTxt.text.toString().trim()
                    model.email=ContactDetails_Email.text.toString().trim()
                    model.phone=spinnerCode.text.toString().trim()+ContactDetails_Phone.text.toString().trim()
                    model.code=spinnerCode.text.toString().trim()
                    model.user_mobile=ContactDetails_Phone.text.toString().trim()
                    if (kinDetailArrayList.get(positionClicked).NewData)
                    {
                        model.status=0
                        model.request=1
                    }
                    else{
                        if (isKinChanged)
                        {
                            model.status=1
                            model.request=0
                        }
                        else
                        {
                            model.status=5
                            model.request=0
                        }
                    }
                    model.created_at=kinDetailArrayList.get(positionClicked).created_at
                    model.updated_at=kinDetailArrayList.get(positionClicked).updated_at
                    model.NewData=kinDetailArrayList.get(positionClicked).NewData
                    model.Newdata=kinDetailArrayList.get(positionClicked).Newdata
                    model.isConfirmed=false
                    var isFound:Boolean=false
                    var pos:Int=-1
                    for (i in 0..kinDetailPassArrayList.size-1)
                    {
                        if (kinDetailArrayList.get(positionClicked).kin_id==kinDetailPassArrayList.get(positionClicked).kin_id)
                        {
                            isFound=true
                            pos=i
                        }
                    }
                    kinDetailPassArrayList.removeAt(pos)
                    kinDetailPassArrayList.add(pos,model)
                    sharedprefs.getKinDetailPassArrayList(mContext)!!.clear()
                    sharedprefs.setKinDetailPassArrayList(mContext,kinDetailPassArrayList)

                    kinDetailArrayList.removeAt(positionClicked)
                    kinDetailArrayList.add(positionClicked,model)
                    sharedprefs.getKinDetailArrayList(mContext)!!.clear()
                    sharedprefs.setKinDetailArrayList(mContext,kinDetailArrayList)


                }


        })
        spinnerCode.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                isKinChanged=true
                var dataArrayList=ArrayList<KinDetailApiModel>()
                var model=KinDetailApiModel()
                model.id=kinDetailArrayList.get(positionClicked).id
                model.kin_id=kinDetailArrayList.get(positionClicked).kin_id
                model.user_id=kinDetailArrayList.get(positionClicked).user_id
                model.title=ContactDetails_Spinnertype.text.toString().trim()
                model.name=contactDetails_fname.text.toString().trim()
                model.last_name=ContactDetails_Lastname.text.toString().trim()
                model.relationship=relationshipTxt.text.toString().trim()
                model.email=ContactDetails_Email.text.toString().trim()
                model.phone=spinnerCode.text.toString().trim()+ContactDetails_Phone.text.toString().trim()
                model.code=spinnerCode.text.toString().trim()
                model.user_mobile=ContactDetails_Phone.text.toString().trim()
                if (kinDetailArrayList.get(positionClicked).NewData)
                {
                    model.status=0
                    model.request=1
                }
                else{
                    if (isKinChanged)
                    {
                        model.status=1
                        model.request=0
                    }
                    else
                    {
                        model.status=5
                        model.request=0
                    }
                }
                model.created_at=kinDetailArrayList.get(positionClicked).created_at
                model.updated_at=kinDetailArrayList.get(positionClicked).updated_at
                model.NewData=kinDetailArrayList.get(positionClicked).NewData
                model.Newdata=kinDetailArrayList.get(positionClicked).Newdata
                model.isConfirmed=false
                var isFound:Boolean=false
                var pos:Int=-1
                for (i in 0..kinDetailPassArrayList.size-1)
                {
                    if (kinDetailArrayList.get(positionClicked).kin_id==kinDetailPassArrayList.get(positionClicked).kin_id)
                    {
                        isFound=true
                        pos=i
                    }
                }
                kinDetailPassArrayList.removeAt(pos)
                kinDetailPassArrayList.add(pos,model)
                sharedprefs.getKinDetailPassArrayList(mContext)!!.clear()
                sharedprefs.setKinDetailPassArrayList(mContext,kinDetailPassArrayList)

                kinDetailArrayList.removeAt(positionClicked)
                kinDetailArrayList.add(positionClicked,model)
                sharedprefs.getKinDetailArrayList(mContext)!!.clear()
                sharedprefs.setKinDetailArrayList(mContext,kinDetailArrayList)



            }
        })

        ContactDetails_Submit.setOnClickListener(View.OnClickListener {
            if (contactDetails_fname.text.toString().trim().equals(""))
            {
                showSuccessAlert(activity,"Please Enter the First Name","Alert")
            }
            else
            {
                if (ContactDetails_Email.text.toString().trim().equals(""))
                {
                    showSuccessAlert(activity,"Please Enter the Email ID","Alert")
                }
                else
                {
                    if (ContactDetails_Phone.text.toString().trim().equals(""))
                    {
                        showSuccessAlert(activity,"Please Enter the Phone Number","Alert")
                    }
                    else
                    {
                        if (spinnerCode.text.toString().trim().equals(""))
                        {
                            showSuccessAlert(activity,"Please Enter the PinCode","Alert")
                        }
                        else
                        {
                            var emailPattern = InternetCheckClass.isEmailValid(ContactDetails_Email.text.toString().trim())
                            if (!emailPattern)
                            {
                                showSuccessAlert(activity,"Please Enter a valid Email","Alert")
                            }
                            else
                            {
                                var dataArrayList=ArrayList<KinDetailApiModel>()
                                var model=KinDetailApiModel()
                                model.id=kinDetailArrayList.get(positionClicked).id
                                model.kin_id=kinDetailArrayList.get(positionClicked).kin_id
                                model.user_id=kinDetailArrayList.get(positionClicked).user_id
                                model.title=ContactDetails_Spinnertype.text.toString().trim()
                                model.name=contactDetails_fname.text.toString().trim()
                                model.last_name=ContactDetails_Lastname.text.toString().trim()
                                model.relationship=relationshipTxt.text.toString().trim()
                                model.email=ContactDetails_Email.text.toString().trim()
                                model.phone=spinnerCode.text.toString().trim()+ContactDetails_Phone.text.toString().trim()
                                model.code=spinnerCode.text.toString().trim()
                                model.user_mobile=ContactDetails_Phone.text.toString().trim()
                                if (kinDetailArrayList.get(positionClicked).NewData)
                                {
                                    model.status=0
                                    model.request=1
                                }
                                else{
                                    if (isKinChanged)
                                    {
                                        model.status=1
                                        model.request=0
                                    }
                                    else
                                    {
                                        model.status=5
                                        model.request=0
                                    }
                                }
                                model.created_at=kinDetailArrayList.get(positionClicked).created_at
                                model.updated_at=kinDetailArrayList.get(positionClicked).updated_at
                                model.NewData=kinDetailArrayList.get(positionClicked).NewData
                                model.Newdata=kinDetailArrayList.get(positionClicked).Newdata
                                model.isConfirmed=true

                                var isFound:Boolean=false
                                var pos:Int=-1
                                for (i in 0..kinDetailPassArrayList.size-1)
                                {
                                    if (kinDetailArrayList.get(positionClicked).kin_id==kinDetailPassArrayList.get(positionClicked).kin_id)
                                    {
                                        isFound=true
                                        pos=i
                                    }
                                }
                                kinDetailPassArrayList.removeAt(pos)
                                kinDetailPassArrayList.add(pos,model)
                                sharedprefs.getKinDetailPassArrayList(mContext)!!.clear()
                                sharedprefs.setKinDetailPassArrayList(mContext,kinDetailPassArrayList)
                                kinDetailArrayList.removeAt(positionClicked)
                                kinDetailArrayList.add(positionClicked,model)
                                sharedprefs.getKinDetailArrayList(mContext)!!.clear()
                                sharedprefs.setKinDetailArrayList(mContext,kinDetailArrayList)
                                familyKinRecyclerAdapter= FamilyContactRecyclerAdapter(sharedprefs.getKinDetailArrayList(mContext)!!)
                                familyContactRecycler.setAdapter(familyKinRecyclerAdapter)
                                dialog.dismiss()

                            }
                        }

                    }
                }
            }

        })

        remove_kin.setOnClickListener(View.OnClickListener {

         if(kinDetailArrayList.size==1)
         {
             showSuccessAlert(activity,"Contact cannot be deleted,there must be at least one family contact associated with your family","Alert")
         }
            else
         {
             val deleteDialog = Dialog(mContext)
             deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
             deleteDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
             deleteDialog.setCancelable(false)
             deleteDialog.setContentView(R.layout.dialogue_discard_data)
             var iconImageView = deleteDialog.findViewById(R.id.iconImageView) as ImageView
             var alertHead = deleteDialog.findViewById(R.id.alertHead) as TextView
             var text_dialog = deleteDialog.findViewById(R.id.text_dialog) as TextView
             var btn_Ok = deleteDialog.findViewById(R.id.btn_Ok) as Button
             var btn_Cancel = deleteDialog.findViewById(R.id.btn_Cancel) as Button
             text_dialog.text = "Are you sure you want to delete this Contact?"
             alertHead.text = "Confirm?"
             iconImageView.setImageResource(R.drawable.exclamationicon)
             btn_Cancel?.setOnClickListener()
             {
                 deleteDialog.dismiss()

             }
             btn_Ok?.setOnClickListener()
             {
                 var kinPos:Int=-1

                 if(kinDetailArrayList.get(positionClicked).NewData)
                 {
                     for (i in 0..kinDetailPassArrayList.size-1)
                     {
                         if(kinDetailArrayList.get(positionClicked).kin_id==kinDetailPassArrayList.get(i).kin_id)
                         {
                             kinPos=i
                         }
                     }

                     kinDetailArrayList.removeAt(positionClicked)
                     sharedprefs.getKinDetailArrayList(mContext)!!.clear()
                     sharedprefs.setKinDetailArrayList(mContext,kinDetailArrayList)
                     kinDetailPassArrayList.removeAt(kinPos)
                     sharedprefs.getKinDetailPassArrayList(mContext)!!.clear()
                     sharedprefs.setKinDetailPassArrayList(mContext,kinDetailPassArrayList)
                     deleteDialog.dismiss()
                     dialog.dismiss()
                 }
                 else
                 {
                     for (i in 0..kinDetailPassArrayList.size-1)
                     {
                         if(kinDetailArrayList.get(positionClicked).kin_id==kinDetailPassArrayList.get(i).kin_id)
                         {
                             kinPos=i
                         }
                     }
                     var model=KinDetailApiModel()
                     model.id=kinDetailArrayList.get(kinPos).id
                     model.kin_id=kinDetailArrayList.get(kinPos).kin_id
                     model.user_id=kinDetailArrayList.get(kinPos).user_id
                     model.title=ContactDetails_Spinnertype.text.toString().trim()
                     model.name=contactDetails_fname.text.toString().trim()
                     model.last_name=ContactDetails_Lastname.text.toString().trim()
                     model.relationship=relationshipTxt.text.toString().trim()
                     model.email=ContactDetails_Email.text.toString().trim()
                     model.phone=spinnerCode.text.toString().trim()+ContactDetails_Phone.text.toString().trim()
                     model.code=spinnerCode.text.toString().trim()
                     model.user_mobile=ContactDetails_Phone.text.toString().trim()
                     model.status=2
                     model.request=0
                     model.created_at=kinDetailArrayList.get(kinPos).created_at
                     model.updated_at=kinDetailArrayList.get(kinPos).updated_at
                     model.NewData=kinDetailArrayList.get(kinPos).NewData
                     model.Newdata=kinDetailArrayList.get(kinPos).Newdata
                     model.isConfirmed=true
                     kinDetailArrayList.removeAt(positionClicked)
                     kinDetailPassArrayList.removeAt(kinPos)
                     kinDetailPassArrayList.add(kinPos,model)
                     sharedprefs.getKinDetailPassArrayList(mContext)!!.clear()
                     sharedprefs.getKinDetailArrayList(mContext)!!.clear()
                     sharedprefs.setKinDetailArrayList(mContext,kinDetailArrayList)
                     sharedprefs.setKinDetailPassArrayList(mContext,kinDetailPassArrayList)
                     familyKinRecyclerAdapter= FamilyContactRecyclerAdapter(sharedprefs.getKinDetailArrayList(mContext)!!)
                     familyContactRecycler.setAdapter(familyKinRecyclerAdapter)
                     deleteDialog.dismiss()
                     dialog.dismiss()

                 }

             }
             deleteDialog.show()
         }





        })

        //imageView4
        imageView4.setOnClickListener(View.OnClickListener {

           if(isKinChanged)
           {
               var dataArrayList=ArrayList<KinDetailApiModel>()
               var model=KinDetailApiModel()
               model.id=kinDetailArrayList.get(positionClicked).id
               model.kin_id=kinDetailArrayList.get(positionClicked).kin_id
               model.user_id=kinDetailArrayList.get(positionClicked).user_id
               model.title=ContactDetails_Spinnertype.text.toString().trim()
               model.name=contactDetails_fname.text.toString().trim()
               model.last_name=ContactDetails_Lastname.text.toString().trim()
               model.relationship=relationshipTxt.text.toString().trim()
               model.email=ContactDetails_Email.text.toString().trim()
               model.phone=spinnerCode.text.toString().trim()+ContactDetails_Phone.text.toString().trim()
               model.code=spinnerCode.text.toString().trim()
               model.user_mobile=ContactDetails_Phone.text.toString().trim()
               if (kinDetailArrayList.get(positionClicked).NewData)
               {
                   model.status=0
                   model.request=1
               }
               else{
                   if (isKinChanged)
                   {
                       model.status=1
                       model.request=0
                   }
                   else
                   {
                       model.status=5
                       model.request=0
                   }
               }
               model.created_at=kinDetailArrayList.get(positionClicked).created_at
               model.updated_at=kinDetailArrayList.get(positionClicked).updated_at
               model.NewData=kinDetailArrayList.get(positionClicked).NewData
               model.Newdata=kinDetailArrayList.get(positionClicked).Newdata
               model.isConfirmed=false
               kinDetailArrayList.removeAt(positionClicked)
               kinDetailArrayList.add(positionClicked,model)
               kinDetailPassArrayList.removeAt(positionClicked)
               kinDetailPassArrayList.add(positionClicked,model)
               sharedprefs.getKinDetailPassArrayList(mContext)!!.clear()
               sharedprefs.getKinDetailArrayList(mContext)!!.clear()
               sharedprefs.setKinDetailArrayList(mContext,kinDetailArrayList)
               sharedprefs.setKinDetailPassArrayList(mContext,kinDetailPassArrayList)
               familyKinRecyclerAdapter= FamilyContactRecyclerAdapter(sharedprefs.getKinDetailArrayList(mContext)!!)
               familyContactRecycler.setAdapter(familyKinRecyclerAdapter)

           }
            dialog.dismiss()
        })
        dialog.show()
    }



    fun showKinDetailAdd(activity: FragmentActivity)
    {
        var isKinChanged:Boolean=false
        val dialog = Dialog(activity)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialoge_kin_detail)
        var ContactDetails_Spinnertype=dialog.findViewById(R.id.ContactDetails_Spinnertype)as AutoCompleteTextView
        var kinDetailArrayList=ArrayList<KinDetailApiModel>()
        var kinDetailPassArrayList=ArrayList<KinDetailApiModel>()
        kinDetailArrayList=sharedprefs.getKinDetailArrayList(activity)!!
        kinDetailPassArrayList=sharedprefs.getKinDetailArrayList(activity)!!
        var titleArrayList=ArrayList<TitlesArrayList>()
        titleArrayList=sharedprefs.getTitleArrayList(activity)
        var SpinnerData=ArrayList<String>()
        for (i in 0..titleArrayList.size-1)
        {
            SpinnerData.add(titleArrayList.get(i).name)
        }
        ContactDetails_Spinnertype.setText(SpinnerData.get(0).toString())
        val DROP = ArrayAdapter(mContext, android.R.layout.simple_list_item_1, SpinnerData)
        ContactDetails_Spinnertype.isCursorVisible = false
        ContactDetails_Spinnertype.isFocusable = false
        ContactDetails_Spinnertype.setAdapter(DROP)
        ContactDetails_Spinnertype.setOnClickListener {
            ContactDetails_Spinnertype.showDropDown()
            isKinChanged=true
        }
        var contactDetails_fname=dialog.findViewById(R.id.contactDetails_fname)as EditText
        var ContactDetails_Lastname=dialog.findViewById(R.id.ContactDetails_Lastname)as EditText
        var ContactDetails_Email=dialog.findViewById(R.id.ContactDetails_Email)as EditText
        var ContactDetails_Phone=dialog.findViewById(R.id.ContactDetails_Phone)as EditText
        var communicationPreferenceLinear=dialog.findViewById(R.id.communicationPreferenceLinear)as LinearLayout
        communicationPreferenceLinear.visibility=View.VISIBLE
        var spinnerCode=dialog.findViewById(R.id.spinnerCode)as EditText
        contactDetails_fname.setImeOptions(EditorInfo.IME_ACTION_DONE)
        contactDetails_fname.isFocusable=true
        contactDetails_fname.isFocusableInTouchMode=true
        ContactDetails_Lastname.setImeOptions(EditorInfo.IME_ACTION_DONE)
        ContactDetails_Lastname.isFocusable=true
        ContactDetails_Lastname.isFocusableInTouchMode=true
        ContactDetails_Email.setImeOptions(EditorInfo.IME_ACTION_DONE)
        ContactDetails_Email.isFocusable=true
        ContactDetails_Email.isFocusableInTouchMode=true
        ContactDetails_Phone.setImeOptions(EditorInfo.IME_ACTION_DONE)
        ContactDetails_Phone.isFocusable=true
        ContactDetails_Phone.isFocusableInTouchMode=true
        spinnerCode.setImeOptions(EditorInfo.IME_ACTION_DONE)
        spinnerCode.isFocusable=true
        spinnerCode.isFocusableInTouchMode=true
        spinnerCode.setText("+971")
        var relationShipLinear=dialog.findViewById(R.id.relationShipLinear)as LinearLayout
        var spinnerRelationShipDialog:SpinnerDialog

        var relationShipArrayList=ArrayList<RelationShipDetailModel>()
        relationShipArrayList=sharedprefs.getRelationShipArrayList(mContext)

        var relationshipData=ArrayList<String>()
        for(i in 0..relationShipArrayList.size-1)
        {
            if(!relationshipArrayList.get(i).name.equals(""))
            {
                relationshipData.add(relationshipArrayList.get(i).name)
            }

        }
        var relationshipTxt=dialog.findViewById(R.id.relationshipTxt)as TextView
        relationshipTxt.setText(relationShipArrayList.get(0).name)
        spinnerRelationShipDialog = SpinnerDialog(activity, relationshipData, "Select RelationShip", "Close") // With No Animation
        spinnerRelationShipDialog = SpinnerDialog(activity, relationshipData, "Select RelationShip", R.style.DialogAnimations_SmileWindow, "Close") // With 	Animation
        spinnerRelationShipDialog.setCancellable(true)
        spinnerRelationShipDialog.setShowKeyboard(false)
        spinnerRelationShipDialog.bindOnSpinerListener(object : OnSpinerItemClick {
            override fun onClick(item: String, position: Int) {
                relationshipTxt.setText(item)
                isKinChanged=true


            }
        })
        val removekinImg = dialog.findViewById<ImageView>(R.id.remove_kin)
        removekinImg.visibility = View.GONE
        relationShipLinear.setOnClickListener(View.OnClickListener {
            spinnerRelationShipDialog.showSpinerDialog()
        })
        var ContactDetails_Submit=dialog.findViewById(R.id.ContactDetails_Submit)as Button
        ContactDetails_Submit.setOnClickListener(View.OnClickListener {

            if (contactDetails_fname.text.toString().trim().equals(""))
            {
                showSuccessAlert(activity,"Please Enter the First Name","Alert")
            }
            else
            {
                if (ContactDetails_Email.text.toString().trim().equals(""))
                {
                    showSuccessAlert(activity,"Please Enter the Email ID","Alert")
                }
                else
                {
                    if (ContactDetails_Phone.text.toString().trim().equals(""))
                    {
                        showSuccessAlert(activity,"Please Enter the Phone Number","Alert")
                    }
                    else
                    {
                        if (spinnerCode.text.toString().trim().equals(""))
                        {
                            showSuccessAlert(activity,"Please Enter the Country Code","Alert")
                        }
                        else
                        {
                            var emailPattern = InternetCheckClass.isEmailValid(ContactDetails_Email.text.toString().trim())
                            if (!emailPattern)
                            {
                                showSuccessAlert(activity,"Please Enter a valid Email","Alert")
                            }
                            else
                            {
                                var dataArrayList=ArrayList<KinDetailApiModel>()
                                var model=KinDetailApiModel()
                                model.id=0
                                model.kin_id=0
                                model.user_id=0
                                model.title=ContactDetails_Spinnertype.text.toString().trim()
                                model.name=contactDetails_fname.text.toString().trim()
                                model.last_name=ContactDetails_Lastname.text.toString().trim()
                                model.relationship=relationshipTxt.text.toString().trim()
                                model.email=ContactDetails_Email.text.toString().trim()
                                model.phone=spinnerCode.text.toString().trim()+ContactDetails_Phone.text.toString().trim()
                                model.code=spinnerCode.text.toString().trim()
                                model.user_mobile=ContactDetails_Phone.text.toString().trim()
                                model.status=0
                                model.request=1
                                model.created_at=""
                                model.updated_at=""
                                model.NewData=true
                                model.Newdata="1"
                                model.isConfirmed=true
                                kinDetailArrayList.add(model)
                                kinDetailPassArrayList.add(model)
                                sharedprefs.getKinDetailPassArrayList(mContext)!!.clear()
                                sharedprefs.getKinDetailArrayList(mContext)!!.clear()
                                sharedprefs.setKinDetailArrayList(mContext,kinDetailArrayList)
                                sharedprefs.setKinDetailPassArrayList(mContext,kinDetailPassArrayList)
                                familyKinRecyclerAdapter= FamilyContactRecyclerAdapter(sharedprefs.getKinDetailArrayList(mContext)!!)
                                familyContactRecycler.setAdapter(familyKinRecyclerAdapter)
                                dialog.dismiss()

                            }

                        }
                    }
                }
            }

        })
        var imageView4=dialog.findViewById(R.id.imageView4)as ImageView
        imageView4.setOnClickListener(View.OnClickListener {

            val deleteDialog = Dialog(mContext)
            deleteDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            deleteDialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            deleteDialog.setCancelable(false)
            deleteDialog.setContentView(R.layout.dialogue_discard_data)
            var iconImageView = deleteDialog.findViewById(R.id.iconImageView) as ImageView
            var alertHead = deleteDialog.findViewById(R.id.alertHead) as TextView
            var text_dialog = deleteDialog.findViewById(R.id.text_dialog) as TextView
            var btn_Ok = deleteDialog.findViewById(R.id.btn_Ok) as Button
            var btn_Cancel = deleteDialog.findViewById(R.id.btn_Cancel) as Button
            text_dialog.text = "Do you want to Discard changes?"
            alertHead.text = "Confirm?"
            iconImageView.setImageResource(R.drawable.exclamationicon)
            btn_Cancel?.setOnClickListener()
            {
                deleteDialog.show()

            }
            btn_Ok?.setOnClickListener()
            {

                deleteDialog.dismiss()
                dialog.dismiss()
            }
            deleteDialog.show()
        })

        dialog.show()
    }
}




