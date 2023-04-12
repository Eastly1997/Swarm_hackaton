package com.lakbay.pamayanan.viewModels

class UserLocation {
    var address: String = ""
    var lat: Double = 0.0
    var lng: Double = 0.0
    var city: String = ""
    var state: String = ""
    var postalCode: Number = 0

    override fun toString(): String {
        return "UserLocation{" +
                "address='" + address + "'," +
                "lat='" + lat + "'," +
                "lng='" + lng + "'," +
                "city='" + city + "'," +
                "state='" + state + "'," +
                "postalCode='" + postalCode + "'," +
                "}"
    }
}