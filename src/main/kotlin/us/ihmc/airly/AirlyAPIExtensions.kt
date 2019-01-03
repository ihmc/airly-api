package us.ihmc.airly

import io.reactivex.Flowable
import us.ihmc.airly.AirlyAPI.UNLIMITED_RESULTS
import us.ihmc.airly.api.models.Area
import us.ihmc.airly.api.models.Installation
import us.ihmc.airly.api.models.LocatedMeasurement

fun AirlyAPI.getInstallationsInArea(area: Area): Flowable<List<Installation>> {

    val barycenter = area.getBarycenter()
    val km = Math.max(
            greatCircleDistanceInKm(area.sw, barycenter),
            greatCircleDistanceInKm(area.ne, barycenter))
    return getNearestInstallations(barycenter.latitude, barycenter.longitude, km, UNLIMITED_RESULTS)
            .flatMap { Flowable.fromIterable(it) }
            .filter { area.contains(it.location) }
            .toList().toFlowable()
}

fun AirlyAPI.getMeasurementsInArea(area: Area): Flowable<MutableList<LocatedMeasurement>> = getInstallationsInArea(area)
        .flatMap { Flowable.fromIterable(it) }
        .concatMap { installation -> getMeasurements(installation.id)
                .map { LocatedMeasurement(it, installation) } }
        .toList().toFlowable()

