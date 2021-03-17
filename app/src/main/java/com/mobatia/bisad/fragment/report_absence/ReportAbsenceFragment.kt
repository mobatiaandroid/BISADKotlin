package com.mobatia.bisad.fragment.report_absence

import android.app.Dialog
import android.content.Context
import android.content.Intent
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
import com.mobatia.bisad.activity.absence.AbsenceDetailActivity
import com.mobatia.bisad.activity.absence.RequestabsenceActivity
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.fragment.report_absence.adapter.RequestAbsenceRecyclerAdapter
import com.mobatia.bisad.fragment.report_absence.model.AbsenceLeaveApiModel
import com.mobatia.bisad.fragment.report_absence.model.AbsenceListModel
import com.mobatia.bisad.fragment.report_absence.model.AbsenceRequestListDetailModel
import com.mobatia.bisad.fragment.student_information.adapter.StudentListAdapter
import com.mobatia.bisad.fragment.student_information.model.StudentList
import com.mobatia.bisad.fragment.student_information.model.StudentListModel
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.recyclermanager.OnItemClickListener
import com.mobatia.bisad.recyclermanager.addOnItemClickListener
import com.mobatia.bisad.rest.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportAbsenceFragment : Fragment(){
    lateinit var jsonConstans: JsonConstants
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var sharedprefs: PreferenceData
    lateinit var studentSpinner: LinearLayout
    lateinit var studImg: ImageView
    lateinit var studentNameTxt: TextView
    lateinit var newRequest: TextView
    lateinit var mAbsenceListView: RecyclerView
    lateinit var studentName: String
    lateinit var studentId: String
    lateinit var studentImg: String
    lateinit var studentClass: String
    var studentListArrayList = ArrayList<StudentList>()
    lateinit var studentInfoCopy :ArrayList<AbsenceRequestListDetailModel>
    var studentInfoArrayList = ArrayList<AbsenceRequestListDetailModel>()
    lateinit var progressDialog: RelativeLayout
    lateinit var mContext: Context
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_report_absence, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        jsonConstans = JsonConstants()
        sharedprefs = PreferenceData()
        mContext = requireContext()
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

        studentSpinner = view!!.findViewById(R.id.studentSpinner) as LinearLayout
        studImg = view!!.findViewById(R.id.studImg) as ImageView
        studentNameTxt = view!!.findViewById(R.id.studentName) as TextView
        newRequest = view!!.findViewById(R.id.newRequest) as TextView
        mAbsenceListView = view!!.findViewById(R.id.mAbsenceListView) as RecyclerView
        progressDialog = view!!.findViewById(R.id.progressDialog) as RelativeLayout
        linearLayoutManager = LinearLayoutManager(mContext)
        mAbsenceListView.layoutManager = linearLayoutManager
        mAbsenceListView.itemAnimator = DefaultItemAnimator()
        studentSpinner.setOnClickListener(object : View.OnClickListener {
            override fun onClick(v: View?) {
                //your implementation goes here
                showStudentList(mContext,studentListArrayList)
               // Log.e("Array Size",studentListArrayList.size.toString())
            }
        })
        val aniRotate: Animation =
            AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
        progressDialog.startAnimation(aniRotate)
        mAbsenceListView.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                val intent =Intent(activity,AbsenceDetailActivity::class.java)
                intent.putExtra("studentName",sharedprefs.getStudentName(mContext))
                intent.putExtra("studentClass",sharedprefs.getStudentClass(mContext))
                intent.putExtra("fromDate",studentInfoArrayList.get(position).from_date)
                intent.putExtra("toDate",studentInfoArrayList.get(position).to_date)
                intent.putExtra("reason",studentInfoArrayList.get(position).reason)
                activity?.startActivity(intent)
            }
        })

        /*    val intent =Intent(activity,AbsenceDetailActivity::class.java)
                intent.putExtra("studentName",sharedprefs.getStudentName(mContext))
                intent.putExtra("studentClass",sharedprefs.getStudentClass(mContext))
                intent.putExtra("fromDate",studentInfoArrayList.get(position).from_date)
                intent.putExtra("toDate",studentInfoArrayList.get(position).to_date)
                intent.putExtra("reason",studentInfoArrayList.get(position).reason)
                activity?.startActivity(intent)*/
        newRequest.setOnClickListener(View.OnClickListener {

            val intent =Intent(activity,RequestabsenceActivity::class.java)
            activity?.startActivity(intent)
        })
    }


    fun callStudentListApi()
    {
        val token = sharedprefs.getaccesstoken(mContext)
        val call: Call<StudentListModel> = ApiClient.getClient.studentList("Bearer "+token)
        call.enqueue(object : Callback<StudentListModel>{
            override fun onFailure(call: Call<StudentListModel>, t: Throwable) {
                Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<StudentListModel>, response: Response<StudentListModel>) {
                val arraySize :Int =response.body()!!.responseArray!!.studentList.size
                if (response.body()!!.status==100)
                {
                    studentListArrayList.addAll(response.body()!!.responseArray!!.studentList)
                    if (sharedprefs.getStudentID(mContext).equals(""))
                    {
                        Log.e("Empty Img","Empty")
                        studentName=studentListArrayList.get(0).name
                        studentImg=studentListArrayList.get(0).photo
                        studentId=studentListArrayList.get(0).id
                        studentClass=studentListArrayList.get(0).section
                        sharedprefs.setStudentID(mContext,studentId)
                        sharedprefs.setStudentName(mContext,studentName)
                        sharedprefs.setStudentPhoto(mContext,studentImg)
                        sharedprefs.setStudentClass(mContext,studentClass)
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
                        studentClass= sharedprefs.getStudentClass(mContext)!!
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
                        callStudentLeaveInfo()
                    }
                    else{
                        InternetCheckClass.showSuccessInternetAlert(com.mobatia.bisad.fragment.home.mContext)
                    }

                    //callStudentInfoApi()
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
        if(mStudentList.size>0)
        {
            val studentAdapter = StudentListAdapter(mContext,mStudentList)
            studentListRecycler.setAdapter(studentAdapter)
        }

        btn_dismiss?.setOnClickListener()
        {
            dialog.dismiss()
        }
        studentListRecycler.addOnItemClickListener(object: OnItemClickListener {
            override fun onItemClicked(position: Int, view: View) {
                // Your logic
                progressDialog.visibility=View.VISIBLE
                val aniRotate: Animation =
                    AnimationUtils.loadAnimation(mContext, R.anim.linear_interpolator)
                progressDialog.startAnimation(aniRotate)

                studentName=studentListArrayList.get(position).name
                studentImg=studentListArrayList.get(position).photo
                studentId=studentListArrayList.get(position).id
                studentClass=studentListArrayList.get(position).section
                sharedprefs.setStudentID(mContext,studentId)
                sharedprefs.setStudentName(mContext,studentName)
                sharedprefs.setStudentPhoto(mContext,studentImg)
                sharedprefs.setStudentClass(mContext,studentClass)
                studentNameTxt.text=studentName
                studentInfoArrayList.clear()
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
                else
                {
                    studImg.setImageResource(R.drawable.student)
                }
                progressDialog.visibility = View.VISIBLE
               callStudentLeaveInfo()
              //  Toast.makeText(activity, mStudentList.get(position).name, Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        })
        dialog.show()
    }
    fun callStudentLeaveInfo()
    {
        progressDialog.visibility = View.VISIBLE
        studentInfoCopy=ArrayList<AbsenceRequestListDetailModel>()
        studentInfoArrayList.clear()
        val token = sharedprefs.getaccesstoken(mContext)
        val studentbody= AbsenceLeaveApiModel(sharedprefs.getStudentID(mContext)!!,0,20)
        val call: Call<AbsenceListModel> = ApiClient.getClient.absenceList(studentbody,"Bearer "+token)
        call.enqueue(object : Callback<AbsenceListModel>{
            override fun onFailure(call: Call<AbsenceListModel>, t: Throwable) {
                progressDialog.visibility = View.GONE
                Log.e("Error", t.localizedMessage)
            }
            override fun onResponse(call: Call<AbsenceListModel>, response: Response<AbsenceListModel>) {
                progressDialog.visibility = View.GONE
                //   val arraySize :Int =response.body()!!.responseArray!!.studentInfo.size
                if (response.body()!!.status==100)
                {
                    studentInfoCopy.addAll(response.body()!!.responseArray.requestList)
                    studentInfoArrayList=studentInfoCopy

                        if (studentInfoArrayList.size>0)
                        {
                            mAbsenceListView.visibility=View.VISIBLE
                            val studentInfoAdapter = RequestAbsenceRecyclerAdapter(studentInfoArrayList)
                            mAbsenceListView.setAdapter(studentInfoAdapter)
                        }
                        else{
                            mAbsenceListView.visibility=View.GONE
                        }



                }


            }

        })
    }

    override fun onResume() {
        super.onResume()
        studentNameTxt.setText(sharedprefs.getStudentName(mContext))
        studentId= sharedprefs.getStudentID(mContext).toString()
        studentImg= sharedprefs.getStudentPhoto(mContext)!!
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
        else
        {
            studImg.setImageResource(R.drawable.student)
        }
        callStudentLeaveInfo()
    //    callStudentListApi()
    }
}




