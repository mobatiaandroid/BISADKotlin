package com.mobatia.bisad.activity.common

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.Window
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import com.mobatia.bisad.MainActivity
import com.mobatia.bisad.R
import com.mobatia.bisad.constants.InternetCheckClass
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.manager.PreferenceData
import com.mobatia.bisad.rest.AccessTokenClass
import com.mobatia.bisad.rest.ApiClient
import kotlinx.android.synthetic.main.activity_login.*
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity(),View.OnTouchListener{
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    var TAG :String="FIREBASE"
    var deviceId : String=""
    lateinit var dialog : Dialog
    lateinit var jsonConstans: JsonConstants
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mContext=this
        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()
        var accessTokenValue= AccessTokenClass.getAccessToken(mContext)
        initUI()

    }
    @SuppressLint("ClickableViewAccessibility")
    fun initUI()
    {
       // val button = findViewById<Button>(R.id.loginBtn)
        var userEditText = findViewById(R.id.userEditText) as EditText
        var passwordEditText = findViewById(R.id.passwordEditText) as EditText
        var loginBtn = findViewById(R.id.loginBtn) as Button
        var signUpButton = findViewById(R.id.signUpButton) as Button
        var forgotPasswordButton = findViewById(R.id.forgotPasswordButton) as Button
        userEditText.setOnTouchListener(this)
        passwordEditText.setOnTouchListener(this)
        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                return@OnCompleteListener
            }
            // Get new FCM registration token
            val token = task.result
            deviceId=token.toString()
            sharedprefs.setFcmID(mContext,deviceId)
        })
        //Keyboard done button click username
        userEditText.setOnEditorActionListener { v, actionId, event ->
			if(actionId == EditorInfo.IME_ACTION_DONE){
                userEditText?.isFocusable=false
                userEditText?.isFocusableInTouchMode=false
				false
			} else {
                userEditText?.isFocusable=false
                userEditText?.isFocusableInTouchMode=false
				false
			}
		}

        //Keyboard done button click password
        passwordEditText.setOnEditorActionListener { v, actionId, event ->
			if(actionId == EditorInfo.IME_ACTION_DONE){
                passwordEditText?.isFocusable=false
                passwordEditText?.isFocusableInTouchMode=false
				false
			} else {
                userEditText?.isFocusable=false
                userEditText?.isFocusableInTouchMode=false
				false
			}
		}

        //Signup Button Click
        signUpButton?.setOnClickListener()
        {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(userEditText.windowToken, 0)
            val immq = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            immq?.hideSoftInputFromWindow(passwordEditText.windowToken, 0)
            showSignupDialogue(mContext)
        }

        //Forget Password Button Click
        forgotPasswordButton?.setOnClickListener()
        {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            imm?.hideSoftInputFromWindow(userEditText.windowToken, 0)
            val immq = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            immq?.hideSoftInputFromWindow(passwordEditText.windowToken, 0)
            showForgetPassword(mContext)
        }


        //Login Button Click
        loginBtn?.setOnClickListener()
        {
            if(userEditText.text.toString().trim().equals(""))
            {
                Toast.makeText(baseContext, "Enter an Email", Toast.LENGTH_SHORT).show()
            }
            else
            {
                var emailPattern = InternetCheckClass.isEmailValid(userEditText.text.toString().trim())
                if (!emailPattern)
                {
                    Toast.makeText(baseContext, "Enter an valid Email", Toast.LENGTH_SHORT).show()
                }
                else{
                    if(passwordEditText.text.toString().trim().equals(""))
                    {
                        Toast.makeText(baseContext, "Enter an Password", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        var internetCheck = InternetCheckClass.isInternetAvailable(mContext)
                        if (internetCheck)
                        {
                            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                            imm?.hideSoftInputFromWindow(userEditText.windowToken, 0)
                            val immq = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                            immq?.hideSoftInputFromWindow(passwordEditText.windowToken, 0)
                            var emailTxt = userEditText.text.toString().trim()
                            var passwordTxt = passwordEditText.text.toString().trim()

                            callLoginApi(emailTxt,passwordTxt,deviceId)
                        }
                    }
                }
            }
    }
}

    // Login Api Call
    fun callLoginApi(email : String,password : String,deviceId : String)
    {

    }

    // touch listener for edittexts
    override fun onTouch(view: View, motionEvent: MotionEvent): Boolean {
        when (view) {
            userEditText -> {
                when (motionEvent.action){
                    MotionEvent.ACTION_DOWN -> {
                        userEditText?.isFocusable=true
                        userEditText?.isFocusableInTouchMode=true
                    }
                    MotionEvent.ACTION_UP -> {
                        view.performClick()
                        userEditText?.isFocusable=true
                        userEditText?.isFocusableInTouchMode=true
                    }
                }
            }
            passwordEditText -> {
                when (motionEvent.action){
                    MotionEvent.ACTION_DOWN -> {
                        passwordEditText?.isFocusable=true
                        passwordEditText?.isFocusableInTouchMode=true
                    }
                    MotionEvent.ACTION_UP -> {
                        view.performClick()
                        passwordEditText?.isFocusable=true
                        passwordEditText?.isFocusableInTouchMode=true
                    }
                }
            }
        }
        return false
    }



    //Sign up popup
    @SuppressLint("ClickableViewAccessibility")
    fun showSignupDialogue(context: Context)
    {
        dialog=Dialog(mContext,R.style.NewDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_layout_signup)
        var text_dialog = dialog.findViewById(R.id.text_dialog) as EditText
        var btn_maybelater = dialog.findViewById(R.id.btn_maybelater) as Button
        var btn_signup = dialog.findViewById(R.id.btn_signup) as Button
        text_dialog.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                text_dialog?.isFocusable=false
                text_dialog?.isFocusableInTouchMode=false
                false
            } else {
                text_dialog?.isFocusable=false
                text_dialog?.isFocusableInTouchMode=false
                false
            }
        }

        text_dialog.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, m: MotionEvent): Boolean {
                // Perform tasks here
                text_dialog?.isFocusable=true
                text_dialog?.isFocusableInTouchMode=true
                return false
            }
        })
        btn_signup?.setOnClickListener()
        {
            if (text_dialog.text.toString().trim().equals(""))
            {
                Toast.makeText(baseContext, "Enter an Email", Toast.LENGTH_SHORT).show()
            }
            else
            {
                var emailPattern = InternetCheckClass.isEmailValid(text_dialog.text.toString().trim())
                if (!emailPattern)
                {
                    Toast.makeText(baseContext, "Enter an valid Email", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(text_dialog.windowToken, 0)
                    callSignUpApi(text_dialog.text.toString().trim())
                }
            }
        }
        btn_maybelater?.setOnClickListener()
        {
            dialog.dismiss()
        }
        dialog.show()

    }


    //Signup API Call
    fun callSignUpApi(email: String)
    {
        val call: Call<ResponseBody> = ApiClient.getClient.signup(
           email,"2", sharedprefs.getFcmID(mContext)
        )
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
                        if(jsonObject.has(jsonConstans.STATUS))
                        {
                            val status : Int=jsonObject.optInt(jsonConstans.STATUS)
                            if (status==100)
                            {

                            }
                            else{
                                if (status==116)
                                {
                                    //call Token Expired
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
                                    }
                                }

                            }
                        }
