package com.example.myui.common

import android.content.Context
import android.util.Log
import com.works.solutionchallange2024.model.LocationData

class GetLocation() {
    var productLongitude:Double=0.0
    var productLatitude :Double =0.0

    fun checkLastLocation(locationData:LocationData): LocationData
    {
        var lastLocationData = locationData
        productLongitude = locationData.longitude.toDouble()
        productLatitude= locationData.latitude.toDouble()
        return lastLocationData
    }
    fun getLatitude():Double
    {
        return productLatitude
    }
    fun getLongitude():Double
    {

        return productLongitude
    }

}