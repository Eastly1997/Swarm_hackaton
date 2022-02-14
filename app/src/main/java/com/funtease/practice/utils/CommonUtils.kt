package com.funtease.practice.utils

import android.content.Context
import android.content.res.Resources
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.funtease.practice.R

class CommonUtils {

    fun convertDptoPx(dp: Int) : Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    fun convertPxtoDp(px: Int) : Int {
        return (px / Resources.getSystem().displayMetrics.density).toInt()
    }

    fun loadImage(context: Context, url: String, view: ImageView) {
        Glide.with(context)
            .load(url)
            .centerCrop()
            .placeholder(R.color.teal_200)
            .into(view)
    }
}