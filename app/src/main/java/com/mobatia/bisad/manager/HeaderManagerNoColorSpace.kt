package com.mobatia.bisad.manager

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.drawable.Drawable
import android.graphics.drawable.StateListDrawable
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams
import android.widget.TextView
import com.mobatia.bisad.R
import java.io.Serializable


class HeaderManagerNoColorSpace : Serializable{

    lateinit var context: Activity
    lateinit var  inflator: LayoutInflater
    lateinit var  headerView: View
    lateinit var  mHeading1: TextView
    lateinit var  mRightText: TextView
    lateinit var  mLeftText: TextView
    lateinit var  relativeParams: LayoutParams
    lateinit var  heading1: String
    lateinit var  edtText: EditText
    lateinit var  mLeftImage: ImageView
    lateinit var  mRight: ImageView
    lateinit var  mLeft: ImageView
    public fun HeaderManagerNoColorSpace(context:Activity,heading1 :String)
    {
       this.context=context
        inflator= LayoutInflater.from(context)
        this.heading1=heading1
    }

    public fun setVisible(v:View)
    {
      v.visibility=View.VISIBLE
    }
    public fun setInvisible(v:View)
    {
      v.visibility=View.INVISIBLE
    }
    public fun getHeader(headerHolder: RelativeLayout,type:Int,getHeading:Boolean) :Int
    {
        initializeUI(getHeading, type)
        relativeParams = LayoutParams(
            LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT
        )
        relativeParams.addRule(RelativeLayout.ALIGN_PARENT_TOP)
        headerHolder.addView(headerView, relativeParams)
        return headerView.id
    }
    public fun initializeUI(type:Int)
    {
        inflator = LayoutInflater.from(context)
        headerView = inflator.inflate(R.layout.common_header_single_withviewline, null)
        val logoHeader = headerView.findViewById<View>(R.id.relative_logo_header) as RelativeLayout
        if (type == 0) {
            logoHeader.setBackgroundResource(R.drawable.titlebar) // two
            // buttons
        } else if (type == 1) {
            logoHeader.setBackgroundResource(R.drawable.titlebar) // left
            // button
        }
        mHeading1 = headerView.findViewById<View>(R.id.heading) as TextView
        mHeading1.visibility = View.GONE
        mHeading1.text = heading1
        mRight = headerView.findViewById<View>(R.id.btn_right) as ImageView
        mLeft = headerView.findViewById<View>(R.id.btn_left) as ImageView
    }

    public fun initializeUI(getHeading :Boolean,type:Int)
    {
        inflator = LayoutInflater.from(context)
        headerView = inflator.inflate(R.layout.common_header_single_withviewline, null)
        val logoHeader = headerView
            .findViewById<View>(R.id.relative_logo_header) as RelativeLayout
        if (type == 0) {
            logoHeader.setBackgroundResource(R.drawable.titlebar)
        } else if (type == 1) {
            logoHeader.setBackgroundResource(R.drawable.titlebar)
            mHeading1 = headerView.findViewById<View>(R.id.heading) as TextView
            mHeading1.visibility = View.GONE
        }
        mHeading1 = headerView.findViewById<View>(R.id.heading) as TextView
        mHeading1.visibility = View.GONE
        mHeading1.text = heading1
        mRight =
            headerView.findViewById<View>(R.id.btn_right) as ImageView
        mLeft =
            headerView.findViewById<View>(R.id.btn_left) as ImageView
    }

   public fun setTitle(title:String)
   {
       mHeading1.text = title
   }
  public fun getLeftButton():ImageView{
      return mLeft
  }
  public fun setButtonLeftSelector(normalStateResID :Int,pressedStateResID:Int)
  {
      mLeft.setImageDrawable(
          getButtonDrawableByScreenCathegory(
              normalStateResID, pressedStateResID
          )
      )
      setVisible(mLeft)
  }
    @SuppressLint("UseCompatLoadingForDrawables")
    public fun getButtonDrawableByScreenCathegory(normalStateResID :Int, pressedStateResID:Int) :Drawable

    {
        val state_normal = context.resources
            .getDrawable(normalStateResID).mutate()
        val state_pressed = context.resources
            .getDrawable(pressedStateResID).mutate()
        val drawable = StateListDrawable()
        drawable.addState(intArrayOf(android.R.attr.state_pressed), state_pressed)
        drawable.addState(intArrayOf(android.R.attr.state_enabled), state_normal)
        return drawable
    }


}