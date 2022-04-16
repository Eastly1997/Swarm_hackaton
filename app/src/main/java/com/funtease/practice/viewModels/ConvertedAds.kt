package com.funtease.practice.viewModels

import android.graphics.drawable.Drawable
import com.google.android.gms.ads.nativead.NativeAd

class ConvertedAds() {

    var mainImage: Drawable? = null
    var headline: String? = ""

    constructor(ads: NativeAd) : this() {
        mainImage = ads.mediaContent.mainImage
        headline = ads.headline
    }


}