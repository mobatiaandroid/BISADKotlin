package com.mobatia.bisad.activity.home

import `in`.galaxyofandroid.spinerdialog.OnSpinerItemClick
import `in`.galaxyofandroid.spinerdialog.SpinnerDialog
import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.home.model.CountryiesDetailModel
import com.mobatia.bisad.fragment.home.model.datacollection.PassportApiModel
import com.mobatia.bisad.manager.PreferenceData
import id.zelory.compressor.Compressor
import id.zelory.compressor.FileUtil
import pub.devrel.easypermissions.EasyPermissions
import java.io.*
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class PassportEmiratesScreen(studentID:String,studentImage:String,studentName:String,uniqueID:String) : Fragment(){

    lateinit var jsonConstans: JsonConstants
    lateinit var sharedprefs: PreferenceData
    lateinit var studentNameTxt: TextView
    lateinit var passportNationalityTxt: TextView
    lateinit var myCalendar :Calendar
    lateinit var PassImageName: TextView
    lateinit var uploadPassportTxt: TextView
    lateinit var VisaImageName: TextView
    lateinit var uploadVisa: TextView
    lateinit var passportNumberTxt: EditText
    lateinit var visaPermitNumberTxt: EditText
    lateinit var passportExpiryTxt: EditText
    lateinit var passportLinear: LinearLayout
    lateinit var closeImg : ImageView
    private val PICK_IMAGE_REQUEST = 1
    private val CLICK_IMAGE_REQUEST = 2
    var fromDate: String=""
    var toDate: String =""
    private val VISA_CAMERA_REQUEST = 3
    private val VISA_GALLERY_REQUEST = 4

    lateinit var passport_image_name_path:String
    lateinit var visa_image_name_path:String
     var passportImageData:String=""
     var visaImageData:String=""
    lateinit var passport_image_path:String
    lateinit var passportImgPath:String
    lateinit var visa_image_path:String
    lateinit var imagicon: ImageView
    lateinit var ViewSelectedVisa: ImageView
    lateinit var ViewSelectedPassport: ImageView
    lateinit var mContext: Context
    var foundPosition:Int=-1
    var isChanged:Boolean=false
    lateinit var spinnerDialog:SpinnerDialog
    lateinit var actualImage:File
    lateinit var compressedImage:File
    lateinit var PassportCamera:File
    lateinit var CompressPassportCamera:File
    lateinit var CompressVisaCamera1:File
    lateinit var VisacompressedImage:File
    lateinit var VisaCamera:File
    lateinit var VisaactualImage:File
    lateinit var countryArrayList:ArrayList<CountryiesDetailModel>
    lateinit var passportDetailArrayList:ArrayList<PassportApiModel>
    var galleryPermissions = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.CAMERA
    )
    var pictureImagePath :String= ""
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_passport_detail, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val builder: StrictMode.VmPolicy.Builder = StrictMode.VmPolicy.Builder()
        StrictMode.setVmPolicy(builder.build())
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        passportDetailArrayList=ArrayList()
        myCalendar= Calendar.getInstance()
        passportDetailArrayList=sharedprefs.getPassportDetailArrayList(mContext)!!
        if (EasyPermissions.hasPermissions(mContext, galleryPermissions.toString())) {
//            Toast.makeText(mContext, "permisssion granted", Toast.LENGTH_SHORT).show();
        } else {
            EasyPermissions.requestPermissions(this,"Access for storage",101,galleryPermissions.toString())

        }
        initializeUI()

    }

    private fun initializeUI() {
        imagicon = view!!.findViewById(R.id.imagicon)
        studentNameTxt = view!!.findViewById(R.id.studentName)
        passportLinear = view!!.findViewById(R.id.passportLinear)
        closeImg = view!!.findViewById(R.id.closeImg)
        passportNumberTxt = view!!.findViewById(R.id.passportNumberTxt)
        passportExpiryTxt = view!!.findViewById(R.id.passportExpiryTxt)
        passportNationalityTxt = view!!.findViewById(R.id.passportNationalityTxt)
        uploadPassportTxt = view!!.findViewById(R.id.uploadPassportTxt)
        PassImageName = view!!.findViewById(R.id.PassImageName)
        visaPermitNumberTxt = view!!.findViewById(R.id.visaPermitNumberTxt)
        ViewSelectedPassport = view!!.findViewById(R.id.ViewSelectedPassport)
        VisaImageName = view!!.findViewById(R.id.VisaImageName)
        ViewSelectedVisa = view!!.findViewById(R.id.ViewSelectedVisa)
        uploadVisa = view!!.findViewById(R.id.uploadVisa)
        var isFound: Boolean = false

        for (i in 0..passportDetailArrayList.size - 1) {
            if (uniqueID.equals(passportDetailArrayList.get(i).student_unique_id)) {
                isFound = true
                foundPosition = i

            }
        }


        val date =
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth -> // TODO Auto-generated method stub
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                fromDate = year.toString() + "-" + (monthOfYear + 1) + "-" + dayOfMonth
                updateLabel()



            }

        passportExpiryTxt.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                DatePickerDialog(
                    mContext, date, myCalendar[Calendar.YEAR], myCalendar[Calendar.MONTH],
                    myCalendar[Calendar.DAY_OF_MONTH]
                ).show()
            }
        })
        if (passportDetailArrayList.get(foundPosition).passport_image_name.equals("")) {

            PassImageName.text = passportDetailArrayList.get(foundPosition).passport_image_name
        }
        else {
            PassImageName.text = passportDetailArrayList.get(foundPosition).passport_image_name
        }
        if (passportDetailArrayList.get(foundPosition).emirates_id_image_name.equals("")) {

            VisaImageName.text = passportDetailArrayList.get(foundPosition).emirates_id_image_name
        }
        else {
            VisaImageName.text = passportDetailArrayList.get(foundPosition).emirates_id_image_name
        }
        if (passportDetailArrayList.get(foundPosition).emirates_id_image_path.equals("")) {

            //ViewSelectedVisa.setText(passportDetailArrayList.get(foundPosition).emirates_id_image_name)
        }
        else {
            ViewSelectedVisa.setImageBitmap(BitmapFactory.decodeFile(passportDetailArrayList.get(foundPosition).emirates_id_image_path))
        }
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
      //  studentNameTxt.setText(studentName)
        studentNameTxt.text = passportDetailArrayList.get(foundPosition).student_name
        countryArrayList= ArrayList()
        countryArrayList=sharedprefs.getCountryArrayList(mContext)
        var countryData=ArrayList<String>()
        for (i in 0..countryArrayList.size-1)
        {
            countryData.add(countryArrayList.get(i).name)
        }

       if (passportDetailArrayList.get(foundPosition).nationality.equals(""))
       {
           passportNationalityTxt.text = countryData.get(0).toString()
       }
        else
       {
           Log.e("Nationality",passportDetailArrayList.get(foundPosition).nationality)
           passportNationalityTxt.text = passportDetailArrayList.get(foundPosition).nationality
       }
        spinnerDialog = SpinnerDialog(activity, countryData, "Select Country", "Close") // With No Animation
        spinnerDialog = SpinnerDialog(activity, countryData, "Select Country", R.style.DialogAnimations_SmileWindow, "Close") // With 	Animation
        spinnerDialog.setCancellable(true)
        spinnerDialog.setShowKeyboard(false)
        spinnerDialog.bindOnSpinerListener(object : OnSpinerItemClick {
            override fun onClick(item: String, position: Int) {
                passportNationalityTxt.text = item
                isChanged=true
                var passportID=passportDetailArrayList.get(foundPosition).id
                var model=PassportApiModel()
                 model.id=passportDetailArrayList.get(foundPosition).id
                model.student_unique_id= passportDetailArrayList.get(foundPosition).student_unique_id
                model.student_id= passportDetailArrayList.get(foundPosition).student_id
                model.student_name= passportDetailArrayList.get(foundPosition).student_name
                model.passport_number= passportDetailArrayList.get(foundPosition).passport_number
                model.nationality= passportNationalityTxt.text.toString().trim()
                model.passport_image= passportDetailArrayList.get(foundPosition).passport_image
                model.date_of_issue= passportDetailArrayList.get(foundPosition).date_of_issue
                if ( passportExpiryTxt.text.toString().trim().equals(""))
                {
                    model.expiry_date= passportExpiryTxt.text.toString().trim()
                }
                else{
                    val fromdate=passportExpiryTxt.text.toString().trim()
                    //  val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val inputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
                    //  val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
                    val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val inputDateStr = fromdate
                    val date: Date = inputFormat.parse(inputDateStr)
                    val outputDateStr: String = outputFormat.format(date)
                    model.expiry_date=outputDateStr
                }
                model.passport_expired= passportDetailArrayList.get(foundPosition).passport_expired
                model.emirates_id_no= passportDetailArrayList.get(foundPosition).emirates_id_no
                model.emirates_id_image= passportDetailArrayList.get(foundPosition).emirates_id_image
                if (passportID==0)
                {
                    model.status= 0
                    model.request=1
                }
                else{
                    model.status= 1
                    model.request= 0
                }
                model.passport_image_name= passportDetailArrayList.get(foundPosition).passport_image_name
                model.passport_image_path= passportDetailArrayList.get(foundPosition).passport_image_path
                model.emirates_id_image_name=passportDetailArrayList.get(foundPosition).emirates_id_image
                model.emirates_id_image_path=passportDetailArrayList.get(foundPosition).emirates_id_image_path
                model.created_at= passportDetailArrayList.get(foundPosition).created_at
                model.updated_at= passportDetailArrayList.get(foundPosition).updated_at
                model.is_date_changed=passportDetailArrayList.get(foundPosition).is_date_changed
                passportDetailArrayList.removeAt(foundPosition)
                passportDetailArrayList.add(foundPosition,model)
                var passportDummy=ArrayList<PassportApiModel>()
                sharedprefs.setPassportDetailArrayList(mContext,passportDummy)
                sharedprefs.setPassportDetailArrayList(mContext,passportDetailArrayList)
            }
        })

        passportLinear.setOnClickListener(View.OnClickListener {
            spinnerDialog.showSpinerDialog()
            isChanged=true

        })

        closeImg.setOnClickListener {
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
        }


        passportNumberTxt.imeOptions = EditorInfo.IME_ACTION_DONE
        passportNumberTxt.isFocusable=true
        passportNumberTxt.isFocusableInTouchMode=true
        if(passportDetailArrayList.get(foundPosition).passport_number.equals(""))
        {

        }
        else{
            passportNumberTxt.setText(passportDetailArrayList.get(foundPosition).passport_number)
        }
        passportNumberTxt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                Log.e("EDITABLE","IT WORKS")
                var passportID=passportDetailArrayList.get(foundPosition).id
                var model=PassportApiModel()
                model.id=passportDetailArrayList.get(foundPosition).id
                model.student_unique_id= passportDetailArrayList.get(foundPosition).student_unique_id
                model.student_id= passportDetailArrayList.get(foundPosition).student_id
                model.student_name= passportDetailArrayList.get(foundPosition).student_name
                model.passport_number= passportNumberTxt.text.toString().trim()
                model.nationality= passportNationalityTxt.text.toString().trim()
                model.passport_image= passportDetailArrayList.get(foundPosition).passport_image
                model.date_of_issue= passportDetailArrayList.get(foundPosition).date_of_issue
                model.is_date_changed=passportDetailArrayList.get(foundPosition).is_date_changed
                if ( passportExpiryTxt.text.toString().trim().equals(""))
                {
                    model.expiry_date= passportExpiryTxt.text.toString().trim()
                }
                else{
                    val fromdate=passportExpiryTxt.text.toString().trim()
                    //  val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val inputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
                    //  val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
                    val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val inputDateStr = fromdate
                    val date: Date = inputFormat.parse(inputDateStr)
                    val outputDateStr: String = outputFormat.format(date)
                    model.expiry_date=outputDateStr
                }
                model.passport_expired= passportDetailArrayList.get(foundPosition).passport_expired
                model.emirates_id_no= passportDetailArrayList.get(foundPosition).emirates_id_no
                model.emirates_id_image= passportDetailArrayList.get(foundPosition).emirates_id_image
                if (passportID==0)
                {
                    model.status= 0
                    model.request=1
                }
                else{
                    model.status= 1
                    model.request= 0
                }
                model.passport_image_name= passportDetailArrayList.get(foundPosition).passport_image_name
                model.passport_image_path= passportDetailArrayList.get(foundPosition).passport_image_path
                model.emirates_id_image_name=passportDetailArrayList.get(foundPosition).emirates_id_image
                model.emirates_id_image_path=passportDetailArrayList.get(foundPosition).emirates_id_image_path
                model.created_at= passportDetailArrayList.get(foundPosition).created_at
                model.updated_at= passportDetailArrayList.get(foundPosition).updated_at
                passportDetailArrayList.removeAt(foundPosition)
                passportDetailArrayList.add(foundPosition,model)
                Log.e("passport number",passportDetailArrayList.get(foundPosition).passport_number)
                sharedprefs.getPassportDetailArrayList(mContext)!!.clear()
                var passportDummy=ArrayList<PassportApiModel>()
                sharedprefs.setPassportDetailArrayList(mContext,passportDummy)
                sharedprefs.setPassportDetailArrayList(mContext,passportDetailArrayList)
            }
        })

