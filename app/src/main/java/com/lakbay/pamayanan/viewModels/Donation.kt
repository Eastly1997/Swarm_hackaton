package com.lakbay.pamayanan.viewModels

class Donation {
    companion object {
        val FIELD_TOTAL = "total"
        val FIELD_CHARITY = "charity"
        val FIELD_COMMUNITY = "community"
        val FIELD_DEVELOPMENT = "development"
    }


    var total: Double = 0.00
    var charity: Double = 0.00
    var community: Double = 0.00
    var development: Double = 0.00
}