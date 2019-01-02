package us.ihmc.airly

import us.ihmc.airly.api.models.Area
import us.ihmc.airly.api.models.Location

fun Area.contains(location: Location) = contains(location.latitude, sw.latitude, ne.latitude) &&
        contains(location.longitude, sw.longitude, ne.longitude)

fun Area.getBarycenter() = Location(
        getMidpoint(sw.latitude, ne.latitude),
        getMidpoint(sw.longitude, ne.longitude))

fun greatCircleDistanceInKm(point1: Location, point2: Location): Double {
    return if ((point1.latitude == point2.latitude) && (point1.longitude == point2.longitude)) {
        0.0
    } else {
        val theta = point1.longitude - point2.longitude
        var dist = Math.sin(Math.toRadians(point1.latitude)) *
                Math.sin(Math.toRadians(point2.latitude)) +
                Math.cos(Math.toRadians(point1.latitude)) *
                Math.cos(Math.toRadians(point2.latitude)) *
                Math.cos(Math.toRadians(theta))
        dist = Math.acos(dist)
        dist = Math.toDegrees(dist)
        dist * 60.0 * 1.1515 * 1.609344
    }
}

internal fun contains(p: Double, begin: Double, end: Double) = (p >= Math.min(begin, end)) &&
        (p <= Math.max(begin, end))

internal fun getMidpoint(p1: Double, p2: Double) = Math.min(p1, p2) + (Math.abs(p1 - p2) / 2)