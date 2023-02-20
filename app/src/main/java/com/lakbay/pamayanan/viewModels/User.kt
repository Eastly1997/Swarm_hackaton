package com.lakbay.pamayanan.viewModels

class User {
    companion object {
        const val FIELD_UID = "uid"
        const val FIELD_IMG = "img"
        const val FIELD_MOBILE_NUMBER = "mobileNumber"
        const val FIELD_USER_NAME = "userName"
        const val FIELD_LOYALTY_POINTS = "loyaltyPoints"
    }

    var uid: String = ""
    var img: String = ""
    var mobileNumber: String = ""
    var userName: String = ""
    var loyaltyPoints: Double = 0.00
}