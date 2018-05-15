package us.ihmc.airly.api.models

/**
 * Created by gbenincasa on 5/15/18.
 */

data class Area(val sw: Coordinates, val ne: Coordinates)

data class Address(var country: String = "", var locality: String = "",
                   var route: String = "", var streetNumber: String = "")

data class Coordinates(var latitude: Float = 0.0.toFloat(), var longitude: Float = 0.0.toFloat())

data class SensorInfoWithPollutionLevel(var id: String = "", val pollutionLevel: Int = 0,
                                        val address: Address = Address(),
                                        val location: Coordinates = Coordinates()) {
    override fun toString(): String {
        return "$id: $pollutionLevel"
    }
}

