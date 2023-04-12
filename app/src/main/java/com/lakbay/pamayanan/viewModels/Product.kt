package com.lakbay.pamayanan.viewModels

import com.lakbay.pamayanan.utils.CommonUtils
import java.io.Serializable

class Product : Serializable {
    companion object {
        val RESTAURANT_UID = "restaurant_uid"
        val CATEGORY = "category"
    }
    var category: String = ""
    var name: String = ""
    var price: Double = 0.0
    var img: String = ""
    var uid: String = ""
    var restaurant_uid: String = ""
    var description: String = ""
    var isCategoryHeader: Boolean = false
    var variation: ArrayList<String> = ArrayList<String>()
    var variationFinal : ArrayList<Variation> = ArrayList<Variation>()


    fun getPriceText() : String {
        return CommonUtils.convertToAmount(price)
    }
}