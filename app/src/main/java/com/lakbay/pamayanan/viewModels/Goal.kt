package com.lakbay.pamayanan.viewModels

import android.graphics.drawable.Drawable
import com.lakbay.pamayanan.utils.CommonUtils

class Goal{
    var title: String = "Goal"
    var donation: Double  = 0.0
    var color: String = "#E4B634"
    var subTitle: String = "Sub Title"
    var description: String = "Description"
    var img: Drawable? = null

    fun getConvertedDonation() : String {
        if(donation == 0.0)
            return ""
        return CommonUtils.convertToAmount(donation)
    }
}