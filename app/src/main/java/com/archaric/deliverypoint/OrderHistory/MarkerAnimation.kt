package com.archaric.deliverypoint.OrderHistory

import android.os.Handler
import android.os.SystemClock

import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Interpolator

import com.google.android.gms.maps.model.LatLng

import com.google.android.gms.maps.model.Marker
import java.lang.Math.*


object MarkerAnimation {
    @JvmStatic
    fun animateMarker(marker: Marker, finalPosition: LatLng?, latLngInterpolator: LatLngInterpolator) {
        val startPosition = marker.position
        val handler = Handler()
        val start = SystemClock.uptimeMillis()
        val interpolator: Interpolator = AccelerateDecelerateInterpolator()
        val durationInMs = 2000f
        handler.post(object : Runnable {
            var elapsed: Long = 0
            var t = 0f
            var v = 0f
            override fun run() {
                // Calculate progress using interpolator
                elapsed = SystemClock.uptimeMillis() - start
                t = elapsed / durationInMs
                v = interpolator.getInterpolation(t)
                latLngInterpolator.interpolate(v, startPosition, finalPosition)?.let { marker.setPosition(it) }
                // Repeat till progress is complete.
                if (t < 1) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16)
                }
            }
        })
    }



}

interface LatLngInterpolator {
    fun interpolate(fraction: Float, a: LatLng?, b: LatLng?): LatLng?
    class Spherical : LatLngInterpolator {
        /* From github.com/googlemaps/android-maps-utils */
        override fun interpolate(fraction: Float, a: LatLng?, b: LatLng?): LatLng {
            // http://en.wikipedia.org/wiki/Slerp
            val fromLat: Double = toRadians(a?.latitude!!)
            val fromLng: Double = toRadians(a.longitude)
            val toLat: Double = toRadians(b?.latitude!!)
            val toLng: Double = toRadians(b.longitude)
            val cosFromLat: Double = cos(fromLat)
            val cosToLat: Double = cos(toLat)
            // Computes Spherical interpolation coefficients.
            val angle = computeAngleBetween(fromLat, fromLng, toLat, toLng)
            val sinAngle: Double = sin(angle)
            if (sinAngle < 1E-6) {
                return a
            }
            val a: Double = sin((1 - fraction) * angle) / sinAngle
            val b: Double = sin(fraction * angle) / sinAngle
            // Converts from polar to vector and interpolate.
            val x: Double = a * cosFromLat * cos(fromLng) + b * cosToLat * cos(toLng)
            val y: Double = a * cosFromLat * sin(fromLng) + b * cosToLat * sin(toLng)
            val z: Double = a * sin(fromLat) + b * sin(toLat)
            // Converts interpolated vector back to polar.
            val lat: Double = atan2(z, sqrt(x * x + y * y))
            val lng: Double = atan2(y, x)
            return LatLng(toDegrees(lat), toDegrees(lng))
        }

        private fun computeAngleBetween(fromLat: Double, fromLng: Double, toLat: Double, toLng: Double): Double {

            val dLat: Double = fromLat - toLat
            val dLng = fromLng - toLng
            return 2 * asin(sqrt(pow(sin(dLat / 2), 2.0) +
                    cos(fromLat) * cos(toLat) * pow(sin(dLng / 2), 2.0)))
        }
    }
}