package com.lakbay.pamayanan.utils

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.lakbay.pamayanan.viewModels.UserLocation
import java.util.*
import kotlin.concurrent.thread

class GeoLocationUtils {

    companion object {

        fun getCurrentLocation(context: Context, callback: (Location?) -> Unit) {
            thread {
                val fusedLocationProviderClient: FusedLocationProviderClient =
                    LocationServices.getFusedLocationProviderClient(context)

                if (ActivityCompat.checkSelfPermission(context,
                        Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    fusedLocationProviderClient.lastLocation.addOnSuccessListener { location: Location? ->
                        callback(location)
                    }
                } else {
                    ActivityCompat.requestPermissions(
                        context as Activity,
                        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                        1
                    )
                    callback(null)
                }
            }
        }

        fun saveLocation(context: Context, location: Location) {
            SharedPrefUtils.saveData(context, CommonConstants.USER_LOCATION, convertLocToAddress(context, location).toString())
        }

        fun convertLocToAddress(context: Context, location: Location) : UserLocation {
            val userLocation: UserLocation = UserLocation()
            val addressList = getAddressFromLocation(context, location.latitude, location.longitude)
            if (addressList != null && addressList.isNotEmpty()) {
                val address = addressList[0]
                val addressLine = address.getAddressLine(0) // The first address line
                val city = address.locality
                val state = address.adminArea
                val postalCode = address.postalCode

                Log.e("LOCATION", "Postal $postalCode")
                with(userLocation) {
                    this.lat = location.latitude
                    this.lng = location.longitude
                    this.city = city
                    this.address = addressLine
                    this.state = state
//                    this.postalCode = Integer.parseInt(postalCode)
                }

            }

            return userLocation
        }

        private fun getAddressFromLocation(context: Context, latitude: Double, longitude: Double, maxResults: Int = 1, locale: Locale = Locale.getDefault()): List<Address>? {
            val geocoder = Geocoder(context, locale)
            val addresses: List<Address>?
            try {
                addresses = geocoder.getFromLocation(latitude, longitude, maxResults)
            } catch (e: Exception) {
                e.printStackTrace()
                return null
            }
            return addresses
        }

    }
}