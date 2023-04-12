package com.lakbay.pamayanan.viewModels


import com.google.firebase.firestore.IgnoreExtraProperties
import java.io.Serializable
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@IgnoreExtraProperties
class Restaurant : Serializable {

    var category_product: ArrayList<String> = ArrayList<String>()
    var category_restaurant: ArrayList<String> = ArrayList<String>()
    var location: ArrayList<Double> = ArrayList<Double>()
    var img: String = ""
    var name: String = ""
    var rating: Double = 0.0
    var uid:String = ""
    var working_hours : ArrayList<WorkingHours> = ArrayList<WorkingHours>()


    class WorkingHours : Serializable {
        var close: String = "23:59"
        var open: String = "00:00"
    }

    fun getClosing() : String {
        val currentDate: LocalDate = LocalDate.now()
        val index =  currentDate.dayOfWeek.value

        val time = LocalTime.parse(working_hours[index].close+":00")
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")

        return time.format(formatter)
    }


    fun getOpening() : String {
        val currentDate: LocalDate = LocalDate.now()
        val index =  currentDate.dayOfWeek.value

        val time = LocalTime.parse(working_hours[index].open+":00")
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")

        return time.format(formatter)
    }

    fun isOpen(): Boolean {
        val currentDate: LocalDate = LocalDate.now()
        val currentTime = LocalTime.now()
        val index =  currentDate.dayOfWeek.value

        val openHours = working_hours[index].open.split(":")
        val closeHours = working_hours[index].close.split(":")
        val openLocal = LocalTime.of(Integer.parseInt(openHours[0]), Integer.parseInt(openHours[1]), 0)
        val closeLocal = LocalTime.of(Integer.parseInt(closeHours[0]), Integer.parseInt(closeHours[1]), 0)

        return currentTime.isAfter(openLocal) && currentTime.isBefore(closeLocal)
    }

}