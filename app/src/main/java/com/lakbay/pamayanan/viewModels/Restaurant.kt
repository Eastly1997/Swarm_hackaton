package com.lakbay.pamayanan.viewModels

import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter

class Restaurant {

    var category_product: ArrayList<String> = ArrayList<String>()
    var category_restaurant: ArrayList<String> = ArrayList<String>()
    var location: ArrayList<Double> = ArrayList<Double>()
    var img: String = ""
    var name: String = ""
    var rating: Double = 0.0
    var uid:String = ""
    var workingHours : ArrayList<WorkingHours> = ArrayList<WorkingHours>()


    class WorkingHours {
        var close: String = "23:59"
        var open: String = "00:00"
    }

    fun getClosing() : String {
        val currentDate: LocalDate = LocalDate.now()
        val index =  currentDate.dayOfWeek.value

        val time = LocalTime.parse(workingHours[index].close+":00")
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")

        return time.format(formatter)
    }


    fun getOpening() : String {
        val currentDate: LocalDate = LocalDate.now()
        val index =  currentDate.dayOfWeek.value

        val time = LocalTime.parse(workingHours[index].open+":00")
        val formatter = DateTimeFormatter.ofPattern("hh:mm a")

        return time.format(formatter)
    }

    fun isOpen(): Boolean {
        val currentDate: LocalDate = LocalDate.now()
        val currentTime = LocalTime.now()
        val index =  currentDate.dayOfWeek.value

        val openHours = workingHours[index].open.split(":")
        val closeHours = workingHours[index].close.split(":")
        val openLocal = LocalTime.of(Integer.parseInt(openHours[0]), Integer.parseInt(openHours[1]), 0)
        val closeLocal = LocalTime.of(Integer.parseInt(closeHours[0]), Integer.parseInt(closeHours[1]), 0)

        return currentTime.isAfter(openLocal) && currentTime.isBefore(closeLocal)
    }

}