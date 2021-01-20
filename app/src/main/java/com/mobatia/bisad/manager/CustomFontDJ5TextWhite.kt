package com.mobatia.bisad.manager

import android.content.Context
import android.graphics.Typeface
import android.util.AttributeSet
import com.mobatia.bisad.R

class CustomFontDJ5TextWhite  : androidx.appcompat.widget.AppCompatTextView{
    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        // ...
        val type= Typeface.createFromAsset(context.assets,"fonts/DJ5CTRIAL.otf")
        this.setTypeface(type)
        this.setTextColor(resources.getColor(R.color.white))
    }

    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0) {
        val type =
            Typeface.createFromAsset(context.assets, "fonts/DJ5CTRIAL.otf")
        this.setTypeface(type)
        this.setTextColor(context.resources.getColor(R.color.white))
    }
}