//                        val accessToken: String = jsonObject.optString("access_token")
//                        Log.e("Accesstokenlog", accessToken)
//                        AccessTokenClass.sharedprefs.setaccesstoken(mContext, accessToken)
//                        Log.e("SharedPrefsAccess", AccessTokenClass.sharedprefs.getaccesstoken(mContext))
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

        })
    }

    //Forget Password popup
    @SuppressLint("ClickableViewAccessibility")
    fun showForgetPassword(context: Context)
    {
        dialog=Dialog(mContext,R.style.NewDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.dialog_forgot_password)
        var text_dialog = dialog.findViewById(R.id.text_dialog) as EditText
        var btn_maybelater = dialog.findViewById(R.id.btn_maybelater) as Button
        var btn_signup = dialog.findViewById(R.id.btn_signup) as Button
        text_dialog.setOnEditorActionListener { v, actionId, event ->
            if(actionId == EditorInfo.IME_ACTION_DONE){
                text_dialog?.isFocusable=false
                text_dialog?.isFocusableInTouchMode=false
                false
            } else {
                text_dialog?.isFocusable=false
                text_dialog?.isFocusableInTouchMode=false
                false
            }
        }

        btn_signup?.setOnClickListener()
        {
            if (text_dialog.text.toString().trim().equals(""))
            {
                Toast.makeText(baseContext, "Enter an Email", Toast.LENGTH_SHORT).show()
            }
            else
            {
                var emailPattern = InternetCheckClass.isEmailValid(text_dialog.text.toString().trim())
                if (!emailPattern)
                {
                    Toast.makeText(baseContext, "Enter an valid Email", Toast.LENGTH_SHORT).show()
                }
                else
                {
                    val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
                    imm?.hideSoftInputFromWindow(text_dialog.windowToken, 0)
                    callForgetPassword(text_dialog.text.toString().trim())
                }
            }
        }

        text_dialog.setOnTouchListener(object : View.OnTouchListener {
            override fun onTouch(v: View, m: MotionEvent): Boolean {
                // Perform tasks here
                text_dialog?.isFocusable=true
                text_dialog?.isFocusableInTouchMode=true
                return false
            }
        })

        btn_maybelater?.setOnClickListener()
        {
            dialog.dismiss()
        }
        dialog.show()

    }
    
    // Call Forget Password API
    fun callForgetPassword(email: String)
    {

    }



}