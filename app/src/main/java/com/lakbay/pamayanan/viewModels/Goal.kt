package com.lakbay.pamayanan.viewModels

import com.lakbay.pamayanan.utils.CommonUtils

class Goal(var name: String, var donation: Double, var hexColor: String){

   constructor() : this("Goal", 0.0,"4567")

    fun getConvertedDonation() : String {
        if(donation == 0.0)
            return ""
        return CommonUtils.convertToAmount(donation)
    }
}