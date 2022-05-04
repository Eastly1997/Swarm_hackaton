package com.lakbay.pamayanan.utils

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.lakbay.pamayanan.R


class CommonUtils {

    companion object {
        val FIREBASE_USER = "users_staging"
        val FIREBASE_DONATION = "donation_staging"
        val ENVIRONMENT = "environment"
        val ENVIRONMENT_PRODUCTION = "production"
        val ENVIRONMENT_STAGING = "staging"
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

        fun loadImage(context: Context, url: Drawable, view: ImageView) {
            Glide.with(context)
                .load(url)
                .centerCrop()
                .placeholder(R.color.teal_200)
                .into(view)
        }
        fun loadCurvedImage(context: Context, url: String, view: ImageView, radius: Int) {
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(radius))
            Glide.with(context)
                .load(url)
                .centerCrop()
                .apply(requestOptions)
                .placeholder(R.color.teal_200)
                .into(view)
        }

        fun loadCurvedImage(context: Context, url: Drawable, view: ImageView, radius: Int) {
            var requestOptions = RequestOptions()
            requestOptions = requestOptions.transforms(CenterCrop(), RoundedCorners(radius))
            Glide.with(context)
                .load(url)
                .centerCrop()
                .apply(requestOptions)
                .placeholder(R.color.teal_200)
                .into(view)
        }


        fun convertToAmount(amount: Double) :String {
            return "₱" + String.format("%.2f", amount)
        }

    }
}