package com.labs.sauti.math

import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class LatLon {

    companion object {
        const val EARTH_RAD = 6371.0

        /**
         * @return km
         * */
        fun distance(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
            val latRad = Math.toRadians(lat2 - lat1)
            val lonRad = Math.toRadians(lon2 - lon1)

            val latRadHalf = latRad * 0.5
            val lonRadHalf = lonRad * 0.5
            val a = sin(latRadHalf) * sin(latRadHalf) +
                    cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
                    sin(lonRadHalf) * sin(lonRadHalf)

            val c = 2.0 * atan2(sqrt(a), sqrt(1.0-a))
            return EARTH_RAD * c
        }
    }

}