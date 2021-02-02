package com.mobatia.bisad.fragment.student_information

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.mobatia.bisad.R
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.home.model.BannerModel
import com.mobatia.bisad.fragment.home.sharedprefs
import com.mobatia.bisad.fragment.student_information.adapter.StudentInfoAdapter
import com.mobatia.bisad.fragment.student_information.adapter.StudentListAdapter
import com.mobatia.bisad.fragment.student_information.model.*
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StudentInformationFragment : Fragment(){
    lateinit var jsonConstans: JsonConstants
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var studentInfoRecycler: RecyclerView
    lateinit var studentNameTxt: TextView
    lateinit var studImg: ImageView
    var studentListArrayList = ArrayList<StudentList>()
    lateinit var studentName: String
    lateinit var studentId: String
    lateinit var studentImg: String
    lateinit var sharedprefs: PreferenceData
    lateinit var progressDialog: RelativeLayout
    lateinit var imageView6: ImageView
    lateinit var imageView4: ImageView
    lateinit var mContext:Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_student_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
        sharedprefs.setStudentID(mContext,"")
        sharedprefs.setStudentName(mContext,"")
        sharedprefs.setStudentPhoto(mContext,"")
        initializeUI()
        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
        if (internetCheck)
        {
            callStudentListApi()
        }
        else{
            InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
        }
    }

    private fun initializeUI() {
        studentName=""
        studentImg=""
        studentId=""
        linearLayoutManager = LinearLayoutManager(mContext)
        studentInfoRecycler = view!!.findViewById(R.id.studentInfoRecycler) as RecyclerView
        studentNameTxt = view!!.findViewById(R.id.studentName) as TextView
        studImg = view!!.findViewById(R.id.studImg) as ImageView
        imageView6 = view!!.findViewById(R.id.imageView6) as ImageView
        imageView4 = view!!.findViewById(R.id.imageView4) as ImageView
        studentInfoRecycler.layoutManager = linearLayoutManager
        studentInfoRecycler.itemAnimator = DefaultItemAnimator()
        progressDialog = view!!.findViewById(R.id.progressDialog) as RelativeLayout
        progressDialog.visibility=View.VISIBLE
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        studentNameTxt.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //your implementation goes here
                showStudentList(mContext,studentListArrayList)

            }
        })
        imageView6.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //your implementation goes here
                showStudentList(mContext,studentListArrayList)

            }
        })
        imageView4.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //your implementation goes here
                showStudentList(mContext,studentListArrayList)

            }
        })

    }

    fun callStudentListApi()
    {
        progressDialog.visibility = View.VISIBLE
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<StudentListModel> = ApiClient.getClient.studentList("Bearer "+token)
        call.enqueue(object : Callback<StudentListModel>{
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
               // Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<StudentListModel>, response: Response<StudentListModel>) {
             //   val arraySize :Int =response.body()!!.responseArray!!.studentList.size
                if (response.body()!!.status==100)
                {
                    studentListArrayList.addAll(response.body()!!.responseArray!!.studentList)
                    if (sharedprefs.getStudentID(mContext).equals(""))
                    {
                        studentName=studentListArrayList.get(0).name
                        studentImg=studentListArrayList.get(0).photo
                        studentId=studentListArrayList.get(0).id
                        sharedprefs.setStudentID(mContext,studentId)
                        sharedprefs.setStudentName(mContext,studentName)
                        sharedprefs.setStudentPhoto(mContext,studentImg)
                        studentNameTxt.text=studentName
                       if(!studentImg.equals(""))
                       {
                           Glide.with(mContext) //1
                               .load(studentImg)
                               .placeholder(R.drawable.student)
                               .error(R.drawable.student)
                               .skipMemoryCache(true) //2
                               .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                               .transform(CircleCrop()) //4
                               .into(studImg)
                       }
                        else{
                         studImg.setImageResource(R.drawable.student)
                       }

                    }
                    else{
                        studentName= sharedprefs.getStudentName(mContext)!!
                        studentImg= sharedprefs.getStudentPhoto(mContext)!!
                        studentId= sharedprefs.getStudentID(mContext)!!
                        studentNameTxt.text=studentName
                        if(!studentImg.equals(""))
                        {
                            Glide.with(mContext) //1
                                .load(studentImg)
                                .placeholder(R.drawable.student)
                                .error(R.drawable.student)
                                .skipMemoryCache(true) //2
                                .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                                .transform(CircleCrop()) //4
                                .into(studImg)
                        }
                        else{
                            studImg.setImageResource(R.drawable.student)
                        }
                    }
                    var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                    if (internetCheck)
                    {
                        callStudentInfoApi()
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }
                }
                else if(response.body()!!.status==116)
                {
                    var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                    if (internetCheck)
                    {
                        AccessTokenClass.getAccessToken(mContext)
                        callStudentListApi()
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                }
                else
                {
                    InternetCheckClass.checkApiStatusError(response.body()!!.status,mContext)
                }


            }

        })
    }

    fun callStudentInfoApi()
    {
        progressDialog.visibility = View.VISIBLE
        var studentInfoArrayList = ArrayList<StudentInfoDetail>()
        val token = sharedprefs.getaccesstoken(mContext)
        val studentbody= StudentInfoApiModel(sharedprefs.getStudentID(mContext)!!)
        val call: Call<StudentInfoModel> = ApiClient.getClient.studentInfo(studentbody,"Bearer "+token)
        call.enqueue(object : Callback<StudentInfoModel>{
            override fun onFailure(call: Call<StudentInfoModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
            }
            override fun onResponse(call: Call<StudentInfoModel>, response: Response<StudentInfoModel>) {
                progressDialog.visibility = View.GONE
             //   val arraySize :Int =response.body()!!.responseArray!!.studentInfo.size
                if (response.body()!!.status==100)
                {

                    if(response.body()!!.responseArray.studentInfo.size>0)
                    {
                        studentInfoRecycler.visibility=View.VISIBLE
                        studentInfoArrayList.addAll(response.body()!!.responseArray!!.studentInfo)
                        val studentInfoAdapter = StudentInfoAdapter(studentInfoArrayList)
                        studentInfoRecycler.setAdapter(studentInfoAdapter)
                    }
                    else
                    {
                        studentInfoRecycler.visibility=View.GONE
                        showSuccessAlert(mContext,"No data found.","Alert")
                    }

                }



            }

        })
    }
    fun showStudentList(context: Context ,mStudentList : ArrayList<StudentList>)
    {
        val dialog = Dialog(context)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.dialogue_student_list)
        var iconImageView = dialog.findViewById(R.id.iconImageView) as ImageView
        var btn_dismiss = dialog.findViewById(R.id.btn_dismiss) as Button
        var studentListRecycler = dialog.findViewById(R.id.studentListRecycler) as RecyclerView
        iconImageView.setImageResource(R.drawable.boy)
        //if(mSocialMediaArray.get())
        val sdk = Build.VERSION.SDK_INT
        if (sdk < Build.VERSION_CODES.JELLY_BEAN) {
            btn_dismiss.setBackgroundDrawable(
                mContext.resources.getDrawable(R.drawable.button_new)
            )
        } else {
            btn_dismiss.setBackground(mContext.resources.getDrawable(R.drawable.button_new))
        }

        studentListRecycler.setHasFixedSize(true)
        val llm = LinearLayoutManager(mContext)
        llm.orientation = LinearLayoutManager.VERTICAL
        studentListRecycler.setLayoutManager(llm)
        val studentAdapter = StudentListAdapter(mContext,mStudentList)
        studentListRecycler.setAdapter(studentAdapter)
        btn_dismiss?.setOnClickListener()
        {
            dialog.dismiss()
        }
        studentListRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                studentName=studentListArrayList.get(position).name
                studentImg=studentListArrayList.get(position).photo
                studentId=studentListArrayList.get(position).id
                sharedprefs.setStudentID(mContext,studentId)
                sharedprefs.setStudentName(mContext,studentName)
                sharedprefs.setStudentPhoto(mContext,studentImg)
                studentNameTxt.text=studentName
                if(!studentImg.equals(""))
                {
                    Glide.with(mContext) //1
                        .load(studentImg)
                        .placeholder(R.drawable.student)
                        .error(R.drawable.student)
                        .skipMemoryCache(true) //2
                        .diskCacheStrategy(DiskCacheStrategy.NONE) //3
                        .transform(CircleCrop()) //4
                        .into(studImg)
                }
                else{
                    studImg.setImageResource(R.drawable.student)
                }
                progressDialog.visibility=View.VISIBLE
                studentInfoRecycler.visibility=View.GONE
                callStudentInfoApi()
                dialog.dismiss()
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

}




