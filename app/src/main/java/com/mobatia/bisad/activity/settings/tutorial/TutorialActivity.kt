package com.mobatia.bisad.activity.settings.tutorial

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.mobatia.bisad.R
import com.mobatia.bisad.activity.settings.tutorial.adapter.TutorialViewPagerAdapter
import com.mobatia.bisad.constants.JsonConstants
import com.mobatia.bisad.fragment.home.mContext
import com.mobatia.bisad.manager.PreferenceData

class TutorialActivity : AppCompatActivity() {
    lateinit var mContext: Context
    lateinit var sharedprefs: PreferenceData
    lateinit var jsonConstans: JsonConstants
    lateinit var tutorialViewPager: ViewPager
    lateinit var imageSkip: ImageView
    lateinit var mLinearLayout: LinearLayout
    lateinit var mImgCircle: Array<ImageView?>
    //   lateinit var  mImgCircle[]: ImageView
    var bannerarray = ArrayList<Int>()
    var dataType: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        mContext = this
        sharedprefs = PreferenceData()
        jsonConstans = JsonConstants()
        bannerarray.add(R.drawable.tut_1)
        bannerarray.add(R.drawable.tut__2)
        bannerarray.add(R.drawable.tut_3)
        bannerarray.add(R.drawable.tut__4)
        bannerarray.add(R.drawable.tut__5)
        bannerarray.add(R.drawable.tut_6)
        initializeUI()
    }

    private fun initializeUI() {
        // headermanager=HeaderManagerNoColorSpace(SocialMediaDetailActivity.this, "FACEBOOK");
        tutorialViewPager = findViewById(R.id.tutorialViewPager)
        imageSkip = findViewById(R.id.imageSkip)
        mLinearLayout = findViewById(R.id.linear)
        mImgCircle = arrayOfNulls(bannerarray.size)
        val mTutorialViewPagerAdapter = TutorialViewPagerAdapter(mContext, bannerarray)
        tutorialViewPager.setCurrentItem(0)
        tutorialViewPager.setAdapter(mTutorialViewPagerAdapter)
        addShowCountView(0)
        imageSkip.setOnClickListener {
            finish()
        }
        tutorialViewPager.setOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageSelected(position: Int) {
                for (i in bannerarray.indices) {
                    mImgCircle[i]!!.setBackgroundDrawable(
                        resources
                            .getDrawable(R.drawable.blackround)
                    )
                }
                if (position < bannerarray.size) {
                    mImgCircle[position]!!.setBackgroundDrawable(
                        resources
                            .getDrawable(R.drawable.redround)
                    )
                    mLinearLayout.removeAllViews()
                    addShowCountView(position)
                } else {
                    mLinearLayout.removeAllViews()
                    finish()
                }
            }

            override fun onPageScrolled(arg0: Int, arg1: Float, arg2: Int) {}
            override fun onPageScrollStateChanged(arg0: Int) {}
        })
        tutorialViewPager.getAdapter()!!.notifyDataSetChanged()
    }


    private fun addShowCountView(count: Int) {
        for (i in bannerarray.indices) {
            mImgCircle[i] = ImageView(mContext)
            val layoutParams = LinearLayout.LayoutParams(
                resources
                    .getDimension(R.dimen.home_circle_width).toInt(),
                resources.getDimension(
                    R.dimen.home_circle_height
                ).toInt()
            )
            mImgCircle.get(i)!!.setLayoutParams(layoutParams)
            if (i == count) {
                mImgCircle.get(i)!!.setBackgroundResource(R.drawable.redround)
            } else {
                mImgCircle.get(i)!!.setBackgroundResource(R.drawable.blackround)
            }
            mLinearLayout.addView(mImgCircle.get(i))
        }
    }
}