//        passportExpiryTxt.setImeOptions(EditorInfo.IME_ACTION_DONE)
//        passportExpiryTxt.isFocusable=true
//        passportExpiryTxt.isFocusableInTouchMode=true
        if(passportDetailArrayList.get(foundPosition).expiry_date.equals(""))
        {

        }
        else{
            val fromdate=passportDetailArrayList.get(foundPosition).expiry_date
            val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
            val inputDateStr = fromdate
            val date: Date = inputFormat.parse(inputDateStr)
            val outputDateStr: String = outputFormat.format(date)
            passportExpiryTxt.setText(outputDateStr)
            //passportExpiryTxt.setText(passportDetailArrayList.get(foundPosition).expiry_date)
        }
        passportExpiryTxt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                Log.e("EDITABLE","IT WORKS")
                var passportID=passportDetailArrayList.get(foundPosition).id
                var model=PassportApiModel()
                model.id=passportDetailArrayList.get(foundPosition).id
                model.student_unique_id= passportDetailArrayList.get(foundPosition).student_unique_id
                model.student_id= passportDetailArrayList.get(foundPosition).student_id
                model.student_name= passportDetailArrayList.get(foundPosition).student_name
                model.passport_number= passportNumberTxt.text.toString().trim()
                model.nationality= passportNationalityTxt.text.toString().trim()
                model.passport_image= passportDetailArrayList.get(foundPosition).passport_image
                model.date_of_issue= passportDetailArrayList.get(foundPosition).date_of_issue
                model.is_date_changed=passportDetailArrayList.get(foundPosition).is_date_changed
                if ( passportExpiryTxt.text.toString().trim().equals(""))
                {
                    model.expiry_date= passportExpiryTxt.text.toString().trim()
                }
                else{
                    val fromdate=passportExpiryTxt.text.toString().trim()
                    //  val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val inputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
                    //  val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
                    val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val inputDateStr = fromdate
                    val date: Date = inputFormat.parse(inputDateStr)
                    val outputDateStr: String = outputFormat.format(date)
                    model.expiry_date=outputDateStr
                }
                model.passport_expired= passportDetailArrayList.get(foundPosition).passport_expired
                model.emirates_id_no= passportDetailArrayList.get(foundPosition).emirates_id_no
                model.emirates_id_image= passportDetailArrayList.get(foundPosition).emirates_id_image
                if (passportID==0)
                {
                    model.status= 0
                    model.request=1
                }
                else{
                    model.status= 1
                    model.request= 0
                }
                model.passport_image_name= passportDetailArrayList.get(foundPosition).passport_image_name
                model.passport_image_path= passportDetailArrayList.get(foundPosition).passport_image_path
                model.emirates_id_image_name=passportDetailArrayList.get(foundPosition).emirates_id_image
                model.emirates_id_image_path=passportDetailArrayList.get(foundPosition).emirates_id_image_path
                model.created_at= passportDetailArrayList.get(foundPosition).created_at
                model.updated_at= passportDetailArrayList.get(foundPosition).updated_at
                passportDetailArrayList.removeAt(foundPosition)
                passportDetailArrayList.add(foundPosition,model)
                Log.e("passport number",passportDetailArrayList.get(foundPosition).passport_number)
                sharedprefs.getPassportDetailArrayList(mContext)!!.clear()
                var passportDummy=ArrayList<PassportApiModel>()
                sharedprefs.setPassportDetailArrayList(mContext,passportDummy)
                sharedprefs.setPassportDetailArrayList(mContext,passportDetailArrayList)
                isChanged=true
            }
        })
        visaPermitNumberTxt.imeOptions = EditorInfo.IME_ACTION_DONE
        visaPermitNumberTxt.isFocusable=true
        visaPermitNumberTxt.isFocusableInTouchMode=true
        if(passportDetailArrayList.get(foundPosition).emirates_id_no.equals(""))
        {

        }
        else{
            visaPermitNumberTxt.setText(passportDetailArrayList.get(foundPosition).emirates_id_no)
        }
        visaPermitNumberTxt.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int)
            {

            }

            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int)
            {

            }

            override fun afterTextChanged(s: Editable) {
                Log.e("EDITABLE","IT WORKS")
                var passportID=passportDetailArrayList.get(foundPosition).id
                var model=PassportApiModel()
                model.id=passportDetailArrayList.get(foundPosition).id
                model.student_unique_id= passportDetailArrayList.get(foundPosition).student_unique_id
                model.student_id= passportDetailArrayList.get(foundPosition).student_id
                model.student_name= passportDetailArrayList.get(foundPosition).student_name
                model.passport_number= passportNumberTxt.text.toString().trim()
                model.nationality= passportNationalityTxt.text.toString().trim()
                model.passport_image= passportDetailArrayList.get(foundPosition).passport_image
                model.date_of_issue= passportDetailArrayList.get(foundPosition).date_of_issue
                if ( passportExpiryTxt.text.toString().trim().equals(""))
                {
                    model.expiry_date= passportExpiryTxt.text.toString().trim()
                }
                else{
                    val fromdate=passportExpiryTxt.text.toString().trim()
                    //  val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val inputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
                    //  val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
                    val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                    val inputDateStr = fromdate
                    val date: Date = inputFormat.parse(inputDateStr)
                    val outputDateStr: String = outputFormat.format(date)
                    model.expiry_date=outputDateStr
                }
                model.passport_expired= passportDetailArrayList.get(foundPosition).passport_expired
                model.emirates_id_no= visaPermitNumberTxt.text.toString().trim()
                model.emirates_id_image= passportDetailArrayList.get(foundPosition).emirates_id_image
                model.is_date_changed=passportDetailArrayList.get(foundPosition).is_date_changed
                if (passportID==0)
                {
                    model.status= 0
                    model.request=1
                }
                else{
                    model.status= 1
                    model.request= 0
                }
                model.passport_image_name= passportDetailArrayList.get(foundPosition).passport_image_name
                model.passport_image_path= passportDetailArrayList.get(foundPosition).passport_image_path
                model.emirates_id_image_name=passportDetailArrayList.get(foundPosition).emirates_id_image
                model.emirates_id_image_path=passportDetailArrayList.get(foundPosition).emirates_id_image_path
                model.created_at= passportDetailArrayList.get(foundPosition).created_at
                model.updated_at= passportDetailArrayList.get(foundPosition).updated_at
                passportDetailArrayList.removeAt(foundPosition)
                passportDetailArrayList.add(foundPosition,model)
                Log.e("passport number",passportDetailArrayList.get(foundPosition).passport_number)
                sharedprefs.getPassportDetailArrayList(mContext)!!.clear()
                var passportDummy=ArrayList<PassportApiModel>()
                sharedprefs.setPassportDetailArrayList(mContext,passportDummy)
                sharedprefs.setPassportDetailArrayList(mContext,passportDetailArrayList)
                isChanged=true
            }
        })


        uploadPassportTxt.setOnClickListener(View.OnClickListener {

            if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED&&
                ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
            ) {
                // Do the file write
                chooseImage()
            } else {
                // Request permission from the user
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE),
                    0
                )
            }
        })
        uploadVisa.setOnClickListener(View.OnClickListener {

            if (ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.CAMERA
                ) == PackageManager.PERMISSION_GRANTED&& ContextCompat.checkSelfPermission(
                    mContext,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // Do the file write
                chooseVisa()
            } else {
                // Request permission from the user
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.CAMERA,Manifest.permission.READ_EXTERNAL_STORAGE),
                    0
                )
            }
        })

        if (passportDetailArrayList.get(foundPosition).passport_image_path.equals(""))
        {

        }
        else{
            ViewSelectedPassport.setImageBitmap(BitmapFactory.decodeFile(passportDetailArrayList.get(foundPosition).passport_image_path))
        }
    }


    fun chooseImage()
    {
        val array = arrayOf("Open Camera", "Choose from Gallery", "Cancel")
     //   var options= arrayOf("Open Camera", "Choose from Gallery", "Cancel")
       // final CharSequence[] options = {"Open Camera", "Choose from Gallery", "Cancel"};
        var builder=AlertDialog.Builder(mContext)
        builder.setTitle("Add Photo!")

        builder.setItems(array) { _, which ->
            val selected = array[which]
            try {
                when (selected) {
                    "Open Camera" ->
                    {
                        val imageFileName =
                            System.currentTimeMillis().toString() + ".jpg"
                        var storageDir:File = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)
                        pictureImagePath=storageDir.absolutePath + "/" + imageFileName
                        Log.e("PICTUREPATH : ",pictureImagePath)
                        val file = File(pictureImagePath)
                        val outputFileUri = Uri.fromFile(file)
                        val cameraIntent =
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
                        startActivityForResult(cameraIntent, CLICK_IMAGE_REQUEST)
                    }
                    "Choose from Gallery" -> {
                        var intent=Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*"
                        startActivityForResult(intent, PICK_IMAGE_REQUEST)

                    }
                    else -> {
                    }
                }
            } catch (e:IllegalArgumentException){

            }
        }
        val dialog = builder.create()
        dialog.show()
    }
    fun chooseVisa()
    {
        val array = arrayOf("Open Camera", "Choose from Gallery", "Cancel")
        var builder=AlertDialog.Builder(mContext)
        builder.setTitle("Add Photo!")

        builder.setItems(array) { _, which ->
            val selected = array[which]
            try {
                when {
                    selected == "Open Camera" -> {
                        val imageFileName =
                            System.currentTimeMillis().toString() + ".jpg"
                        var storageDir:File = Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES)
                        pictureImagePath=storageDir.absolutePath + "/" + imageFileName
                        Log.e("PICTUREPATH : ",pictureImagePath)
                        val file = File(pictureImagePath)
                        val outputFileUri = Uri.fromFile(file)
                        val cameraIntent =
                            Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri)
                        startActivityForResult(cameraIntent, VISA_CAMERA_REQUEST)
                    }
                    selected.equals("Choose from Gallery") -> {
                        var intent=Intent(Intent.ACTION_GET_CONTENT)
                        intent.type = "image/*"
                        startActivityForResult(intent, VISA_GALLERY_REQUEST)

                        Log.e("WORKS","GALLERY")
                    }
                    else -> {

                    }
                }
            }catch (e:IllegalArgumentException){
                // Catch the color string parse exception
                // toast("$selected Color not supported.")
            }
        }
        val dialog = builder.create()
        dialog.show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                showError("Failed to open picture!")
                return
            }
            try {
                actualImage = FileUtil.from(mContext, data.data)
                compressedImage = Compressor.Builder(mContext)
                    .setMaxWidth(940f)
                    .setMaxHeight(800f)
                    .setQuality(100)
                    .setCompressFormat(Bitmap.CompressFormat.JPEG)
                    .setDestinationDirectoryPath(
                        Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_PICTURES
                        ).absolutePath
                    )
                    .build()
                    .compressToFile(actualImage)
                setCompressedImage()
                passport_image_name_path = compressedImage.name
                PassImageName.text = compressedImage.name

                //clearImage();
            } catch (e: IOException) {
                showError("Failed to read picture data!")
                e.printStackTrace()
            }
        }
        if (requestCode == CLICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK) {
            PassportCamera = File(pictureImagePath)
            if (PassportCamera.exists()) {
                try {
                    if (data != null) {
                        PassportCamera = FileUtil.from(mContext, data.data)
                    }
                    CompressPassportCamera = Compressor.Builder(mContext)
                        .setMaxWidth(940f)
                        .setMaxHeight(800f)
                        .setQuality(100)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(
                            Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES
                            ).absolutePath
                        )
                        .build()
                        .compressToFile(PassportCamera)
                    CompressPassportCamera()
                    PassImageName.text = CompressPassportCamera.name
                    passport_image_name_path = CompressPassportCamera.name

                  Toast.makeText(mContext, "Compressed image save in " + CompressPassportCamera.path, Toast.LENGTH_LONG).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        if (requestCode ==VISA_CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {
            VisaCamera = File(pictureImagePath)
            if (VisaCamera.exists()) {
                try {
                    if (data != null) {
                        VisaCamera = FileUtil.from(mContext, data.data)
                    }
                    CompressVisaCamera1 = Compressor.Builder(mContext)
                        .setMaxWidth(940f)
                        .setMaxHeight(800f)
                        .setQuality(100)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(
                            Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES
                            ).absolutePath
                        )
                        .build()
                        .compressToFile(VisaCamera)
                    CompressVISAcamera()
                    VisaImageName.text = CompressVisaCamera1.name
                    visa_image_name_path = CompressVisaCamera1.name
                                       Toast.makeText(mContext, "Compressed image save in " + CompressVisaCamera1.path, Toast.LENGTH_LONG).show()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        if (requestCode == VISA_GALLERY_REQUEST && resultCode == Activity.RESULT_OK) {
            if (data == null) {
                showError("Failed to open picture!")
                return
            }
            try {
                VisaactualImage = FileUtil.from(mContext, data.data)
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.FROYO) {
                    VisacompressedImage = Compressor.Builder(mContext)
                        .setMaxWidth(940f)
                        .setMaxHeight(800f)
                        .setQuality(100)
                        .setCompressFormat(Bitmap.CompressFormat.JPEG)
                        .setDestinationDirectoryPath(
                            Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES
                            ).absolutePath
                        )
                        .build()
                        .compressToFile(VisaactualImage)
                }
                setVisaCompressedImage()
                VisaImageName.text = VisacompressedImage.name
                visa_image_name_path = VisacompressedImage.name
                //clearImage();
            } catch (e: IOException) {
                showError("Failed to read picture data!")
                e.printStackTrace()
            }
        }

    }

    fun showError(errorMessage: String?) {
        Toast.makeText(mContext, errorMessage, Toast.LENGTH_SHORT).show()
    }
    fun setCompressedImage() {
        Toast.makeText(
            mContext,
            "Compressed image save in " + compressedImage.path,
            Toast.LENGTH_LONG
        ).show()
        Log.d("Compressor", "Compressed image save in " + compressedImage.path)

        val bitmap: Bitmap
        val options = BitmapFactory.Options()
        options.inSampleSize = 2
        bitmap = BitmapFactory.decodeFile(compressedImage.path, options)
        ViewSelectedPassport.setImageBitmap(BitmapFactory.decodeFile(compressedImage.path))
        passport_image_path = compressedImage.path
        passport_image_name_path = compressedImage.name

        var inputStream: InputStream? = null // You can get an inputStream using any I/O API

        try {
            inputStream = FileInputStream(compressedImage.path)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val bytes: ByteArray
        val buffer = ByteArray(8192)
        var bytesRead: Int
        val output = ByteArrayOutputStream()

        try {
            while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        bytes = output.toByteArray()
        val encodedString =
            Base64.encodeToString(bytes, Base64.DEFAULT)
        passportImageData = encodedString
        var passportID=passportDetailArrayList.get(foundPosition).id
        var model=PassportApiModel()
        model.id=passportDetailArrayList.get(foundPosition).id
        model.student_unique_id= passportDetailArrayList.get(foundPosition).student_unique_id
        model.student_id= passportDetailArrayList.get(foundPosition).student_id
        model.student_name= passportDetailArrayList.get(foundPosition).student_name
        model.passport_number= passportNumberTxt.text.toString().trim()
        model.nationality= passportNationalityTxt.text.toString().trim()

        model.date_of_issue= passportDetailArrayList.get(foundPosition).date_of_issue
        if ( passportExpiryTxt.text.toString().trim().equals(""))
        {
            model.expiry_date= passportExpiryTxt.text.toString().trim()
        }
        else{
            val fromdate=passportExpiryTxt.text.toString().trim()
            val inputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
            val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val inputDateStr = fromdate
            val date: Date = inputFormat.parse(inputDateStr)
            val outputDateStr: String = outputFormat.format(date)
            model.expiry_date=outputDateStr
        }

        model.passport_expired= passportDetailArrayList.get(foundPosition).passport_expired
        model.emirates_id_no= passportDetailArrayList.get(foundPosition).emirates_id_no
        model.passport_image= passportImageData
        model.passport_image_path=passport_image_path
        model.passport_image_name=passport_image_name_path
        model.is_date_changed=passportDetailArrayList.get(foundPosition).is_date_changed
        model.emirates_id_image= passportDetailArrayList.get(foundPosition).emirates_id_image
        model.emirates_id_image_name= passportDetailArrayList.get(foundPosition).emirates_id_image_name
        model.emirates_id_image_path= passportDetailArrayList.get(foundPosition).emirates_id_image_path

        if (passportID==0)
        {
            model.status= 0
            model.request=1
        }
        else{
            model.status= 1
            model.request= 0
        }
        model.created_at= passportDetailArrayList.get(foundPosition).created_at
        model.updated_at= passportDetailArrayList.get(foundPosition).updated_at
        passportDetailArrayList.removeAt(foundPosition)
        passportDetailArrayList.add(foundPosition,model)
        Log.e("passport number",passportDetailArrayList.get(foundPosition).passport_number)
        sharedprefs.getPassportDetailArrayList(mContext)!!.clear()
        var passportDummy=ArrayList<PassportApiModel>()
        sharedprefs.setPassportDetailArrayList(mContext,passportDummy)
        sharedprefs.setPassportDetailArrayList(mContext,passportDetailArrayList)
        isChanged=true


    }
    fun CompressPassportCamera()
    {
        val bitmap: Bitmap
        val options = BitmapFactory.Options()

        options.inSampleSize = 2
        bitmap = BitmapFactory.decodeFile(CompressPassportCamera.path, options)
        ViewSelectedPassport.setImageBitmap(BitmapFactory.decodeFile(CompressPassportCamera.path))
        passport_image_path = CompressPassportCamera.path
        passport_image_name_path = CompressPassportCamera.name

        var inputStream: InputStream? = null // You can get an inputStream using any I/O API

        try {
            inputStream = FileInputStream(CompressPassportCamera.path)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val bytes: ByteArray
        val buffer = ByteArray(8192)
        var bytesRead: Int
        val output = ByteArrayOutputStream()

        try {
            while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        bytes = output.toByteArray()
        val encodedString =
            Base64.encodeToString(bytes, Base64.DEFAULT)
        passportImageData = encodedString
        var passportID=passportDetailArrayList.get(foundPosition).id
        var model=PassportApiModel()
        model.id=passportDetailArrayList.get(foundPosition).id
        model.student_unique_id= passportDetailArrayList.get(foundPosition).student_unique_id
        model.student_id= passportDetailArrayList.get(foundPosition).student_id
        model.student_name= passportDetailArrayList.get(foundPosition).student_name
        model.passport_number= passportNumberTxt.text.toString().trim()
        model.nationality= passportNationalityTxt.text.toString().trim()
        model.date_of_issue= passportDetailArrayList.get(foundPosition).date_of_issue
        if ( passportExpiryTxt.text.toString().trim().equals(""))
        {
            model.expiry_date= passportExpiryTxt.text.toString().trim()
        }
        else{
            val fromdate=passportExpiryTxt.text.toString().trim()
            //  val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val inputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
            //  val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
            val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val inputDateStr = fromdate
            val date: Date = inputFormat.parse(inputDateStr)
            val outputDateStr: String = outputFormat.format(date)
            model.expiry_date=outputDateStr
        }
        model.passport_image= passportImageData
        model.passport_image_path=passport_image_path
        model.passport_image_name=passport_image_name_path
        model.is_date_changed=passportDetailArrayList.get(foundPosition).is_date_changed
        model.emirates_id_image= passportDetailArrayList.get(foundPosition).emirates_id_image
        model.emirates_id_image_name= passportDetailArrayList.get(foundPosition).emirates_id_image_name
        model.emirates_id_image_path= passportDetailArrayList.get(foundPosition).emirates_id_image_path
        model.passport_expired= passportDetailArrayList.get(foundPosition).passport_expired
        model.emirates_id_no= passportDetailArrayList.get(foundPosition).emirates_id_no
        model.emirates_id_image= visaImageData
        if (passportID==0)
        {
            model.status= 0
            model.request=1
        }
        else{
            model.status= 1
            model.request= 0
        }
        model.created_at= passportDetailArrayList.get(foundPosition).created_at
        model.updated_at= passportDetailArrayList.get(foundPosition).updated_at
        passportDetailArrayList.removeAt(foundPosition)
        passportDetailArrayList.add(foundPosition,model)
        Log.e("passport number",passportDetailArrayList.get(foundPosition).passport_number)
        sharedprefs.getPassportDetailArrayList(mContext)!!.clear()
        var passportDummy=ArrayList<PassportApiModel>()
        sharedprefs.setPassportDetailArrayList(mContext,passportDummy)
        sharedprefs.setPassportDetailArrayList(mContext,passportDetailArrayList)
        isChanged=true
    }
    fun CompressVISAcamera()
    {
        val bitmap: Bitmap
        val options = BitmapFactory.Options()

        options.inSampleSize = 2
        bitmap = BitmapFactory.decodeFile(CompressVisaCamera1.path, options)
        ViewSelectedVisa.setImageBitmap(BitmapFactory.decodeFile(CompressVisaCamera1.path))
        visa_image_path = CompressVisaCamera1.path
        visa_image_name_path = CompressVisaCamera1.name
        var inputStream: InputStream? = null // You can get an inputStream using any I/O API

        try {
            inputStream = FileInputStream(CompressVisaCamera1.path)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val bytes: ByteArray
        val buffer = ByteArray(8192)
        var bytesRead: Int
        val output = ByteArrayOutputStream()

        try {
            while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        bytes = output.toByteArray()
        val encodedString =
            Base64.encodeToString(bytes, Base64.DEFAULT)

        visaImageData = encodedString
        var passportID=passportDetailArrayList.get(foundPosition).id
        var model=PassportApiModel()
        model.id=passportDetailArrayList.get(foundPosition).id
        model.student_unique_id= passportDetailArrayList.get(foundPosition).student_unique_id
        model.student_id= passportDetailArrayList.get(foundPosition).student_id
        model.student_name= passportDetailArrayList.get(foundPosition).student_name
        model.passport_number= passportNumberTxt.text.toString().trim()
        model.nationality= passportNationalityTxt.text.toString().trim()
        model.date_of_issue= passportDetailArrayList.get(foundPosition).date_of_issue
        if ( passportExpiryTxt.text.toString().trim().equals(""))
        {
            model.expiry_date= passportExpiryTxt.text.toString().trim()
        }
        else{
            val fromdate=passportExpiryTxt.text.toString().trim()
            //  val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val inputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
            //  val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
            val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val inputDateStr = fromdate
            val date: Date = inputFormat.parse(inputDateStr)
            val outputDateStr: String = outputFormat.format(date)
            model.expiry_date=outputDateStr
        }

        model.passport_expired= passportDetailArrayList.get(foundPosition).passport_expired
        model.emirates_id_no= passportDetailArrayList.get(foundPosition).emirates_id_no
        model.passport_image_name=passportDetailArrayList.get(foundPosition).passport_image_name
        model.passport_image=passportDetailArrayList.get(foundPosition).passport_image
        model.is_date_changed=passportDetailArrayList.get(foundPosition).is_date_changed
        model.passport_image_path=passportDetailArrayList.get(foundPosition).passport_image_path
        model.emirates_id_image_path=visa_image_path
        model.emirates_id_image=visaImageData
        model.emirates_id_image_name=visa_image_name_path
        if (passportID==0)
        {
            model.status= 0
            model.request=1
        }
        else{
            model.status= 1
            model.request= 0
        }
        model.created_at= passportDetailArrayList.get(foundPosition).created_at
        model.updated_at= passportDetailArrayList.get(foundPosition).updated_at
        passportDetailArrayList.removeAt(foundPosition)
        passportDetailArrayList.add(foundPosition,model)
        Log.e("passport number",passportDetailArrayList.get(foundPosition).passport_number)
        sharedprefs.getPassportDetailArrayList(mContext)!!.clear()
        var passportDummy=ArrayList<PassportApiModel>()
        sharedprefs.setPassportDetailArrayList(mContext,passportDummy)
        sharedprefs.setPassportDetailArrayList(mContext,passportDetailArrayList)
    }
    fun setVisaCompressedImage()
    {
        Toast.makeText(
            mContext,
            "Compressed image save in " + VisacompressedImage.path,
            Toast.LENGTH_LONG
        ).show()
        Log.d(
            "Compressor",
            "Compressed image save in " + VisacompressedImage.path
        )

        val bitmap: Bitmap
        val options = BitmapFactory.Options()

        options.inSampleSize = 2
        bitmap = BitmapFactory.decodeFile(VisacompressedImage.path, options)
        ViewSelectedVisa.setImageBitmap(BitmapFactory.decodeFile(VisacompressedImage.path))
        visa_image_path = VisacompressedImage.path
        visa_image_name_path = VisacompressedImage.name

        var inputStream: InputStream? = null // You can get an inputStream using any I/O API

        try {
            inputStream = FileInputStream(VisacompressedImage.path)
        } catch (e: FileNotFoundException) {
            e.printStackTrace()
        }
        val bytes: ByteArray
        val buffer = ByteArray(8192)
        var bytesRead: Int
        val output = ByteArrayOutputStream()

        try {
            while (inputStream!!.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

        bytes = output.toByteArray()
        val encodedString =
            Base64.encodeToString(bytes, Base64.DEFAULT)
        visaImageData = encodedString
        var passportID=passportDetailArrayList.get(foundPosition).id
        var model=PassportApiModel()
        model.id=passportDetailArrayList.get(foundPosition).id
        model.student_unique_id= passportDetailArrayList.get(foundPosition).student_unique_id
        model.student_id= passportDetailArrayList.get(foundPosition).student_id
        model.student_name= passportDetailArrayList.get(foundPosition).student_name
        model.passport_number= passportNumberTxt.text.toString().trim()
        model.nationality= passportNationalityTxt.text.toString().trim()
        model.date_of_issue= passportDetailArrayList.get(foundPosition).date_of_issue
        if ( passportExpiryTxt.text.toString().trim().equals(""))
        {
            model.expiry_date= passportExpiryTxt.text.toString().trim()
        }
        else{
            val fromdate=passportExpiryTxt.text.toString().trim()
            //  val inputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val inputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
            //  val outputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
            val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val inputDateStr = fromdate
            val date: Date = inputFormat.parse(inputDateStr)
            val outputDateStr: String = outputFormat.format(date)
            model.expiry_date=outputDateStr
        }

        model.passport_expired= passportDetailArrayList.get(foundPosition).passport_expired
        model.emirates_id_no= passportDetailArrayList.get(foundPosition).emirates_id_no
        model.passport_image_name=passportDetailArrayList.get(foundPosition).passport_image_name
        model.passport_image=passportDetailArrayList.get(foundPosition).passport_image
        model.is_date_changed=passportDetailArrayList.get(foundPosition).is_date_changed
        model.passport_image_path=passportDetailArrayList.get(foundPosition).passport_image_path
        model.emirates_id_image_path=visa_image_path
        model.emirates_id_image=visaImageData
        model.emirates_id_image_name=visa_image_name_path
        Log.e("VISA path",visa_image_path)
        Log.e("VISA name",visa_image_name_path)
        Log.e("VISA image",visaImageData)
        if (passportID==0)
        {
            model.status= 0
            model.request=1
        }
        else{
            model.status= 1
            model.request= 0
        }
        Log.e("VISA path",visa_image_path)
        Log.e("VISA name",visa_image_name_path)
        Log.e("VISA image",visaImageData)
        model.created_at= passportDetailArrayList.get(foundPosition).created_at
        model.updated_at= passportDetailArrayList.get(foundPosition).updated_at
        passportDetailArrayList.removeAt(foundPosition)
        passportDetailArrayList.add(foundPosition,model)
        Log.e("passport number",passportDetailArrayList.get(foundPosition).passport_number)
        sharedprefs.getPassportDetailArrayList(mContext)!!.clear()
        var passportDummy=ArrayList<PassportApiModel>()
        sharedprefs.setPassportDetailArrayList(mContext,passportDummy)
        sharedprefs.setPassportDetailArrayList(mContext,passportDetailArrayList)
        isChanged=true
    }

    fun updateLabel()
    {
        isChanged=true
        val myFormat = "dd MMMM yyyy" //In which you need put here
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        passportExpiryTxt.setText(sdf.format(myCalendar.time))
        var passportID=passportDetailArrayList.get(foundPosition).id
        var model=PassportApiModel()
        model.id=passportDetailArrayList.get(foundPosition).id
        model.student_unique_id= passportDetailArrayList.get(foundPosition).student_unique_id
        model.student_id= passportDetailArrayList.get(foundPosition).student_id
        model.student_name= passportDetailArrayList.get(foundPosition).student_name
        model.passport_number= passportNumberTxt.text.toString().trim()
        model.nationality= passportNationalityTxt.text.toString().trim()
        model.passport_image= passportDetailArrayList.get(foundPosition).passport_image
        model.date_of_issue= passportDetailArrayList.get(foundPosition).date_of_issue
        model.is_date_changed=true
        if ( passportExpiryTxt.text.toString().trim().equals(""))
        {
            model.expiry_date= passportExpiryTxt.text.toString().trim()
        }
        else{
            val fromdate=passportExpiryTxt.text.toString().trim()
            val inputFormat: DateFormat = SimpleDateFormat("dd MMMM yyyy")
            val outputFormat: DateFormat = SimpleDateFormat("yyyy-MM-dd")
            val inputDateStr = fromdate
            val date: Date = inputFormat.parse(inputDateStr)
            val outputDateStr: String = outputFormat.format(date)
            model.expiry_date=outputDateStr
        }
        model.passport_expired= passportDetailArrayList.get(foundPosition).passport_expired
        model.emirates_id_no= passportDetailArrayList.get(foundPosition).emirates_id_no
        model.emirates_id_image= passportDetailArrayList.get(foundPosition).emirates_id_image
        if (passportID==0)
        {
            model.status= 0
            model.request=1
        }
        else{
            model.status= 1
            model.request= 0
        }
        model.passport_image_name= passportDetailArrayList.get(foundPosition).passport_image_name
        model.passport_image_path= passportDetailArrayList.get(foundPosition).passport_image_path
        model.emirates_id_image_name=passportDetailArrayList.get(foundPosition).emirates_id_image
        model.emirates_id_image_path=passportDetailArrayList.get(foundPosition).emirates_id_image_path
        model.created_at= passportDetailArrayList.get(foundPosition).created_at
        model.updated_at= passportDetailArrayList.get(foundPosition).updated_at
        passportDetailArrayList.removeAt(foundPosition)
        passportDetailArrayList.add(foundPosition,model)
        var passportDummy=ArrayList<PassportApiModel>()
        sharedprefs.setPassportDetailArrayList(mContext,passportDummy)
        sharedprefs.setPassportDetailArrayList(mContext,passportDetailArrayList)
    }
}





