package us.ihmc.airly

import io.reactivex.Flowable
import us.ihmc.airly.AirlyAPI.UNLIMITED_RESULTS

fun AirlyAPI.getNearestInstallation(latitude: Double, longitude: Double, maxDistanceKm: Double, maxResults: Int): Flowable<Installation> {

    return getNearestInstallations(latitude, longitude, maxDistanceKm, maxResults)
            .flatMap { Flowable.fromIterable(it) }
}

fun AirlyAPI.getInstallationInArea(area: Area): Flowable<Installation> {

    val barycenter = area.getBarycenter()
    val km = Math.max(
            greatCircleDistanceInKm(area.sw, barycenter),
            greatCircleDistanceInKm(area.ne, barycenter))
    return getNearestInstallation(barycenter.latitude, barycenter.longitude, km, UNLIMITED_RESULTS)
            .filter { area.contains(it.location) }
}

fun AirlyAPI.getInstallationsInArea(area: Area): Flowable<List<Installation>> = getInstallationInArea(area)
            .toList().toFlowable()

fun AirlyAPI.getMeasurementInArea(area: Area): Flowable<LocatedMeasurement> = getInstallationInArea(area)
        .concatMap { installation -> getMeasurements(installation.id)
                .map { LocatedMeasurement(it, installation) } }

fun AirlyAPI.getMeasurementsInArea(area: Area): Flowable<MutableList<LocatedMeasurement>> = getMeasurementInArea(area)
        .toList().toFlowable()

