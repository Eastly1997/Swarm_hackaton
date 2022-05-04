package com.lakbay.pamayanan.viewModels

class User() {
    companion object {
        val FIELD_UID = "uid"
        val FIELD_IMG = "img"
        val FIELD_MOBILE_NUMBER = "mobileNumber"
        val FIELD_USER_NAME = "userName"
        val FIELD_DONATED_AMOUNT = "donatedAmount"
    }
    var uid: String = ""
    var img: String = ""
    var mobileNumber: String = ""
    var userName: String = ""
    var donatedAmount: Double = 0.00
}