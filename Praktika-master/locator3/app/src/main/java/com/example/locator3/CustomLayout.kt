package com.example.locator3

import android.content.Context
import android.os.Build
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi

class CustomLayout : LinearLayout {
    // описываем публичные аттрибуты для текста и картинки
    var tempTextView: TextView? = null
    var icoImageView: ImageView? = null

    // пишем конструктор
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    constructor(context: Context?): super(context){
        this.orientation = LinearLayout.VERTICAL
        this.minimumWidth = 150

        // создаем TextView
        tempTextView = TextView(context)
        tempTextView!!.textAlignment = View.TEXT_ALIGNMENT_CENTER
        this.addView(tempTextView)

        // создаем ImageView
        icoImageView = ImageView(context)
        this.addView(icoImageView)

    }
}